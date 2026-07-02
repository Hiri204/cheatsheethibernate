package cheatsheethibernate.service;

import java.util.List;
import cheatsheethibernate.entity.Announcement;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.CheatSheet; // 🎯 ဤ import အသစ်ကို ထည့်ပါ
import cheatsheethibernate.entity.Tag;

public interface AdminContentService {
    
    // ==========================================
    // Categories
    // ==========================================
    List<Category> getAllCategories();
    Category findCategoryById(int id);
    void saveCategory(Category cat);
    void deleteCategory(int id);

    // ==========================================
    // Tags
    // ==========================================
    List<Tag> getAllTags();
    Tag findTagById(Integer id);
    void saveTag(Tag tag);
    void deleteTag(Integer id);
    
    // ==========================================
    // CheatSheet & Tags Mapping
    // ==========================================
    void assignTagToCheatSheet(Long sheetId, Integer tagId);
    
    // 🎯 ဒီ method အသစ်ကို ထည့်သွင်းပေးလိုက်ပါ
    List<CheatSheet> findSheetsByTagName(String tagName);

    // ==========================================
    // Announcements
    // ==========================================
    List<Announcement> getAllAnnouncements();
    void saveAnnouncement(Announcement ann);
    void deleteAnnouncement(Long id);
}