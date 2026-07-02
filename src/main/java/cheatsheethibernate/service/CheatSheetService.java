package cheatsheethibernate.service;

import java.util.List;
import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;

public interface CheatSheetService {
    
    List<CheatSheet> getCheatSheetsByCategory(String category);
    
    // 🎯 ရှေ့က Tag Filter လည်ပတ်နိုင်ရန် ဖြည့်စွက်ထားသော Method
    List<CheatSheet> getCheatSheetsByTag(String tagName); 
   
    List<CheatSheet> getAllCheatSheets();
    
    void saveCheatSheet(CheatSheet cheatSheet);

    // ==== User Auth Methods ====
    void registerUser(User user);
    User loginUser(String username, String password);
    
    List<CheatSheet> getCheatSheetsByUser(User user);

    // 🎯 Edit Form အတွက် ID အလိုက် ဒေတာတစ်ခုတည်း ဆွဲထုတ်ရန်
    CheatSheet getCheatSheetById(Long id);

    // 🎯 စနစ်ထဲမှ ဖျက်သိမ်းရန်
    void deleteCheatSheet(Long id);

	void deleteCheatSheet(CheatSheet cheatSheet);
	CheatSheet getCheatSheetDetailsById(Long id);
	void addReview(Long cheatSheetId, User user, Integer rating, String text);
    List<Review> getReviewsByCheatSheet(Long cheatSheetId);
    Double getAverageRating(Long cheatSheetId);
    void addOrUpdateReview(Long cheatSheetId, User user, Integer rating, String text);
    void addComment(Long cheatSheetId, User user, String content, Integer parentId);
    List<Comment> getMainCommentsByCheatSheet(Long cheatSheetId);
}