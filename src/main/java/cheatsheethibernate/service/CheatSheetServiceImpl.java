package cheatsheethibernate.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.CheatSheetRepository;
import cheatsheethibernate.repository.AdminContentRepository; // ✅ Tag Filter အတွက် Repo သစ်အား တွဲဖက်သုံးခြင်း


@Service
@Transactional // ✅ Active Hibernate Session Transaction များ မှန်ကန်စေရန်
public class CheatSheetServiceImpl implements CheatSheetService {

    @Autowired
    private CheatSheetRepository cheatSheetRepository;

    @Autowired
    private AdminContentRepository adminContentRepository; // ✅ Injecting Admin Content Repository

    @Override
    public List<CheatSheet> getCheatSheetsByCategory(String category) {
        return cheatSheetRepository.findByCategoryIgnoreCase(category);
    }

    // 🎯 Tag Name အလိုက် CheatSheet များကို Repository မှတစ်ဆင့် ရှာဖွေပေးခြင်း
    @Override
    public List<CheatSheet> getCheatSheetsByTag(String tagName) {
        return adminContentRepository.findSheetsByTagName(tagName);
    }

    @Override
    public List<CheatSheet> getAllCheatSheets() {
        return cheatSheetRepository.findAll();
    }

    @Override
    public void saveCheatSheet(CheatSheet cheatSheet) {
        cheatSheetRepository.save(cheatSheet);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CheatSheet> getCheatSheetsByUser(User user) {
        // Repository ထဲက တို့တွေပြင်ထားတဲ့ findByUser ကို လှမ်းခေါ်ခြင်း
        return cheatSheetRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public CheatSheet getCheatSheetById(Long id) {
        // Repository ထဲက findById ကို လှမ်းခေါ်ခြင်း
        return cheatSheetRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteCheatSheet(CheatSheet cheatSheet) {
        // Repository ထဲက delete ကို လှမ်းခေါ်ခြင်း
        cheatSheetRepository.delete(cheatSheet);
    }

    // ==== User Auth ပိုင်းအတွက် Implementation ကုဒ်များ ====

    @Override
    public void registerUser(User user) {
        cheatSheetRepository.saveUser(user);
    }

    @Override
    public User loginUser(String username, String password) {
        User user = cheatSheetRepository.findUserByUsername(username);
        
        // passwordHash ကို အသုံးပြု၍ တိုက်ရိုက် စစ်ဆေးခြင်း
        if (user != null && user.getPasswordHash() != null && user.getPasswordHash().equals(password)) {
            return user; 
        }
        return null; 
    }

	@Override
	public void deleteCheatSheet(Long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
    public void addReview(Long cheatSheetId, User user, Integer rating, String text) {
        CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
        if (cheatSheet != null) {
            Review review = new Review();
            review.setCheatSheet(cheatSheet);
            review.setUser(user);
            review.setRating(rating);
            review.setReviewText(text);
            
            cheatSheetRepository.saveReview(review);
        }
    }
	@Override
    @Transactional(readOnly = true)
    public CheatSheet getCheatSheetDetailsById(Long id) {
        return cheatSheetRepository.findByIdWithDetails(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByCheatSheet(Long cheatSheetId) {
        return cheatSheetRepository.findReviewsByCheatSheetId(cheatSheetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Long cheatSheetId) {
        return cheatSheetRepository.getAverageRating(cheatSheetId);
    }
    @Override
    public void addOrUpdateReview(Long cheatSheetId, User user, Integer rating, String text) {
        // ၁။ လက်ရှိ User က ဒီ Cheat Sheet ပေါ်မှာ Review ပေးဖူးခြင်း ရှိ၊ မရှိ စစ်ဆေးသည်
        Review existingReview = cheatSheetRepository.findReviewByUserAndCheatSheet(user.getUserId(), cheatSheetId);
        
        if (existingReview != null) {
            // ၂။ ပေးဖူးပါက ဒေတာအသစ်ထပ်မပွားစေဘဲ လက်ရှိ Record အဟောင်းပေါ်တွင်သာ အသစ်ပြင်ဆင် (Update) ပါသည်
            existingReview.setRating(rating);
            existingReview.setReviewText(text);
            cheatSheetRepository.saveReview(existingReview);
        } else {
            // ၃။ လုံးဝမပေးဖူးသေးပါက Record အသစ်တစ်ခုအဖြစ် စတင်တည်ဆောက် (Insert) ပါသည်
            CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
            if (cheatSheet != null) {
                Review review = new Review();
                review.setCheatSheet(cheatSheet);
                review.setUser(user);
                review.setRating(rating);
                review.setReviewText(text);
                cheatSheetRepository.saveReview(review);
            }
        }}
     @Override
     public void addComment(Long cheatSheetId, User user, String content, Integer parentId) {
         CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
         if (cheatSheet != null) {
             Comment comment = new Comment();
             comment.setCheatSheet(cheatSheet);
             comment.setUser(user);
             comment.setContent(content);
             
             // 🎯 parentId ပါလာပါက Reply အဖြစ် သတ်မှတ်ပြီး Parent Object နှင့် ချိတ်ဆက်ပေးပါမည်
             if (parentId != null) {
                 Comment parent = cheatSheetRepository.findCommentById(parentId);
                 if (parent != null) {
                     comment.setParentComment(parent);
                 }
             }
             
             cheatSheetRepository.saveComment(comment);
         }
     }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getMainCommentsByCheatSheet(Long cheatSheetId) {
        return cheatSheetRepository.findMainCommentsByCheatSheetId(cheatSheetId);
    }
}