package cheatsheethibernate.repository;

import cheatsheethibernate.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

	// ===== Basic CRUD Operations =====
	List<User> getAllUsers();

	void saveUser(User user);

	User getById(Integer id);

	Optional<User> findByIdOptional(Integer id);

	void updateUser(User user);

	void deleteUser(Integer id);

	boolean exists(Integer id);

	long count();

	// ===== Search and Find Methods =====
	User findByUsername(String username);

	User findByEmail(String email);

	List<User> searchUsers(String keyword);

	List<User> findUsersByStatus(String status);

	long countByStatus(String status);

	List<User> findUsersByRole(String role);

	List<User> getRecentUsers(int limit);

	// ===== Password Reset Methods =====
	int clearExpiredResetCodes();

	void saveResetCode(String email, String resetCode, LocalDateTime expiryDate);

	User findByResetCode(String resetCode);

	// ===== User Status Management =====
	void suspendUser(Integer userId, String reason);

	void unsuspendUser(Integer userId);

	List<User> getSuspendedUsers();

	// ===== Follow/Unfollow Methods =====
	void follow(Long followerId, Long followingId);

	void unfollow(Long followerId, Long followingId);

	boolean isFollowing(Long followerId, Long followingId);

	long countFollowers(Long userId);

	long countFollowing(Long userId);

	List<User> getFollowers(Long userId);

	List<User> getFollowing(Long userId);

	boolean hasFollowers(Long userId);

	boolean hasFollowing(Long userId);

	// ===== Statistics Methods =====
	List<Object[]> getUserRegistrationStats(LocalDateTime startDate, LocalDateTime endDate);

	List<Object[]> getUserRoleDistribution();

	// ===== Bulk Operations =====
	int bulkUpdateStatus(List<Integer> userIds, String status);

	int deleteInactiveUsers(LocalDateTime cutoffDate);

	// ===== Validation Methods =====
	boolean isUsernameTaken(String username);

	boolean isEmailTaken(String email);

	boolean isPhoneTaken(String phone);
}