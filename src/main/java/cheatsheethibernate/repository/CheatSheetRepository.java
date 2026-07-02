package cheatsheethibernate.repository;

import java.time.LocalDateTime;
import java.util.List;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;

public interface CheatSheetRepository {

	// ===== Basic CRUD Operations =====

	/**
	 * Find a cheat sheet by its ID
	 */
	CheatSheet findById(Long id);

	/**
	 * Find a cheat sheet by its ID with all details (eager loading)
	 */
	CheatSheet findByIdWithDetails(Long id);

	/**
	 * Get all cheat sheets
	 */
	List<CheatSheet> findAll();

	/**
	 * Save a cheat sheet (insert or update)
	 */
	void save(CheatSheet cheatSheet);

	/**
	 * Delete a cheat sheet
	 */
	void delete(CheatSheet cheatSheet);

	/**
	 * Check if a cheat sheet exists
	 */
	boolean exists(Long id);

	/**
	 * Get total count of cheat sheets
	 */
	long count();

	/**
	 * Get count by user
	 */
	long countByUser(User user);

	// ===== Category Methods =====

	/**
	 * Find cheat sheets by category ID
	 */
	List<CheatSheet> findByCategoryId(int categoryId);

	/**
	 * Find cheat sheets by category name (case insensitive)
	 */
	List<CheatSheet> findByCategoryIgnoreCase(String category);

	/**
	 * Find cheat sheets by category ID and status
	 */
	List<CheatSheet> findByCategoryIdAndStatus(int categoryId, CheatSheet.Status status);

	/**
	 * Find cheat sheets by category name and status
	 */
	List<CheatSheet> findByCategoryNameAndStatus(String categoryName, CheatSheet.Status status);

	// ===== User Methods =====

	/**
	 * Find cheat sheets by user
	 */
	List<CheatSheet> findByUser(User user);

	/**
	 * Find cheat sheets by user ID
	 */
	List<CheatSheet> findByUserId(Integer userId);

	/**
	 * Find cheat sheets by user and status
	 */
	List<CheatSheet> findByUserAndStatus(User user, CheatSheet.Status status);

	/**
	 * Find cheat sheets by user ID and status
	 */
	List<CheatSheet> findByUserIdAndStatus(Integer userId, CheatSheet.Status status);

	/**
	 * Save a user
	 */
	void saveUser(User user);

	/**
	 * Find user by username
	 */
	User findUserByUsername(String username);

	// ===== Status Methods =====

	/**
	 * Find cheat sheets by status
	 */
	List<CheatSheet> findByStatus(CheatSheet.Status status);

	/**
	 * Find all published cheat sheets
	 */
	List<CheatSheet> findAllPublished();

	/**
	 * Find all published cheat sheets with a limit
	 */
	List<CheatSheet> findAllPublished(int limit);

	/**
	 * Find recent cheat sheets (ordered by creation date)
	 */
	List<CheatSheet> findRecentSheets();

	/**
	 * Find recent cheat sheets with a limit
	 */
	List<CheatSheet> findRecentSheets(int limit);

	/**
	 * Find cheat sheets by multiple statuses
	 */
	List<CheatSheet> findByStatusIn(List<CheatSheet.Status> statuses);

	// ===== Tag Methods =====

	/**
	 * Find cheat sheets by tag ID
	 */
	List<CheatSheet> findByTagId(Integer tagId);

	/**
	 * Find cheat sheets by tag name
	 */
	List<CheatSheet> findByTagName(String tagName);

	/**
	 * Find cheat sheets by tag ID and status
	 */
	List<CheatSheet> findByTagIdAndStatus(Integer tagId, CheatSheet.Status status);

	// ===== Search Methods =====

	/**
	 * Search cheat sheets by keyword (in title and content)
	 */
	List<CheatSheet> searchByKeyword(String keyword);

	/**
	 * Search cheat sheets by keyword with status filter
	 */
	List<CheatSheet> searchByKeywordAndStatus(String keyword, CheatSheet.Status status);

	/**
	 * Search cheat sheets by keyword with pagination
	 */
	List<CheatSheet> searchByKeywordWithPagination(String keyword, int offset, int limit);

	// ===== Ban Methods =====

	/**
	 * Find all banned cheat sheets
	 */
	List<CheatSheet> findBannedSheets();

	/**
	 * Find banned cheat sheets by user
	 */
	List<CheatSheet> findBannedSheetsByUser(User user);

	/**
	 * Find banned cheat sheets by user ID
	 */
	List<CheatSheet> findBannedSheetsByUserId(Integer userId);

	/**
	 * Find all banned cheat sheets with details
	 */
	List<CheatSheet> findBannedSheetsWithDetails();

	/**
	 * Update ban status for a cheat sheet
	 */
	void updateBanStatus(Long sheetId, String status, String reason, LocalDateTime bannedAt, LocalDateTime expiresAt,
			Integer bannedBy);

	/**
	 * Unban a cheat sheet
	 */
	void unbanSheet(Long sheetId);

	/**
	 * Find expired bans (banned sheets where ban has expired)
	 */
	List<CheatSheet> findExpiredBans();

	/**
	 * Find bans expiring soon (within the next 24 hours)
	 */
	List<CheatSheet> findBansExpiringSoon();

	/**
	 * Get active bans
	 */
	List<CheatSheet> findActiveBans();

	// ===== Review Methods =====

	/**
	 * Save a review
	 */
	void saveReview(Review review);

	/**
	 * Find reviews by cheat sheet ID
	 */
	List<Review> findReviewsByCheatSheetId(Long cheatSheetId);

	/**
	 * Find reviews by cheat sheet ID with pagination
	 */
	List<Review> findReviewsByCheatSheetIdWithPagination(Long cheatSheetId, int offset, int limit);

	/**
	 * Find a review by user and cheat sheet
	 */
	Review findReviewByUserAndCheatSheet(Integer userId, Long cheatSheetId);

	/**
	 * Get average rating for a cheat sheet
	 */
	Double getAverageRating(Long cheatSheetId);

	/**
	 * Get rating distribution for a cheat sheet
	 */
	List<Object[]> getRatingDistribution(Long cheatSheetId);

	/**
	 * Delete a review
	 */
	void deleteReview(Integer reviewId);

	/**
	 * Get review count for a cheat sheet
	 */
	long countReviewsByCheatSheetId(Long cheatSheetId);

	// ===== Comment Methods =====

	/**
	 * Save a comment
	 */
	void saveComment(Comment comment);

	/**
	 * Find main comments by cheat sheet ID (parent comments only)
	 */
	List<Comment> findMainCommentsByCheatSheetId(Long cheatSheetId);

	/**
	 * Find all comments by cheat sheet ID
	 */
	List<Comment> findAllCommentsByCheatSheetId(Long cheatSheetId);

	/**
	 * Find comment by ID
	 */
	Comment findCommentById(Integer commentId);

	/**
	 * Find replies to a comment
	 */
	List<Comment> findRepliesByCommentId(Integer commentId);

	/**
	 * Delete a comment
	 */
	void deleteComment(Integer commentId);

	/**
	 * Get comment count for a cheat sheet
	 */
	long countCommentsByCheatSheetId(Long cheatSheetId);

	// ===== Interaction Methods =====

	/**
	 * Increment like count
	 */
	void incrementLikeCount(Long sheetId);

	/**
	 * Decrement like count
	 */
	void decrementLikeCount(Long sheetId);

	/**
	 * Increment comment count
	 */
	void incrementCommentCount(Long sheetId);

	/**
	 * Decrement comment count
	 */
	void decrementCommentCount(Long sheetId);

	/**
	 * Increment view count
	 */
	void incrementViewCount(Long sheetId);

	/**
	 * Increment bookmark count
	 */
	void incrementBookmarkCount(Long sheetId);

	/**
	 * Decrement bookmark count
	 */
	void decrementBookmarkCount(Long sheetId);

	/**
	 * Update rating for a cheat sheet
	 */
	void updateRating(Long sheetId, int ratingValue);

	// ===== Sorting and Filtering Methods =====

	/**
	 * Find most liked cheat sheets
	 */
	List<CheatSheet> findMostLiked();

	/**
	 * Find most liked cheat sheets with limit
	 */
	List<CheatSheet> findMostLiked(int limit);

	/**
	 * Find top rated cheat sheets
	 */
	List<CheatSheet> findTopRated();

	/**
	 * Find top rated cheat sheets with limit
	 */
	List<CheatSheet> findTopRated(int limit);

	/**
	 * Find most viewed cheat sheets
	 */
	List<CheatSheet> findMostViewed();

	/**
	 * Find most viewed cheat sheets with limit
	 */
	List<CheatSheet> findMostViewed(int limit);

	/**
	 * Find most bookmarked cheat sheets
	 */
	List<CheatSheet> findMostBookmarked();

	/**
	 * Find most bookmarked cheat sheets with limit
	 */
	List<CheatSheet> findMostBookmarked(int limit);

	/**
	 * Find cheat sheets by date range
	 */
	List<CheatSheet> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Find cheat sheets by user and date range
	 */
	List<CheatSheet> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

	// ===== Statistics Methods =====

	/**
	 * Get status distribution
	 */
	List<Object[]> getStatusDistribution();

	/**
	 * Get category distribution
	 */
	List<Object[]> getCategoryDistribution();

	/**
	 * Get daily creation statistics
	 */
	List<Object[]> getDailyCreationStats(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Get user activity statistics
	 */
	List<Object[]> getUserActivityStats();

	// ===== Bulk Operations =====

	/**
	 * Bulk update status
	 */
	int bulkUpdateStatus(List<Long> sheetIds, CheatSheet.Status status);

	/**
	 * Bulk delete cheat sheets
	 */
	int bulkDelete(List<Long> sheetIds);

	/**
	 * Archive old cheat sheets
	 */
	int archiveOldSheets(LocalDateTime cutoffDate);

	// ===== Soft Delete Methods =====

	/**
	 * Soft delete a cheat sheet
	 */
	void softDelete(Long sheetId);

	/**
	 * Restore a soft-deleted cheat sheet
	 */
	void restore(Long sheetId);

	/**
	 * Find all soft-deleted cheat sheets
	 */
	List<CheatSheet> findSoftDeleted();

	/**
	 * Permanently delete a soft-deleted cheat sheet
	 */
	void permanentDelete(Long sheetId);
}