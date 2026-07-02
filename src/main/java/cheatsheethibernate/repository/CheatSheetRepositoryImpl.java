package cheatsheethibernate.repository;

import java.time.LocalDateTime;
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

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	// ===== BASIC CRUD OPERATIONS =====

	@Override
	public CheatSheet findById(Long id) {
		return getCurrentSession().get(CheatSheet.class, id);
	}

	@Override
	public CheatSheet findByIdWithDetails(Long id) {
		String hql = "SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
				+ "LEFT JOIN FETCH c.tags " + "WHERE c.id = :id";
		return getCurrentSession().createQuery(hql, CheatSheet.class).setParameter("id", id).uniqueResult();
	}

	@Override
	public List<CheatSheet> findAll() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).getResultList();
	}

	@Override
	public void save(CheatSheet cheatSheet) {
		getCurrentSession().saveOrUpdate(cheatSheet);
	}

	@Override
	public void delete(CheatSheet cheatSheet) {
		if (cheatSheet != null && cheatSheet.getId() != null) {
			Session session = getCurrentSession();
			Long sheetId = cheatSheet.getId();

			// Delete in correct order to avoid foreign key violations
			session.createNativeQuery("DELETE FROM reports WHERE cheatsheet_id = :sheetId")
					.setParameter("sheetId", sheetId).executeUpdate();
			session.createNativeQuery("DELETE FROM reviews WHERE cheatsheet_id = :sheetId")
					.setParameter("sheetId", sheetId).executeUpdate();
			session.createNativeQuery("DELETE FROM comments WHERE cheatsheet_id = :sheetId AND parent_id IS NOT NULL")
					.setParameter("sheetId", sheetId).executeUpdate();
			session.createNativeQuery("DELETE FROM comments WHERE cheatsheet_id = :sheetId")
					.setParameter("sheetId", sheetId).executeUpdate();
			session.createNativeQuery("DELETE FROM cheatsheet_tags WHERE cheatsheet_id = :sheetId")
					.setParameter("sheetId", sheetId).executeUpdate();
			session.delete(cheatSheet);
		}
	}

	@Override
	public boolean exists(Long id) {
		Long count = getCurrentSession().createQuery("SELECT COUNT(c) FROM CheatSheet c WHERE c.id = :id", Long.class)
				.setParameter("id", id).getSingleResult();
		return count > 0;
	}

	@Override
	public long count() {
		return getCurrentSession().createQuery("SELECT COUNT(c) FROM CheatSheet c", Long.class).getSingleResult();
	}

	@Override
	public long countByUser(User user) {
		Long count = getCurrentSession()
				.createQuery("SELECT COUNT(c) FROM CheatSheet c WHERE c.user.userId = :userId", Long.class)
				.setParameter("userId", user.getUserId()).getSingleResult();
		return count != null ? count : 0L;
	}

	// ===== CATEGORY METHODS =====

	@Override
	public List<CheatSheet> findByCategoryId(int categoryId) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.category.categoryId = :categoryId AND c.status != 'banned' "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("categoryId", categoryId).getResultList();
	}

	@Override
	public List<CheatSheet> findByCategoryIgnoreCase(String category) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE LOWER(c.category.name) = LOWER(:category) AND c.status != 'banned' "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("category", category).getResultList();
	}

	@Override
	public List<CheatSheet> findByCategoryIdAndStatus(int categoryId, CheatSheet.Status status) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.category.categoryId = :categoryId AND c.status = :status "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("categoryId", categoryId).setParameter("status", status).getResultList();
	}

	@Override
	public List<CheatSheet> findByCategoryNameAndStatus(String categoryName, CheatSheet.Status status) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE LOWER(c.category.name) = LOWER(:categoryName) AND c.status = :status "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("categoryName", categoryName).setParameter("status", status).getResultList();
	}

	// ===== USER METHODS =====

	@Override
	public List<CheatSheet> findByUser(User user) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.user.userId = :userId " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("userId", user.getUserId()).getResultList();
	}

	@Override
	public List<CheatSheet> findByUserId(Integer userId) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.user.userId = :userId " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("userId", userId).getResultList();
	}

	@Override
	public List<CheatSheet> findByUserAndStatus(User user, CheatSheet.Status status) {
		return getCurrentSession()
				.createQuery(
						"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
								+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
								+ "WHERE c.user.userId = :userId AND c.status = :status " + "ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.setParameter("userId", user.getUserId()).setParameter("status", status).getResultList();
	}

	@Override
	public List<CheatSheet> findByUserIdAndStatus(Integer userId, CheatSheet.Status status) {
		return getCurrentSession()
				.createQuery(
						"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
								+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
								+ "WHERE c.user.userId = :userId AND c.status = :status " + "ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.setParameter("userId", userId).setParameter("status", status).getResultList();
	}

	@Override
	public void saveUser(User user) {
		getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public User findUserByUsername(String username) {
		List<User> users = getCurrentSession().createQuery("FROM User WHERE username = :username", User.class)
				.setParameter("username", username).getResultList();
		return users.isEmpty() ? null : users.get(0);
	}

	// ===== STATUS METHODS =====

	@Override
	public List<CheatSheet> findByStatus(CheatSheet.Status status) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = :status " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("status", status).getResultList();
	}

	@Override
	public List<CheatSheet> findAllPublished() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).getResultList();
	}

	@Override
	public List<CheatSheet> findAllPublished(int limit) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findRecentSheets() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findRecentSheets(int limit) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findByStatusIn(List<CheatSheet.Status> statuses) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status IN (:statuses) " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("statuses", statuses).getResultList();
	}

	// ===== TAG METHODS =====

	@Override
	public List<CheatSheet> findByTagId(Integer tagId) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "JOIN c.tags t "
						+ "WHERE t.tagId = :tagId AND c.status = 'published' " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("tagId", tagId).getResultList();
	}

	@Override
	public List<CheatSheet> findByTagName(String tagName) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags " + "JOIN c.tags t "
						+ "WHERE LOWER(t.name) = LOWER(:tagName) AND c.status = 'published' "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("tagName", tagName).getResultList();
	}

	@Override
	public List<CheatSheet> findByTagIdAndStatus(Integer tagId, CheatSheet.Status status) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "JOIN c.tags t "
						+ "WHERE t.tagId = :tagId AND c.status = :status " + "ORDER BY c.createdAt DESC",
				CheatSheet.class).setParameter("tagId", tagId).setParameter("status", status).getResultList();
	}

	// ===== SEARCH METHODS =====

	@Override
	public List<CheatSheet> searchByKeyword(String keyword) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
						+ "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " + "AND c.status = 'published' "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("keyword", keyword).getResultList();
	}

	@Override
	public List<CheatSheet> searchByKeywordAndStatus(String keyword, CheatSheet.Status status) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
						+ "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " + "AND c.status = :status "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("keyword", keyword).setParameter("status", status).getResultList();
	}

	@Override
	public List<CheatSheet> searchByKeywordWithPagination(String keyword, int offset, int limit) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE (LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
						+ "OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " + "AND c.status = 'published' "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("keyword", keyword).setFirstResult(offset).setMaxResults(limit).getResultList();
	}

	// ===== BAN METHODS =====

	@Override
	public List<CheatSheet> findBannedSheets() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'banned' " + "ORDER BY c.bannedAt DESC",
				CheatSheet.class).getResultList();
	}

	@Override
	public List<CheatSheet> findBannedSheetsByUser(User user) {
		return getCurrentSession()
				.createQuery(
						"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
								+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
								+ "WHERE c.user.userId = :userId AND c.status = 'banned' " + "ORDER BY c.bannedAt DESC",
						CheatSheet.class)
				.setParameter("userId", user.getUserId()).getResultList();
	}

	@Override
	public List<CheatSheet> findBannedSheetsByUserId(Integer userId) {
		return getCurrentSession()
				.createQuery(
						"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
								+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
								+ "WHERE c.user.userId = :userId AND c.status = 'banned' " + "ORDER BY c.bannedAt DESC",
						CheatSheet.class)
				.setParameter("userId", userId).getResultList();
	}

	@Override
	public List<CheatSheet> findBannedSheetsWithDetails() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'banned' " + "ORDER BY c.bannedAt DESC",
				CheatSheet.class).getResultList();
	}

	@Override
	public void updateBanStatus(Long sheetId, String status, String reason, LocalDateTime bannedAt,
			LocalDateTime expiresAt, Integer bannedBy) {
		String hql = "UPDATE CheatSheet c SET c.status = :status, c.banReason = :reason, "
				+ "c.bannedAt = :bannedAt, c.banExpiresAt = :expiresAt, c.bannedBy = :bannedBy " + "WHERE c.id = :id";
		getCurrentSession().createQuery(hql).setParameter("status", CheatSheet.Status.valueOf(status))
				.setParameter("reason", reason).setParameter("bannedAt", bannedAt).setParameter("expiresAt", expiresAt)
				.setParameter("bannedBy", bannedBy).setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void unbanSheet(Long sheetId) {
		String hql = "UPDATE CheatSheet c SET c.status = :status, c.banReason = null, "
				+ "c.bannedAt = null, c.banExpiresAt = null, c.bannedBy = null " + "WHERE c.id = :id";
		getCurrentSession().createQuery(hql).setParameter("status", CheatSheet.Status.published)
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public List<CheatSheet> findExpiredBans() {
		return getCurrentSession().createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
				+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
				+ "WHERE c.status = 'banned' AND c.banExpiresAt IS NOT NULL " + "AND c.banExpiresAt < :now",
				CheatSheet.class).setParameter("now", LocalDateTime.now()).getResultList();
	}

	@Override
	public List<CheatSheet> findBansExpiringSoon() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime soon = now.plusHours(24);
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.status = 'banned' AND c.banExpiresAt IS NOT NULL "
						+ "AND c.banExpiresAt BETWEEN :now AND :soon", CheatSheet.class)
				.setParameter("now", now).setParameter("soon", soon).getResultList();
	}

	@Override
	public List<CheatSheet> findActiveBans() {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.status = 'banned' AND (c.banExpiresAt IS NULL OR c.banExpiresAt > :now) "
						+ "ORDER BY c.bannedAt DESC", CheatSheet.class)
				.setParameter("now", LocalDateTime.now()).getResultList();
	}

	// ===== REVIEW METHODS =====

	@Override
	public void saveReview(Review review) {
		getCurrentSession().saveOrUpdate(review);
	}

	@Override
	public List<Review> findReviewsByCheatSheetId(Long cheatSheetId) {
		String hql = "FROM Review r JOIN FETCH r.user " + "WHERE r.cheatSheet.id = :csId AND r.deletedAt IS NULL "
				+ "ORDER BY r.createdAt DESC";
		return getCurrentSession().createQuery(hql, Review.class).setParameter("csId", cheatSheetId).getResultList();
	}

	@Override
	public List<Review> findReviewsByCheatSheetIdWithPagination(Long cheatSheetId, int offset, int limit) {
		String hql = "FROM Review r JOIN FETCH r.user " + "WHERE r.cheatSheet.id = :csId AND r.deletedAt IS NULL "
				+ "ORDER BY r.createdAt DESC";
		return getCurrentSession().createQuery(hql, Review.class).setParameter("csId", cheatSheetId)
				.setFirstResult(offset).setMaxResults(limit).getResultList();
	}

	@Override
	public Review findReviewByUserAndCheatSheet(Integer userId, Long cheatSheetId) {
		String hql = "FROM Review r WHERE r.user.userId = :userId AND r.cheatSheet.id = :csId AND r.deletedAt IS NULL";
		List<Review> list = getCurrentSession().createQuery(hql, Review.class).setParameter("userId", userId)
				.setParameter("csId", cheatSheetId).getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Double getAverageRating(Long cheatSheetId) {
		Double avg = getCurrentSession()
				.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.cheatSheet.id = :csId AND r.deletedAt IS NULL",
						Double.class)
				.setParameter("csId", cheatSheetId).uniqueResult();
		return avg != null ? avg : 0.0;
	}

	@Override
	public List<Object[]> getRatingDistribution(Long cheatSheetId) {
		return getCurrentSession()
				.createQuery("SELECT r.rating, COUNT(r) FROM Review r "
						+ "WHERE r.cheatSheet.id = :csId AND r.deletedAt IS NULL "
						+ "GROUP BY r.rating ORDER BY r.rating DESC", Object[].class)
				.setParameter("csId", cheatSheetId).getResultList();
	}

	@Override
	public void deleteReview(Integer reviewId) {
		String hql = "UPDATE Review r SET r.deletedAt = :deletedAt WHERE r.id = :id";
		getCurrentSession().createQuery(hql).setParameter("deletedAt", LocalDateTime.now()).setParameter("id", reviewId)
				.executeUpdate();
	}

	@Override
	public long countReviewsByCheatSheetId(Long cheatSheetId) {
		return getCurrentSession()
				.createQuery("SELECT COUNT(r) FROM Review r WHERE r.cheatSheet.id = :csId AND r.deletedAt IS NULL",
						Long.class)
				.setParameter("csId", cheatSheetId).getSingleResult();
	}

	// ===== COMMENT METHODS =====

	@Override
	public void saveComment(Comment comment) {
		getCurrentSession().saveOrUpdate(comment);
	}

	@Override
	public List<Comment> findMainCommentsByCheatSheetId(Long cheatSheetId) {
		String hql = "SELECT DISTINCT c FROM Comment c " + "JOIN FETCH c.user " + "LEFT JOIN FETCH c.replies r "
				+ "LEFT JOIN FETCH r.user "
				+ "WHERE c.cheatSheet.id = :csId AND c.parentComment IS NULL AND c.deletedAt IS NULL "
				+ "ORDER BY c.createdAt DESC";
		return getCurrentSession().createQuery(hql, Comment.class).setParameter("csId", cheatSheetId).getResultList();
	}

	@Override
	public List<Comment> findAllCommentsByCheatSheetId(Long cheatSheetId) {
		String hql = "FROM Comment c JOIN FETCH c.user " + "WHERE c.cheatSheet.id = :csId AND c.deletedAt IS NULL "
				+ "ORDER BY c.createdAt DESC";
		return getCurrentSession().createQuery(hql, Comment.class).setParameter("csId", cheatSheetId).getResultList();
	}

	@Override
	public Comment findCommentById(Integer commentId) {
		return getCurrentSession().get(Comment.class, commentId);
	}

	@Override
	public List<Comment> findRepliesByCommentId(Integer commentId) {
		String hql = "SELECT DISTINCT c FROM Comment c JOIN FETCH c.user "
				+ "WHERE c.parentComment.id = :parentId AND c.deletedAt IS NULL " + "ORDER BY c.createdAt ASC";
		return getCurrentSession().createQuery(hql, Comment.class).setParameter("parentId", commentId).getResultList();
	}

	@Override
	public void deleteComment(Integer commentId) {
		String hql = "UPDATE Comment c SET c.deletedAt = :deletedAt WHERE c.id = :id";
		getCurrentSession().createQuery(hql).setParameter("deletedAt", LocalDateTime.now())
				.setParameter("id", commentId).executeUpdate();
	}

	@Override
	public long countCommentsByCheatSheetId(Long cheatSheetId) {
		return getCurrentSession()
				.createQuery("SELECT COUNT(c) FROM Comment c WHERE c.cheatSheet.id = :csId AND c.deletedAt IS NULL",
						Long.class)
				.setParameter("csId", cheatSheetId).getSingleResult();
	}

	// ===== INTERACTION METHODS =====

	@Override
	public void incrementLikeCount(Long sheetId) {
		getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.likeCount = COALESCE(c.likeCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void decrementLikeCount(Long sheetId) {
		getCurrentSession().createQuery(
				"UPDATE CheatSheet c SET c.likeCount = CASE WHEN c.likeCount > 0 THEN c.likeCount - 1 ELSE 0 END WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void incrementCommentCount(Long sheetId) {
		getCurrentSession()
				.createQuery(
						"UPDATE CheatSheet c SET c.commentCount = COALESCE(c.commentCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void decrementCommentCount(Long sheetId) {
		getCurrentSession().createQuery(
				"UPDATE CheatSheet c SET c.commentCount = CASE WHEN c.commentCount > 0 THEN c.commentCount - 1 ELSE 0 END WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void incrementViewCount(Long sheetId) {
		getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.viewCount = COALESCE(c.viewCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void incrementBookmarkCount(Long sheetId) {
		getCurrentSession()
				.createQuery(
						"UPDATE CheatSheet c SET c.bookmarkCount = COALESCE(c.bookmarkCount, 0) + 1 WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void decrementBookmarkCount(Long sheetId) {
		getCurrentSession().createQuery(
				"UPDATE CheatSheet c SET c.bookmarkCount = CASE WHEN c.bookmarkCount > 0 THEN c.bookmarkCount - 1 ELSE 0 END WHERE c.id = :id")
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void updateRating(Long sheetId, int ratingValue) {
		Double avg = getAverageRating(sheetId);
		long count = countReviewsByCheatSheetId(sheetId);

		String hql = "UPDATE CheatSheet c SET c.ratingSum = :ratingSum, c.ratingCount = :ratingCount WHERE c.id = :id";
		getCurrentSession().createQuery(hql).setParameter("ratingSum", (int) (avg * count))
				.setParameter("ratingCount", (int) count).setParameter("id", sheetId).executeUpdate();
	}

	// ===== SORTING AND FILTERING =====

	@Override
	public List<CheatSheet> findMostLiked() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.likeCount DESC",
				CheatSheet.class).setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findMostLiked(int limit) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.likeCount DESC",
				CheatSheet.class).setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findTopRated() {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.status = 'published' AND c.ratingCount > 0 "
						+ "ORDER BY (c.ratingSum / c.ratingCount) DESC", CheatSheet.class)
				.setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findTopRated(int limit) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.status = 'published' AND c.ratingCount > 0 "
						+ "ORDER BY (c.ratingSum / c.ratingCount) DESC", CheatSheet.class)
				.setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findMostViewed() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.viewCount DESC",
				CheatSheet.class).setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findMostViewed(int limit) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.viewCount DESC",
				CheatSheet.class).setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findMostBookmarked() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.bookmarkCount DESC",
				CheatSheet.class).setMaxResults(10).getResultList();
	}

	@Override
	public List<CheatSheet> findMostBookmarked(int limit) {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.status = 'published' " + "ORDER BY c.bookmarkCount DESC",
				CheatSheet.class).setMaxResults(limit).getResultList();
	}

	@Override
	public List<CheatSheet> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return getCurrentSession()
				.createQuery(
						"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
								+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
								+ "WHERE c.createdAt BETWEEN :startDate AND :endDate " + "ORDER BY c.createdAt DESC",
						CheatSheet.class)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
	}

	@Override
	public List<CheatSheet> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate) {
		return getCurrentSession()
				.createQuery("SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user "
						+ "LEFT JOIN FETCH c.category " + "LEFT JOIN FETCH c.tags "
						+ "WHERE c.user.userId = :userId AND c.createdAt BETWEEN :startDate AND :endDate "
						+ "ORDER BY c.createdAt DESC", CheatSheet.class)
				.setParameter("userId", user.getUserId()).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
	}

	// ===== STATISTICS METHODS =====

	@Override
	public List<Object[]> getStatusDistribution() {
		return getCurrentSession()
				.createQuery("SELECT c.status, COUNT(c) FROM CheatSheet c GROUP BY c.status", Object[].class)
				.getResultList();
	}

	@Override
	public List<Object[]> getCategoryDistribution() {
		return getCurrentSession()
				.createQuery("SELECT c.category.name, COUNT(c) FROM CheatSheet c GROUP BY c.category.name",
						Object[].class)
				.getResultList();
	}

	@Override
	public List<Object[]> getDailyCreationStats(LocalDateTime startDate, LocalDateTime endDate) {
		return getCurrentSession()
				.createQuery(
						"SELECT FUNCTION('DATE', c.createdAt), COUNT(c) FROM CheatSheet c "
								+ "WHERE c.createdAt BETWEEN :startDate AND :endDate "
								+ "GROUP BY FUNCTION('DATE', c.createdAt) " + "ORDER BY FUNCTION('DATE', c.createdAt)",
						Object[].class)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
	}

	@Override
	public List<Object[]> getUserActivityStats() {
		return getCurrentSession().createQuery("SELECT c.user.username, COUNT(c) FROM CheatSheet c "
				+ "GROUP BY c.user.username ORDER BY COUNT(c) DESC", Object[].class).getResultList();
	}

	// ===== BULK OPERATIONS =====

	@Override
	public int bulkUpdateStatus(List<Long> sheetIds, CheatSheet.Status status) {
		return getCurrentSession().createQuery("UPDATE CheatSheet c SET c.status = :status WHERE c.id IN (:ids)")
				.setParameter("status", status).setParameter("ids", sheetIds).executeUpdate();
	}

	@Override
	public int bulkDelete(List<Long> sheetIds) {
		for (Long id : sheetIds) {
			CheatSheet sheet = findById(id);
			if (sheet != null) {
				delete(sheet);
			}
		}
		return sheetIds.size();
	}

	@Override
	public int archiveOldSheets(LocalDateTime cutoffDate) {
		return getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.status = :status "
						+ "WHERE c.createdAt < :cutoffDate AND c.status = 'published'")
				.setParameter("status", CheatSheet.Status.archived).setParameter("cutoffDate", cutoffDate)
				.executeUpdate();
	}

	// ===== SOFT DELETE METHODS =====

	@Override
	public void softDelete(Long sheetId) {
		getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.deletedAt = :deletedAt, c.status = :status WHERE c.id = :id")
				.setParameter("deletedAt", LocalDateTime.now()).setParameter("status", CheatSheet.Status.archived)
				.setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public void restore(Long sheetId) {
		getCurrentSession()
				.createQuery("UPDATE CheatSheet c SET c.deletedAt = null, c.status = :status WHERE c.id = :id")
				.setParameter("status", CheatSheet.Status.published).setParameter("id", sheetId).executeUpdate();
	}

	@Override
	public List<CheatSheet> findSoftDeleted() {
		return getCurrentSession().createQuery(
				"SELECT DISTINCT c FROM CheatSheet c " + "LEFT JOIN FETCH c.user " + "LEFT JOIN FETCH c.category "
						+ "LEFT JOIN FETCH c.tags " + "WHERE c.deletedAt IS NOT NULL " + "ORDER BY c.deletedAt DESC",
				CheatSheet.class).getResultList();
	}

	@Override
	public void permanentDelete(Long sheetId) {
		CheatSheet sheet = findById(sheetId);
		if (sheet != null) {
			delete(sheet);
		}
	}
}