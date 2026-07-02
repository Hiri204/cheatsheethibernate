package cheatsheethibernate.service;

import java.time.LocalDateTime;
import java.util.List;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;

/**
 * Service interface for CheatSheet management. Provides business logic for
 * cheat sheet CRUD operations, user authentication, review and comment
 * management, and ban management.
 */
public interface CheatSheetService {

	// ===== Basic CRUD Operations =====

	/**
	 * Save a cheat sheet (insert or update)
	 * 
	 * @param cheatSheet The cheat sheet to save
	 */
	void saveCheatSheet(CheatSheet cheatSheet);

	/**
	 * Get a cheat sheet by its ID
	 * 
	 * @param id The cheat sheet ID
	 * @return The cheat sheet, or null if not found
	 */
	CheatSheet getCheatSheetById(Long id);

	/**
	 * Get a cheat sheet with all details (eager loading)
	 * 
	 * @param id The cheat sheet ID
	 * @return The cheat sheet with details
	 */
	CheatSheet getCheatSheetDetailsById(Long id);

	/**
	 * Get all cheat sheets
	 * 
	 * @return List of all cheat sheets
	 */
	List<CheatSheet> getAllCheatSheets();

	/**
	 * Get all published cheat sheets
	 * 
	 * @return List of published cheat sheets
	 */
	List<CheatSheet> getAllPublishedCheatSheets();

	/**
	 * Get recent cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of recent cheat sheets
	 */
	List<CheatSheet> getRecentCheatSheets(int limit);

	/**
	 * Delete a cheat sheet
	 * 
	 * @param id The cheat sheet ID to delete
	 */
	void deleteCheatSheet(Long id);

	/**
	 * Delete a cheat sheet
	 * 
	 * @param cheatSheet The cheat sheet to delete
	 */
	void deleteCheatSheet(CheatSheet cheatSheet);

	// ===== Category Methods =====

	/**
	 * Get cheat sheets by category name
	 * 
	 * @param categoryName The category name
	 * @return List of cheat sheets in the category
	 */
	List<CheatSheet> getCheatSheetsByCategory(String categoryName);

	/**
	 * Get cheat sheets by category ID
	 * 
	 * @param categoryId The category ID
	 * @return List of cheat sheets in the category
	 */
	List<CheatSheet> getCheatSheetsByCategoryId(int categoryId);

	/**
	 * Get cheat sheets by category and status
	 * 
	 * @param categoryName The category name
	 * @param status       The status to filter by
	 * @return List of cheat sheets matching the criteria
	 */
	List<CheatSheet> getCheatSheetsByCategoryAndStatus(String categoryName, CheatSheet.Status status);

	// ===== Tag Methods =====

	/**
	 * Get cheat sheets by tag name
	 * 
	 * @param tagName The tag name
	 * @return List of cheat sheets with the tag
	 */
	List<CheatSheet> getCheatSheetsByTag(String tagName);

	/**
	 * Get cheat sheets by tag ID
	 * 
	 * @param tagId The tag ID
	 * @return List of cheat sheets with the tag
	 */
	List<CheatSheet> getCheatSheetsByTagId(Integer tagId);

	/**
	 * Get cheat sheets by tag and status
	 * 
	 * @param tagName The tag name
	 * @param status  The status to filter by
	 * @return List of cheat sheets matching the criteria
	 */
	List<CheatSheet> getCheatSheetsByTagAndStatus(String tagName, CheatSheet.Status status);

	// ===== User Methods =====

	/**
	 * Get cheat sheets by user
	 * 
	 * @param user The user
	 * @return List of cheat sheets by the user
	 */
	List<CheatSheet> getCheatSheetsByUser(User user);

	/**
	 * Get cheat sheets by user ID
	 * 
	 * @param userId The user ID
	 * @return List of cheat sheets by the user
	 */
	List<CheatSheet> getCheatSheetsByUserId(Integer userId);

	/**
	 * Get cheat sheets by user and status
	 * 
	 * @param user   The user
	 * @param status The status to filter by
	 * @return List of cheat sheets matching the criteria
	 */
	List<CheatSheet> getCheatSheetsByUserAndStatus(User user, CheatSheet.Status status);

	/**
	 * Count cheat sheets by user
	 * 
	 * @param user The user
	 * @return Number of cheat sheets by the user
	 */
	long countCheatSheetsByUser(User user);

	// ===== Search Methods =====

	/**
	 * Search cheat sheets by keyword
	 * 
	 * @param keyword The search keyword
	 * @return List of cheat sheets matching the keyword
	 */
	List<CheatSheet> searchCheatSheets(String keyword);

	/**
	 * Search cheat sheets by keyword with status filter
	 * 
	 * @param keyword The search keyword
	 * @param status  The status to filter by
	 * @return List of cheat sheets matching the criteria
	 */
	List<CheatSheet> searchCheatSheetsByStatus(String keyword, CheatSheet.Status status);

	/**
	 * Search cheat sheets with pagination
	 * 
	 * @param keyword The search keyword
	 * @param offset  The starting position
	 * @param limit   The maximum number of results
	 * @return List of cheat sheets matching the keyword
	 */
	List<CheatSheet> searchCheatSheetsWithPagination(String keyword, int offset, int limit);

	// ===== Sorting Methods =====

	/**
	 * Get most liked cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of most liked cheat sheets
	 */
	List<CheatSheet> getMostLikedCheatSheets(int limit);

	/**
	 * Get top rated cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of top rated cheat sheets
	 */
	List<CheatSheet> getTopRatedCheatSheets(int limit);

	/**
	 * Get most viewed cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of most viewed cheat sheets
	 */
	List<CheatSheet> getMostViewedCheatSheets(int limit);

	/**
	 * Get most bookmarked cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of most bookmarked cheat sheets
	 */
	List<CheatSheet> getMostBookmarkedCheatSheets(int limit);

	// ===== User Authentication =====

	/**
	 * Register a new user
	 * 
	 * @param user The user to register
	 */
	void registerUser(User user);

	/**
	 * Login a user
	 * 
	 * @param username The username
	 * @param password The password
	 * @return The logged in user, or null if authentication fails
	 */
	User loginUser(String username, String password);

	// ===== Review Methods =====

	/**
	 * Add a review for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param user         The user adding the review
	 * @param rating       The rating (1-5)
	 * @param text         The review text
	 */
	void addReview(Long cheatSheetId, User user, Integer rating, String text);

	/**
	 * Add or update a review for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param user         The user adding/updating the review
	 * @param rating       The rating (1-5)
	 * @param text         The review text
	 */
	void addOrUpdateReview(Long cheatSheetId, User user, Integer rating, String text);

	/**
	 * Get all reviews for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of reviews
	 */
	List<Review> getReviewsByCheatSheet(Long cheatSheetId);

	/**
	 * Get reviews for a cheat sheet with pagination
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param offset       The starting position
	 * @param limit        The maximum number of results
	 * @return List of reviews
	 */
	List<Review> getReviewsByCheatSheetWithPagination(Long cheatSheetId, int offset, int limit);

	/**
	 * Get average rating for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return The average rating
	 */
	Double getAverageRating(Long cheatSheetId);

	/**
	 * Get rating distribution for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of Object arrays [rating, count]
	 */
	List<Object[]> getRatingDistribution(Long cheatSheetId);

	/**
	 * Delete a review
	 * 
	 * @param reviewId The review ID
	 * @param userId   The user ID deleting the review
	 */
	void deleteReview(Integer reviewId, Integer userId);

	/**
	 * Check if a user has reviewed a cheat sheet
	 * 
	 * @param userId       The user ID
	 * @param cheatSheetId The cheat sheet ID
	 * @return true if reviewed, false otherwise
	 */
	boolean hasUserReviewed(Integer userId, Long cheatSheetId);

	// ===== Comment Methods =====

	/**
	 * Add a comment to a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param user         The user adding the comment
	 * @param content      The comment content
	 * @param parentId     The parent comment ID (null for main comment)
	 */
	void addComment(Long cheatSheetId, User user, String content, Integer parentId);

	/**
	 * Get main comments for a cheat sheet (parent comments only)
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of main comments
	 */
	List<Comment> getMainCommentsByCheatSheet(Long cheatSheetId);

	/**
	 * Get all comments for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of all comments
	 */
	List<Comment> getAllCommentsByCheatSheet(Long cheatSheetId);

	/**
	 * Get replies to a comment
	 * 
	 * @param commentId The comment ID
	 * @return List of replies
	 */
	List<Comment> getRepliesByComment(Integer commentId);

	/**
	 * Delete a comment
	 * 
	 * @param commentId The comment ID
	 * @param userId    The user ID deleting the comment
	 */
	void deleteComment(Integer commentId, Integer userId);

	/**
	 * Count comments for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return Number of comments
	 */
	long countComments(Long cheatSheetId);

	// ===== Ban Methods =====

	/**
	 * Ban a cheat sheet
	 * 
	 * @param sheetId   The cheat sheet ID
	 * @param reason    The reason for banning
	 * @param expiresAt The expiry date (null for permanent)
	 * @param adminId   The ID of the admin banning the sheet
	 * @throws IllegalArgumentException if sheet is not found
	 * @throws IllegalStateException    if sheet is already banned
	 */
	void banSheet(Long sheetId, String reason, LocalDateTime expiresAt, Integer adminId);

	/**
	 * Unban a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 * @throws IllegalArgumentException if sheet is not found
	 * @throws IllegalStateException    if sheet is not banned
	 */
	void unbanSheet(Long sheetId);

	/**
	 * Get all banned cheat sheets
	 * 
	 * @return List of banned cheat sheets
	 */
	List<CheatSheet> getBannedSheets();

	/**
	 * Get banned cheat sheets by user
	 * 
	 * @param user The user
	 * @return List of banned cheat sheets by the user
	 */
	List<CheatSheet> getBannedSheetsByUser(User user);

	/**
	 * Get active bans (not expired)
	 * 
	 * @return List of active bans
	 */
	List<CheatSheet> getActiveBans();

	/**
	 * Process expired bans - automatically unban sheets whose ban has expired
	 */
	void processExpiredBans();

	/**
	 * Check if a cheat sheet is banned
	 * 
	 * @param sheetId The cheat sheet ID
	 * @return true if banned, false otherwise
	 */
	boolean isSheetBanned(Long sheetId);

	/**
	 * Get ban information for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 * @return The banned cheat sheet, or null if not banned
	 */
	CheatSheet getBanInfo(Long sheetId);

	// ===== Interaction Methods =====

	/**
	 * Increment like count for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void incrementLikeCount(Long sheetId);

	/**
	 * Decrement like count for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void decrementLikeCount(Long sheetId);

	/**
	 * Increment view count for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void incrementViewCount(Long sheetId);

	/**
	 * Increment bookmark count for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void incrementBookmarkCount(Long sheetId);

	/**
	 * Decrement bookmark count for a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void decrementBookmarkCount(Long sheetId);

	// ===== Status Management =====

	/**
	 * Update cheat sheet status
	 * 
	 * @param sheetId The cheat sheet ID
	 * @param status  The new status
	 * @throws IllegalArgumentException if sheet is not found
	 */
	void updateStatus(Long sheetId, CheatSheet.Status status);

	/**
	 * Publish a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void publishSheet(Long sheetId);

	/**
	 * Archive a cheat sheet
	 * 
	 * @param sheetId The cheat sheet ID
	 */
	void archiveSheet(Long sheetId);

	// ===== Statistics Methods =====

	/**
	 * Get status distribution
	 * 
	 * @return List of Object arrays [status, count]
	 */
	List<Object[]> getStatusDistribution();

	/**
	 * Get category distribution
	 * 
	 * @return List of Object arrays [category, count]
	 */
	List<Object[]> getCategoryDistribution();

	/**
	 * Get total sheet count
	 * 
	 * @return Total number of cheat sheets
	 */
	long getTotalSheetCount();

	/**
	 * Get published sheet count
	 * 
	 * @return Number of published cheat sheets
	 */
	long getPublishedSheetCount();

	/**
	 * Get banned sheet count
	 * 
	 * @return Number of banned cheat sheets
	 */
	long getBannedSheetCount();
}