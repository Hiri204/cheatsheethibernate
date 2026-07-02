package cheatsheethibernate.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cheatsheethibernate.entity.Announcement;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.CheatSheet; // 🎯 import အသစ်ထည့်ပါ
import cheatsheethibernate.entity.Tag;
import cheatsheethibernate.repository.AdminContentRepository;

@Service
@Transactional 
public class AdminContentServiceImpl implements AdminContentService {

    @Autowired
    private AdminContentRepository contentRepo;

    // ==========================================
    // Categories Implementation
    // ==========================================
    @Override
    public List<Category> getAllCategories() { 
        return contentRepo.findAllCategories(); 
    }
    
    @Override
    public Category findCategoryById(int id) { 
        return contentRepo.findCategoryById(id); 
    }

    @Override
    public void saveCategory(Category cat) { 
        contentRepo.saveCategory(cat); 
    }

    @Override
    public void deleteCategory(int id) { 
        Category category = contentRepo.findCategoryById(id);
        if (category != null) {
            contentRepo.deleteCategory(id); 
        }
    }

    // ==========================================
    // Tags Implementation
    // ==========================================
    @Override
    public List<Tag> getAllTags() { 
        return contentRepo.findAllTags(); 
    }
    
    @Override
    public Tag findTagById(Integer id) { 
        return contentRepo.findTagById(id); 
    }
    
    @Override
    public void saveTag(Tag tag) { 
        contentRepo.saveTag(tag); 
    }
    
    @Override
    public void deleteTag(Integer id) { 
        contentRepo.deleteTag(id); 
    }
    
    // ==========================================
    // CheatSheet & Tags Mapping Implementation
    // ==========================================
    @Override
    public void assignTagToCheatSheet(Long sheetId, Integer tagId) { 
        contentRepo.addTagToCheatSheet(sheetId, tagId); 
    }

    // 🎯 ဤ Method အသစ်ကို အောက်တွင် ထည့်ပေးလိုက်ပါ
    @Override
    public List<CheatSheet> findSheetsByTagName(String tagName) {
        return contentRepo.findSheetsByTagName(tagName);
    }

    // ==========================================
    // Announcements Implementation
    // ==========================================
    @Override
    public List<Announcement> getAllAnnouncements() { 
        return contentRepo.findAllAnnouncements(); 
    }
    
    @Override
    public void saveAnnouncement(Announcement ann) { 
        contentRepo.saveAnnouncement(ann); 
    }
    
    @Override
    public void deleteAnnouncement(Long id) { 
        contentRepo.deleteAnnouncement(id); 
    }
}