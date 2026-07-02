package cheatsheethibernate.service;

import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for User management. Provides business logic for user CRUD
 * operations, ban management, follow system, and user analytics.
 */
public interface UserService {

	// ===== Basic CRUD Operations =====

	/**
	 * Get all users
	 * 
	 * @return List of all users
	 */
	List<User> getAllUsers();

	/**
	 * Get all users with their details
	 * 
	 * @return List of all users with details
	 */
	List<User> getAllUsersWithDetails();

	/**
	 * Get user by ID
	 * 
	 * @param id The user ID
	 * @return The user, or null if not found
	 */
	User getById(Integer id);

	/**
	 * Get user by ID using Optional
	 * 
	 * @param id The user ID
	 * @return Optional containing the user if found
	 */
	Optional<User> findByIdOptional(Integer id);

	/**
	 * Insert a new user
	 * 
	 * @param obj The user to insert
	 * @return The generated user ID
	 */
	Integer insertUser(User obj);

	/**
	 * Update an existing user
	 * 
	 * @param obj The user with updated values
	 * @return The updated user
	 */
	User updateUser(User obj);

	/**
	 * Delete a user by ID
	 * 
	 * @param id The user ID to delete
	 */
	void deleteUser(Integer id);

	/**
	 * Check if a user exists
	 * 
	 * @param id The user ID
	 * @return true if exists, false otherwise
	 */
	boolean exists(Integer id);

	/**
	 * Get total number of users
	 * 
	 * @return Total user count
	 */
	long getTotalUserCount();

	// ===== User Lookup and Search =====

	/**
	 * Find user by username
	 * 
	 * @param username The username
	 * @return The user, or null if not found
	 */
	User findByUsername(String username);

	/**
	 * Find user by email
	 * 
	 * @param email The email
	 * @return The user, or null if not found
	 */
	User findByEmail(String email);

	/**
	 * Search users by keyword
	 * 
	 * @param keyword The search keyword
	 * @return List of matching users
	 */
	List<User> searchUsers(String keyword);

	/**
	 * Get users by status
	 * 
	 * @param status The status to filter by
	 * @return List of users with the given status
	 */
	List<User> getUsersByStatus(String status);

	/**
	 * Get users by role
	 * 
	 * @param role The role to filter by
	 * @return List of users with the given role
	 */
	List<User> getUsersByRole(String role);

	/**
	 * Get recent users
	 * 
	 * @param limit Maximum number of users to return
	 * @return List of recent users
	 */
	List<User> getRecentUsers(int limit);

	// ===== Password Reset Methods =====

	/**
	 * Clear expired password reset codes
	 * 
	 * @return Number of reset codes cleared
	 */
	int clearExpiredResetCodes();

	/**
	 * Generate and save password reset code
	 * 
	 * @param email The user's email
	 * @return The generated reset code
	 */
	String generateResetCode(String email);

	/**
	 * Validate reset code
	 * 
	 * @param resetCode The reset code to validate
	 * @return The user if valid, null otherwise
	 */
	User validateResetCode(String resetCode);

	/**
	 * Reset password using reset code
	 * 
	 * @param resetCode   The reset code
	 * @param newPassword The new password
	 * @return true if successful, false otherwise
	 */
	boolean resetPassword(String resetCode, String newPassword);

	// ===== User Status Management =====

	/**
	 * Suspend a user account
	 * 
	 * @param userId The user ID
	 * @param reason The reason for suspension
	 */
	void suspendUser(Integer userId, String reason);

	/**
	 * Unsuspend a user account
	 * 
	 * @param userId The user ID
	 */
	void unsuspendUser(Integer userId);

	/**
	 * Get all suspended users
	 * 
	 * @return List of suspended users
	 */
	List<User> getSuspendedUsers();

	/**
	 * Activate a user account
	 * 
	 * @param userId The user ID
	 */
	void activateUser(Integer userId);

	/**
	 * Deactivate a user account
	 * 
	 * @param userId The user ID
	 * @param reason The reason for deactivation
	 */
	void deactivateUser(Integer userId, String reason);

	// ===== Ban Management =====

	/**
	 * Get all banned users
	 * 
	 * @return List of banned users
	 */
	List<BannedUser> getAllBannedUsers();

	/**
	 * Get active bans
	 * 
	 * @return List of active bans
	 */
	List<BannedUser> getActiveBans();

	/**
	 * Get expired bans
	 * 
	 * @return List of expired bans
	 */
	List<BannedUser> getExpiredBans();

	/**
	 * Ban a user
	 * 
	 * @param userId        The user ID to ban
	 * @param reason        The reason for banning
	 * @param currentStatus The current status of the user
	 * @param adminId       The ID of the admin performing the ban
	 */
	void banUser(Integer userId, String reason, String currentStatus, Integer adminId);

	/**
	 * Ban a user with duration
	 * 
	 * @param userId       The user ID to ban
	 * @param reason       The reason for banning
	 * @param durationDays Number of days to ban (null for permanent)
	 * @param adminId      The ID of the admin performing the ban
	 */
	void banUserWithDuration(Integer userId, String reason, Integer durationDays, Integer adminId);

	/**
	 * Unban a user
	 * 
	 * @param userId The user ID to unban
	 */
	void unbanUser(Integer userId);

	/**
	 * Check if a user is banned
	 * 
	 * @param userId The user ID
	 * @return true if banned, false otherwise
	 */
	boolean isUserBanned(Integer userId);

	/**
	 * Get ban information for a user
	 * 
	 * @param userId The user ID
	 * @return The BannedUser object, or null if not banned
	 */
	BannedUser getBanInfo(Integer userId);

	// ===== Follow/Unfollow System =====

	/**
	 * Follow a user
	 * 
	 * @param followerId  The ID of the user who wants to follow
	 * @param followingId The ID of the user to follow
	 * @throws IllegalArgumentException if trying to follow self or invalid users
	 * @throws IllegalStateException    if already following
	 */
	void followUser(Integer followerId, Integer followingId);

	/**
	 * Unfollow a user
	 * 
	 * @param followerId  The ID of the user who wants to unfollow
	 * @param followingId The ID of the user to unfollow
	 */
	void unfollowUser(Integer followerId, Integer followingId);

	/**
	 * Check if a user is following another user
	 * 
	 * @param followerId  The ID of the follower
	 * @param followingId The ID of the user being followed
	 * @return true if following, false otherwise
	 */
	boolean isFollowing(Integer followerId, Integer followingId);

	/**
	 * Get follower count for a user
	 * 
	 * @param userId The user ID
	 * @return Number of followers
	 */
	long countFollowers(Integer userId);

	/**
	 * Get following count for a user
	 * 
	 * @param userId The user ID
	 * @return Number of users this user is following
	 */
	long countFollowing(Integer userId);

	/**
	 * Get list of followers for a user
	 * 
	 * @param userId The user ID
	 * @return List of users following this user
	 */
	List<User> getFollowers(Integer userId);

	/**
	 * Get list of users this user is following
	 * 
	 * @param userId The user ID
	 * @return List of users this user follows
	 */
	List<User> getFollowing(Integer userId);

	/**
	 * Check if user has any followers
	 * 
	 * @param userId The user ID
	 * @return true if has followers, false otherwise
	 */
	boolean hasFollowers(Integer userId);

	/**
	 * Check if user is following anyone
	 * 
	 * @param userId The user ID
	 * @return true if following anyone, false otherwise
	 */
	boolean hasFollowing(Integer userId);

	// ===== Statistics and Analytics =====

	/**
	 * Get user registration statistics by date range
	 * 
	 * @param startDate The start date
	 * @param endDate   The end date
	 * @return Map with date as key and registration count as value
	 */
	Map<LocalDateTime, Long> getUserRegistrationStats(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Get user role distribution
	 * 
	 * @return Map with role as key and count as value
	 */
	Map<String, Long> getUserRoleDistribution();

	/**
	 * Get user status distribution
	 * 
	 * @return Map with status as key and count as value
	 */
	Map<String, Long> getUserStatusDistribution();

	/**
	 * Get active users count
	 * 
	 * @return Number of active users
	 */
	long getActiveUserCount();

	/**
	 * Get inactive users count
	 * 
	 * @return Number of inactive users
	 */
	long getInactiveUserCount();

	/**
	 * Get suspended users count
	 * 
	 * @return Number of suspended users
	 */
	long getSuspendedUserCount();

	/**
	 * Get most followed users
	 * 
	 * @param limit Maximum number to return
	 * @return List of Object arrays [userId, followerCount]
	 */
	List<Object[]> getMostFollowedUsers(int limit);

	/**
	 * Get most active users (most reports submitted)
	 * 
	 * @param limit Maximum number to return
	 * @return List of Object arrays [userId, reportCount]
	 */
	List<Object[]> getMostActiveUsers(int limit);

	// ===== Validation Methods =====

	/**
	 * Check if username is taken
	 * 
	 * @param username The username to check
	 * @return true if taken, false otherwise
	 */
	boolean isUsernameTaken(String username);

	/**
	 * Check if email is taken
	 * 
	 * @param email The email to check
	 * @return true if taken, false otherwise
	 */
	boolean isEmailTaken(String email);

	/**
	 * Check if phone number is taken
	 * 
	 * @param phone The phone number to check
	 * @return true if taken, false otherwise
	 */
	boolean isPhoneTaken(String phone);

	/**
	 * Validate user credentials
	 * 
	 * @param username The username
	 * @param password The password
	 * @return The user if credentials are valid, null otherwise
	 */
	User validateCredentials(String username, String password);

	// ===== Bulk Operations =====

	/**
	 * Bulk update user status
	 * 
	 * @param userIds List of user IDs
	 * @param status  The new status
	 * @param adminId The ID of the admin performing the action
	 * @return Number of users updated
	 */
	int bulkUpdateStatus(List<Integer> userIds, String status, Integer adminId);

	/**
	 * Delete inactive users
	 * 
	 * @param daysInactive Number of days of inactivity
	 * @param adminId      The ID of the admin performing the action
	 * @return Number of users deleted
	 */
	int deleteInactiveUsers(int daysInactive, Integer adminId);

	/**
	 * Send notification to all users (admin only)
	 * 
	 * @param subject The notification subject
	 * @param message The notification message
	 * @param adminId The ID of the admin sending the notification
	 */
	void sendNotificationToAllUsers(String subject, String message, Integer adminId);
}