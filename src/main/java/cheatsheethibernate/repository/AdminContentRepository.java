package cheatsheethibernate.repository;

import java.util.List;

import cheatsheethibernate.entity.Announcement;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Tag;

public interface AdminContentRepository {
    // ==========================================
    // Categories
    // ==========================================
    List<Category> findAllCategories();
    Category findCategoryById(int id);
    void saveCategory(Category cat);
    void deleteCategory(int id);

    // ==========================================
    // Tags (🎯 Integer သို့ ပြောင်းလဲထားသည်)
    // ==========================================
    List<Tag> findAllTags();
    Tag findTagById(Integer id); // Long မှ Integer သို့ ပြောင်းပါ
    void saveTag(Tag tag);
    void deleteTag(Integer id);  // Long မှ Integer သို့ ပြောင်းပါ

    // ==========================================
    // CheatSheet_Tags Mapping
    // ==========================================
    // (အကယ်၍ CheatSheet ID များက Long ဖြစ်ပါက ဤနေရာတွင် Long အတိုင်းထားပါ)
    void addTagToCheatSheet(Long cheatSheetId, Integer tagId);

    // ==========================================
    // Announcements
    // ==========================================
    List<Announcement> findAllAnnouncements();
    void saveAnnouncement(Announcement ann);
    void deleteAnnouncement(Long id);
	List<CheatSheet> findSheetsByTagName(String tagName);
	void addTagToCheatSheet(Long cheatSheetId, Long tagId);
}