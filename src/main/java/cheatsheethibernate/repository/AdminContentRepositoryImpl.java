package cheatsheethibernate.repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cheatsheethibernate.entity.Announcement;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Tag;

@Repository
public class AdminContentRepositoryImpl implements AdminContentRepository {

    @Autowired
    private SessionFactory sessionFactory;

    // ... (အခြား method များ ယခင်အတိုင်း ထားပါ) ...

    // ==========================================
    // 🎯 Tag အလိုက် CheatSheet ရှာရန် (အသစ်ထည့်သွင်းရန်)
    // ==========================================
    @Override
    public List<CheatSheet> findSheetsByTagName(String tagName) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT DISTINCT c FROM CheatSheet c JOIN c.tags t WHERE t.name = :tagName", CheatSheet.class)
            .setParameter("tagName", tagName)
            .getResultList();
    }

    // ==========================================
    // Categories
    // ==========================================
    @Override
    public List<Category> findAllCategories() {
        return sessionFactory.getCurrentSession().createQuery("from Category", Category.class).getResultList();
    }

    @Override
    public Category findCategoryById(int id) {
        return sessionFactory.getCurrentSession().get(Category.class, id);
    }

    @Override
    public void saveCategory(Category cat) { 
        sessionFactory.getCurrentSession().saveOrUpdate(cat); 
    }
    
    @Override
    public void deleteCategory(int id) {
        Category cat = sessionFactory.getCurrentSession().get(Category.class, id);
        if (cat != null) sessionFactory.getCurrentSession().delete(cat);
    }

    // ==========================================
    // Tags
    // ==========================================
    @Override
    public List<Tag> findAllTags() {
        return sessionFactory.getCurrentSession().createQuery("from Tag", Tag.class).getResultList();
    }
    
    @Override
    public Tag findTagById(Integer id) {
        return sessionFactory.getCurrentSession().get(Tag.class, id); 
    }
    
    @Override
    public void saveTag(Tag tag) { 
        sessionFactory.getCurrentSession().saveOrUpdate(tag); 
    }
    
    @Override
    public void deleteTag(Integer id) {
        Tag tag = sessionFactory.getCurrentSession().get(Tag.class, id);
        if (tag != null) sessionFactory.getCurrentSession().delete(tag);
    }

    // ==========================================
    // CheatSheet_Tags Mapping
    // ==========================================
    @Override
    public void addTagToCheatSheet(Long cheatSheetId, Long tagId) {
        CheatSheet sheet = sessionFactory.getCurrentSession().get(CheatSheet.class, cheatSheetId);
        Tag tag = sessionFactory.getCurrentSession().get(Tag.class, tagId.intValue()); 
        
        if (sheet != null && tag != null) {
            sheet.getTags().add(tag);
            sessionFactory.getCurrentSession().saveOrUpdate(sheet);
        }
    }

    // ==========================================
    // Announcements
    // ==========================================
    @Override
    public List<Announcement> findAllAnnouncements() {
        return sessionFactory.getCurrentSession().createQuery("from Announcement order by id desc", Announcement.class).getResultList();
    }
    
    @Override
    public void saveAnnouncement(Announcement ann) { 
        sessionFactory.getCurrentSession().saveOrUpdate(ann); 
    }
    
    @Override
    public void deleteAnnouncement(Long id) {
        Announcement ann = sessionFactory.getCurrentSession().get(Announcement.class, id);
        if (ann != null) sessionFactory.getCurrentSession().delete(ann);
    }

	@Override
	public void addTagToCheatSheet(Long cheatSheetId, Integer tagId) {
		// TODO Auto-generated method stub
		
	}
}