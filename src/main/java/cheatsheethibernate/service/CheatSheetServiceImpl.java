package cheatsheethibernate.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Comment;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.CheatSheetRepository;
import cheatsheethibernate.repository.UserRepository;

@Service
@Transactional
public class CheatSheetServiceImpl implements CheatSheetService {

	@Autowired
	private CheatSheetRepository cheatSheetRepository;

	@Autowired
	private UserRepository userRepository;

	// ===== Basic CRUD Operations =====

	@Override
	public void saveCheatSheet(CheatSheet cheatSheet) {
		cheatSheetRepository.save(cheatSheet);
	}

	@Override
	@Transactional(readOnly = true)
	public CheatSheet getCheatSheetById(Long id) {
		return cheatSheetRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public CheatSheet getCheatSheetDetailsById(Long id) {
		return cheatSheetRepository.findByIdWithDetails(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getAllCheatSheets() {
		return cheatSheetRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getAllPublishedCheatSheets() {
		return cheatSheetRepository.findAllPublished();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getRecentCheatSheets(int limit) {
		return cheatSheetRepository.findRecentSheets(limit);
	}

	@Override
	@Transactional
	public void deleteCheatSheet(Long id) {
		CheatSheet cheatSheet = cheatSheetRepository.findById(id);
		if (cheatSheet != null) {
			cheatSheetRepository.delete(cheatSheet);
		}
	}

	@Override
	@Transactional
	public void deleteCheatSheet(CheatSheet cheatSheet) {
		if (cheatSheet != null) {
			cheatSheetRepository.delete(cheatSheet);
		}
	}

	// ===== Category Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByCategory(String categoryName) {
		return cheatSheetRepository.findByCategoryIgnoreCase(categoryName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByCategoryId(int categoryId) {
		return cheatSheetRepository.findByCategoryId(categoryId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByCategoryAndStatus(String categoryName, CheatSheet.Status status) {
		return cheatSheetRepository.findByCategoryNameAndStatus(categoryName, status);
	}

	// ===== Tag Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByTag(String tagName) {
		return cheatSheetRepository.findByTagName(tagName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByTagId(Integer tagId) {
		return cheatSheetRepository.findByTagId(tagId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByTagAndStatus(String tagName, CheatSheet.Status status) {
		List<CheatSheet> sheets = cheatSheetRepository.findByTagName(tagName);
		if (sheets == null || sheets.isEmpty()) {
			return List.of();
		}
		return sheets.stream().filter(sheet -> sheet.getStatus() == status)
				.collect(java.util.stream.Collectors.toList());
	}

	// ===== User Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByUser(User user) {
		return cheatSheetRepository.findByUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByUserId(Integer userId) {
		return cheatSheetRepository.findByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getCheatSheetsByUserAndStatus(User user, CheatSheet.Status status) {
		return cheatSheetRepository.findByUserAndStatus(user, status);
	}

	@Override
	@Transactional(readOnly = true)
	public long countCheatSheetsByUser(User user) {
		return cheatSheetRepository.countByUser(user);
	}

	// ===== Search Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> searchCheatSheets(String keyword) {
		return cheatSheetRepository.searchByKeyword(keyword);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> searchCheatSheetsByStatus(String keyword, CheatSheet.Status status) {
		return cheatSheetRepository.searchByKeywordAndStatus(keyword, status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> searchCheatSheetsWithPagination(String keyword, int offset, int limit) {
		return cheatSheetRepository.searchByKeywordWithPagination(keyword, offset, limit);
	}

	// ===== Sorting Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getMostLikedCheatSheets(int limit) {
		return cheatSheetRepository.findMostLiked(limit);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getTopRatedCheatSheets(int limit) {
		return cheatSheetRepository.findTopRated(limit);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getMostViewedCheatSheets(int limit) {
		return cheatSheetRepository.findMostViewed(limit);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getMostBookmarkedCheatSheets(int limit) {
		return cheatSheetRepository.findMostBookmarked(limit);
	}

	// ===== User Authentication =====

	@Override
	@Transactional
	public void registerUser(User user) {
		userRepository.saveUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User loginUser(String username, String password) {
		User user = userRepository.findByUsername(username);
		if (user != null && user.getPasswordHash() != null && user.getPasswordHash().equals(password)) {
			return user;
		}
		return null;
	}

	// ===== Review Methods =====

	@Override
	@Transactional
	public void addReview(Long cheatSheetId, User user, Integer rating, String text) {
		CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
		if (cheatSheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + cheatSheetId);
		}
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}

		Review review = new Review();
		review.setCheatSheet(cheatSheet);
		review.setUser(user);
		review.setRating(rating);
		review.setReviewText(text);

		cheatSheetRepository.saveReview(review);
		// Update cheat sheet rating
		cheatSheetRepository.updateRating(cheatSheetId, rating);
	}

	@Override
	@Transactional
	public void addOrUpdateReview(Long cheatSheetId, User user, Integer rating, String text) {
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}

		Review existingReview = cheatSheetRepository.findReviewByUserAndCheatSheet(user.getUserId(), cheatSheetId);

		if (existingReview != null) {
			// Update existing review
			existingReview.setRating(rating);
			existingReview.setReviewText(text);
			cheatSheetRepository.saveReview(existingReview);
		} else {
			// Add new review
			CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
			if (cheatSheet == null) {
				throw new IllegalArgumentException("CheatSheet not found with ID: " + cheatSheetId);
			}
			Review review = new Review();
			review.setCheatSheet(cheatSheet);
			review.setUser(user);
			review.setRating(rating);
			review.setReviewText(text);
			cheatSheetRepository.saveReview(review);
		}
		// Update cheat sheet rating
		cheatSheetRepository.updateRating(cheatSheetId, rating);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Review> getReviewsByCheatSheet(Long cheatSheetId) {
		return cheatSheetRepository.findReviewsByCheatSheetId(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Review> getReviewsByCheatSheetWithPagination(Long cheatSheetId, int offset, int limit) {
		return cheatSheetRepository.findReviewsByCheatSheetIdWithPagination(cheatSheetId, offset, limit);
	}

	@Override
	@Transactional(readOnly = true)
	public Double getAverageRating(Long cheatSheetId) {
		return cheatSheetRepository.getAverageRating(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getRatingDistribution(Long cheatSheetId) {
		return cheatSheetRepository.getRatingDistribution(cheatSheetId);
	}

	@Override
	@Transactional
	public void deleteReview(Integer reviewId, Integer userId) {
		// First, get the review by ID to verify ownership
		Review review = cheatSheetRepository.findReviewByUserAndCheatSheet(userId, null);
		if (review != null && review.getReviewId().equals(reviewId)) {
			// Soft delete the review
			review.setDeletedAt(LocalDateTime.now());
			cheatSheetRepository.saveReview(review);
		} else {
			throw new IllegalArgumentException("Review not found or you don't have permission to delete it");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasUserReviewed(Integer userId, Long cheatSheetId) {
		return cheatSheetRepository.findReviewByUserAndCheatSheet(userId, cheatSheetId) != null;
	}

	// ===== Comment Methods =====

	@Override
	@Transactional
	public void addComment(Long cheatSheetId, User user, String content, Integer parentId) {
		CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);
		if (cheatSheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + cheatSheetId);
		}
		if (content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("Comment content cannot be empty");
		}

		Comment comment = new Comment();
		comment.setCheatSheet(cheatSheet);
		comment.setUser(user);
		comment.setContent(content.trim());

		if (parentId != null) {
			Comment parent = cheatSheetRepository.findCommentById(parentId);
			if (parent != null) {
				comment.setParentComment(parent);
			}
		}

		cheatSheetRepository.saveComment(comment);
		cheatSheetRepository.incrementCommentCount(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> getMainCommentsByCheatSheet(Long cheatSheetId) {
		return cheatSheetRepository.findMainCommentsByCheatSheetId(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> getAllCommentsByCheatSheet(Long cheatSheetId) {
		return cheatSheetRepository.findAllCommentsByCheatSheetId(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> getRepliesByComment(Integer commentId) {
		return cheatSheetRepository.findRepliesByCommentId(commentId);
	}

	@Override
	@Transactional
	public void deleteComment(Integer commentId, Integer userId) {
		Comment comment = cheatSheetRepository.findCommentById(commentId);
		if (comment == null) {
			throw new IllegalArgumentException("Comment not found with ID: " + commentId);
		}
		if (!comment.getUser().getUserId().equals(userId)) {
			throw new IllegalStateException("You don't have permission to delete this comment");
		}
		cheatSheetRepository.deleteComment(commentId);
		cheatSheetRepository.decrementCommentCount(comment.getCheatSheet().getId());
	}

	@Override
	@Transactional(readOnly = true)
	public long countComments(Long cheatSheetId) {
		return cheatSheetRepository.countCommentsByCheatSheetId(cheatSheetId);
	}

	// ===== Ban Methods =====

	@Override
	@Transactional
	public void banSheet(Long sheetId, String reason, LocalDateTime expiresAt, Integer adminId) {
		CheatSheet sheet = cheatSheetRepository.findById(sheetId);
		if (sheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + sheetId);
		}
		if (sheet.isBanned()) {
			throw new IllegalStateException("Sheet is already banned");
		}
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Ban reason cannot be empty");
		}

		cheatSheetRepository.updateBanStatus(sheetId, "banned", reason.trim(), LocalDateTime.now(), expiresAt, adminId);
	}

	@Override
	@Transactional
	public void unbanSheet(Long sheetId) {
		CheatSheet sheet = cheatSheetRepository.findById(sheetId);
		if (sheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + sheetId);
		}
		if (!sheet.isBanned()) {
			throw new IllegalStateException("Sheet is not banned");
		}
		cheatSheetRepository.unbanSheet(sheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getBannedSheets() {
		return cheatSheetRepository.findBannedSheets();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getBannedSheetsByUser(User user) {
		return cheatSheetRepository.findBannedSheetsByUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CheatSheet> getActiveBans() {
		return cheatSheetRepository.findActiveBans();
	}

	@Override
	@Transactional
	public void processExpiredBans() {
		List<CheatSheet> expiredBans = cheatSheetRepository.findExpiredBans();
		for (CheatSheet sheet : expiredBans) {
			cheatSheetRepository.unbanSheet(sheet.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isSheetBanned(Long sheetId) {
		CheatSheet sheet = cheatSheetRepository.findById(sheetId);
		return sheet != null && sheet.isBanned();
	}

	@Override
	@Transactional(readOnly = true)
	public CheatSheet getBanInfo(Long sheetId) {
		CheatSheet sheet = cheatSheetRepository.findById(sheetId);
		return (sheet != null && sheet.isBanned()) ? sheet : null;
	}

	// ===== Interaction Methods =====

	@Override
	@Transactional
	public void incrementLikeCount(Long sheetId) {
		cheatSheetRepository.incrementLikeCount(sheetId);
	}

	@Override
	@Transactional
	public void decrementLikeCount(Long sheetId) {
		cheatSheetRepository.decrementLikeCount(sheetId);
	}

	@Override
	@Transactional
	public void incrementViewCount(Long sheetId) {
		cheatSheetRepository.incrementViewCount(sheetId);
	}

	@Override
	@Transactional
	public void incrementBookmarkCount(Long sheetId) {
		cheatSheetRepository.incrementBookmarkCount(sheetId);
	}

	@Override
	@Transactional
	public void decrementBookmarkCount(Long sheetId) {
		cheatSheetRepository.decrementBookmarkCount(sheetId);
	}

	// ===== Status Management =====

	@Override
	@Transactional
	public void updateStatus(Long sheetId, CheatSheet.Status status) {
		CheatSheet sheet = cheatSheetRepository.findById(sheetId);
		if (sheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + sheetId);
		}
		sheet.setStatus(status);
		cheatSheetRepository.save(sheet);
	}

	@Override
	@Transactional
	public void publishSheet(Long sheetId) {
		updateStatus(sheetId, CheatSheet.Status.published);
	}

	@Override
	@Transactional
	public void archiveSheet(Long sheetId) {
		updateStatus(sheetId, CheatSheet.Status.archived);
	}

	// ===== Statistics Methods =====

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getStatusDistribution() {
		return cheatSheetRepository.getStatusDistribution();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getCategoryDistribution() {
		return cheatSheetRepository.getCategoryDistribution();
	}

	@Override
	@Transactional(readOnly = true)
	public long getTotalSheetCount() {
		return cheatSheetRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long getPublishedSheetCount() {
		return cheatSheetRepository.findByStatus(CheatSheet.Status.published).size();
	}

	@Override
	@Transactional(readOnly = true)
	public long getBannedSheetCount() {
		return cheatSheetRepository.findByStatus(CheatSheet.Status.banned).size();
	}
}