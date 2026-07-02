package cheatsheethibernate.repository;

import java.util.List;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;

public interface CheatSheetRepository {

	// Basic CRUD
	CheatSheet findById(Long id);

	List<CheatSheet> findAll();

	void save(CheatSheet cheatSheet);

	void delete(CheatSheet cheatSheet);

	// Category methods
	List<CheatSheet> findByCategoryId(int categoryId);

	List<CheatSheet> findByCategoryIgnoreCase(String category);

	// User methods
	List<CheatSheet> findByUser(User user);

	void saveUser(User user);

	User findUserByUsername(String username);

	// NEW: Find by user and status
	List<CheatSheet> findByUserAndStatus(User user, CheatSheet.Status status);

	// NEW: Count by user
	long countByUser(User user);

	// NEW: Find by status
	List<CheatSheet> findByStatus(CheatSheet.Status status);

	// NEW: Find by category and status
	List<CheatSheet> findByCategoryIdAndStatus(int categoryId, CheatSheet.Status status);

	// NEW: Find by tag
	List<CheatSheet> findByTagId(Integer tagId);

	List<CheatSheet> findByTagName(String tagName);

	// NEW: Search
	List<CheatSheet> searchByKeyword(String keyword);
	
	CheatSheet findByIdWithDetails(Long id);
    void saveReview(Review review);
    List<Review> findReviewsByCheatSheetId(Long cheatSheetId);
    Double getAverageRating(Long cheatSheetId);
    Review findReviewByUserAndCheatSheet(Integer userId, Long cheatSheetId);
    
    void saveComment(Comment comment);
    List<Comment> findMainCommentsByCheatSheetId(Long cheatSheetId);
    Comment findCommentById(Integer commentId);
    
    
	// NEW: Special queries
	List<CheatSheet> findAllPublished();

	List<CheatSheet> findRecentSheets();

	List<CheatSheet> findMostLiked();

	List<CheatSheet> findTopRated();

	// NEW: Interaction methods
	void incrementLikeCount(Long sheetId);

	void decrementLikeCount(Long sheetId);

	void incrementCommentCount(Long sheetId);

	void decrementCommentCount(Long sheetId);

	void incrementViewCount(Long sheetId);

	void updateRating(Long sheetId, int ratingValue);
	
	
}