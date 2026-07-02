package cheatsheethibernate.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;

@Repository
@Transactional
public class CheatSheetRepositoryImpl implements CheatSheetRepository {

	@Autowired
	private SessionFactory sessionFactory;

	// ===== BASIC CRUD =====

	@Override
	public CheatSheet findById(Long id) {
		return sessionFactory.getCurrentSession().get(CheatSheet.class, id);
	}

	@Override
	public List<CheatSheet> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.status = 'published' ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.getResultList();
	}

	@Override
	public void save(CheatSheet cheatSheet) {
		sessionFactory.getCurrentSession().saveOrUpdate(cheatSheet);
	}
	@Override
	public void delete(CheatSheet cheatSheet) {
	    if (cheatSheet != null && cheatSheet.getId() != null) {
	        Session session = sessionFactory.getCurrentSession();
	        Long sheetId = cheatSheet.getId();
	        
	        // 🎯 ၁။ ရှေးဦးစွာ reviews Table ထဲက သက်ဆိုင်ရာ data များကို Native SQL ဖြင့် အရင်ဖျက်ပါ
	        session.createNativeQuery("DELETE FROM reviews WHERE cheatsheet_id = :sheetId")
	               .setParameter("sheetId", sheetId)
	               .executeUpdate();
	               
	        // 🎯 ၂။ comments Table ထဲက parent_id ရှိနေသော Replies (သားသမီး) များကို Native SQL ဖြင့် အရင်ဖျက်ပါ
	        session.createNativeQuery("DELETE FROM comments WHERE cheatsheet_id = :sheetId AND parent_id IS NOT NULL")
	               .setParameter("sheetId", sheetId)
	               .executeUpdate();
	               
	        // 🎯 ٣။ Replies များ ကင်းစင်သွားပြီဖြစ်၍ မိခင် Comments များကို Native SQL ဖြင့် ဆက်လက်ဖျက်ပါ
	        session.createNativeQuery("DELETE FROM comments WHERE cheatsheet_id = :sheetId")
	               .setParameter("sheetId", sheetId)
	               .executeUpdate();
	               
	        // 🎯 ၄။ ကျန်ရှိနေသော cheatsheet_tags (ManyToMany Bridge Table) ထဲက သက်ဆိုင်ရာ လင့်များကို ဖြတ်တောက်ပါ
	        session.createNativeQuery("DELETE FROM cheatsheet_tags WHERE cheatsheet_id = :sheetId")
	               .setParameter("sheetId", sheetId)
	               .executeUpdate();
	        
	        // 🎯 ၅။ အနှောင်အဖွဲ့အားလုံး သေချာပေါက် ပြတ်တောက်သွားပြီဖြစ်၍ ပင်မ CheatSheet ကို အပြီးဖျက်ချခြင်း
	        session.delete(cheatSheet);
	    }
	}

	// ===== CATEGORY METHODS =====

	@Override
	public List<CheatSheet> findByCategoryId(int categoryId) {
		return sessionFactory.getCurrentSession().createQuery(
				"from CheatSheet c where c.category.categoryId = :categoryId and c.status = 'published' ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("categoryId", categoryId).getResultList();
	}

	@Override
	public List<CheatSheet> findByCategoryIgnoreCase(String category) {
		return sessionFactory.getCurrentSession().createQuery(
				"from CheatSheet c where lower(c.category.name) = lower(:category) and c.status = 'published' ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("category", category).getResultList();
	}

	// ===== USER METHODS =====

	@Override
	public List<CheatSheet> findByUser(User user) {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.user.userId = :userId ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.setParameter("userId", user.getUserId()).getResultList();
	}

	@Override
	public void saveUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public User findUserByUsername(String username) {
		List<User> users = sessionFactory.getCurrentSession()
				.createQuery("from User where username = :username", User.class).setParameter("username", username)
				.getResultList();
		return users.isEmpty() ? null : users.get(0);
	}

	// ===== NEW: USER + STATUS METHODS =====

	@Override
	public List<CheatSheet> findByUserAndStatus(User user, CheatSheet.Status status) {
		return sessionFactory.getCurrentSession().createQuery(
				"from CheatSheet c where c.user.userId = :userId and c.status = :status ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("userId", user.getUserId()).setParameter("status", status)
				.getResultList();
	}

	@Override
	public long countByUser(User user) {
		Long count = sessionFactory.getCurrentSession()
				.createQuery("select count(c) from CheatSheet c where c.user.userId = :userId", Long.class)
				.setParameter("userId", user.getUserId()).getSingleResult();
		return count != null ? count : 0L;
	}

	// ===== NEW: STATUS METHODS =====

	@Override
	public List<CheatSheet> findByStatus(CheatSheet.Status status) {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.status = :status ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("status", status).getResultList();
	}

	@Override
	public List<CheatSheet> findByCategoryIdAndStatus(int categoryId, CheatSheet.Status status) {
		return sessionFactory.getCurrentSession().createQuery(
				"from CheatSheet c where c.category.categoryId = :categoryId and c.status = :status ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("categoryId", categoryId).setParameter("status", status).getResultList();
	}

	// ===== NEW: TAG METHODS =====

	@Override
	public List<CheatSheet> findByTagId(Integer tagId) {
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c JOIN c.tags t WHERE t.tagId = :tagId and c.status = 'published' ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("tagId", tagId).getResultList();
	}

	@Override
	public List<CheatSheet> findByTagName(String tagName) {
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c JOIN c.tags t WHERE LOWER(t.name) = LOWER(:tagName) and c.status = 'published' ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("tagName", tagName).getResultList();
	}
	
	
	 @Override
	    public CheatSheet findByIdWithDetails(Long id) {
	        Session session = sessionFactory.getCurrentSession();
	        String hql = "SELECT DISTINCT c FROM CheatSheet c " +
	                     "LEFT JOIN FETCH c.user " +
	                     "LEFT JOIN FETCH c.category " +
	                     "LEFT JOIN FETCH c.tags " +
	                     "WHERE c.id = :id";
	        return session.createQuery(hql, CheatSheet.class)
	                      .setParameter("id", id)
	                      .uniqueResult();
	    }
	@Override
    public void saveReview(Review review) {
        sessionFactory.getCurrentSession().saveOrUpdate(review);
    }

    @Override
    public List<Review> findReviewsByCheatSheetId(Long cheatSheetId) {
        Session session = sessionFactory.getCurrentSession();
        // User Object ကိုပါ တစ်ခါတည်း Lazy Exception မဖြစ်အောင် JOIN FETCH လုပ်ထားပါသည်
        String hql = "from Review r JOIN FETCH r.user " +
                     "where r.cheatSheet.id = :csId and r.deletedAt is null " +
                     "order by r.createdAt desc";
        return session.createQuery(hql, Review.class)
                      .setParameter("csId", cheatSheetId)
                      .getResultList();
    }
 // 🎯 ဤ Cheat Sheet ပေါ်တွင် အဆိုပါ အသုံးပြုသူ ပေးဖူးသော Review အား ရှာဖွေပေးမည့် HQL ကုဒ်
    @Override
    public Review findReviewByUserAndCheatSheet(Integer userId, Long cheatSheetId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Review r where r.user.userId = :userId and r.cheatSheet.id = :csId and r.deletedAt is null";
        List<Review> list = session.createQuery(hql, Review.class)
                                   .setParameter("userId", userId)
                                   .setParameter("csId", cheatSheetId)
                                   .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Double getAverageRating(Long cheatSheetId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select avg(r.rating) from Review r where r.cheatSheet.id = :csId and r.deletedAt is null";
        Double avg = session.createQuery(hql, Double.class)
                            .setParameter("csId", cheatSheetId)
                            .uniqueResult();
        return avg != null ? avg : 0.0;
    }
    
    @Override
    public void saveComment(Comment comment) {
        sessionFactory.getCurrentSession().saveOrUpdate(comment);
    }

    @Override
    public List<Comment> findMainCommentsByCheatSheetId(Long cheatSheetId) {
        Session session = sessionFactory.getCurrentSession();
        
        // 🎯 FIX: c.user ရော c.replies ကိုပါ JOIN FETCH သုံးပြီး Session မပိတ်ခင် တစ်ခါတည်း အပြီးဆွဲထုတ်ထားခြင်း
        String hql = "SELECT DISTINCT c FROM Comment c " +
                     "JOIN FETCH c.user " +
                     "LEFT JOIN FETCH c.replies r " +
                     "LEFT JOIN FETCH r.user " +
                     "WHERE c.cheatSheet.id = :csId AND c.parentComment IS NULL AND c.deletedAt IS NULL " +
                     "ORDER BY c.createdAt DESC";
                     
        return session.createQuery(hql, Comment.class)
                      .setParameter("csId", cheatSheetId)
                      .getResultList();
    }

    @Override
    public Comment findCommentById(Integer commentId) {
        return sessionFactory.getCurrentSession().get(Comment.class, commentId);
    }

	// ===== NEW: SEARCH METHODS =====

	@Override
	public List<CheatSheet> searchByKeyword(String keyword) {
		return sessionFactory.getCurrentSession().createQuery(
				"SELECT c FROM CheatSheet c WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) and c.status = 'published' ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("keyword", keyword).getResultList();
	}

	// ===== NEW: SPECIAL QUERIES =====

	@Override
	public List<CheatSheet> findAllPublished() {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.status = 'published' ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.getResultList();
	}

	@Override
	public List<CheatSheet> findRecentSheets() {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.status = 'published' ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findMostLiked() {
		return sessionFactory.getCurrentSession()
				.createQuery("from CheatSheet c where c.status = 'published' ORDER BY c.likeCount DESC",
						CheatSheet.class)
				.setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findTopRated() {
		return sessionFactory.getCurrentSession().createQuery(
				"from CheatSheet c where c.status = 'published' and c.ratingCount > 0 ORDER BY (c.ratingSum / c.ratingCount) DESC",
				CheatSheet.class).setMaxResults(10).getResultList();
	}

	// ===== NEW: INTERACTION METHODS =====

	@Override
	public void incrementLikeCount(Long sheetId) {
		sessionFactory.getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.likeCount = COALESCE(c.likeCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void decrementLikeCount(Long sheetId) {
		sessionFactory.getCurrentSession().createQuery(
				"UPDATE CheatSheet c SET c.likeCount = CASE WHEN c.likeCount > 0 THEN c.likeCount - 1 ELSE 0 END WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void incrementCommentCount(Long sheetId) {
		sessionFactory.getCurrentSession()
				.createQuery(
						"UPDATE CheatSheet c SET c.commentCount = COALESCE(c.commentCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void decrementCommentCount(Long sheetId) {
		sessionFactory.getCurrentSession().createQuery(
				"UPDATE CheatSheet c SET c.commentCount = CASE WHEN c.commentCount > 0 THEN c.commentCount - 1 ELSE 0 END WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void incrementViewCount(Long sheetId) {
		sessionFactory.getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.viewCount = COALESCE(c.viewCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void updateRating(Long sheetId, int ratingValue) {
		// TODO Auto-generated method stub
		
	}

	
	}
