package cheatsheethibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.BannedUserRepository;
import cheatsheethibernate.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BannedUserRepository bannedUserRepository;

	// ===== Basic CRUD Operations =====

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsersWithDetails() {
		return userRepository.getAllUsers(); // Repository can be enhanced to load details
	}

	@Override
	@Transactional(readOnly = true)
	public User getById(Integer id) {
		if (id == null) {
			return null;
		}
		return userRepository.getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByIdOptional(Integer id) {
		return Optional.ofNullable(getById(id));
	}

	@Override
	@Transactional
	public Integer insertUser(User obj) {
		if (obj == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		userRepository.saveUser(obj);
		return obj.getUserId();
	}

	@Override
	@Transactional
	public User updateUser(User obj) {
		if (obj == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		userRepository.updateUser(obj);
		return obj;
	}

	@Override
	@Transactional
	public void deleteUser(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		userRepository.deleteUser(id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean exists(Integer id) {
		if (id == null) {
			return false;
		}
		return userRepository.exists(id);
	}

	@Override
	@Transactional(readOnly = true)
	public long getTotalUserCount() {
		return userRepository.count();
	}

	// ===== User Lookup and Search =====

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			return null;
		}
		return userRepository.findByUsername(username.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return null;
		}
		return userRepository.findByEmail(email.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> searchUsers(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return userRepository.searchUsers(keyword.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getUsersByStatus(String status) {
		if (status == null || status.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return userRepository.findUsersByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getUsersByRole(String role) {
		if (role == null || role.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return userRepository.findUsersByRole(role);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getRecentUsers(int limit) {
		if (limit <= 0) {
			return new ArrayList<>();
		}
		return userRepository.getRecentUsers(limit);
	}

	// ===== Password Reset Methods =====

	@Override
	@Transactional
	public int clearExpiredResetCodes() {
		return userRepository.clearExpiredResetCodes();
	}

	@Override
	@Transactional
	public String generateResetCode(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}

		User user = findByEmail(email.trim());
		if (user == null) {
			throw new IllegalArgumentException("User not found with email: " + email);
		}

		// Generate a random 6-digit code
		String resetCode = String.format("%06d", new Random().nextInt(999999));
		LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);

		user.setResetCode(resetCode);
		user.setResetCodeExpiry(expiryDate);
		updateUser(user);

		return resetCode;
	}

	@Override
	@Transactional(readOnly = true)
	public User validateResetCode(String resetCode) {
		if (resetCode == null || resetCode.trim().isEmpty()) {
			return null;
		}
		return userRepository.findByResetCode(resetCode.trim());
	}

	@Override
	@Transactional
	public boolean resetPassword(String resetCode, String newPassword) {
		if (resetCode == null || resetCode.trim().isEmpty()) {
			throw new IllegalArgumentException("Reset code cannot be empty");
		}
		if (newPassword == null || newPassword.trim().isEmpty()) {
			throw new IllegalArgumentException("New password cannot be empty");
		}
		if (newPassword.length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters");
		}

		User user = validateResetCode(resetCode);
		if (user == null) {
			return false;
		}

		user.setPasswordHash(newPassword); // In real app, hash the password
		user.setResetCode(null);
		user.setResetCodeExpiry(null);
		updateUser(user);

		return true;
	}

	// ===== User Status Management =====

	@Override
	@Transactional
	public void suspendUser(Integer userId, String reason) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		user.setStatus("suspended");
		user.setSuspensionReason(reason);
		user.setSuspendedAt(LocalDateTime.now());
		updateUser(user);
	}

	@Override
	@Transactional
	public void unsuspendUser(Integer userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		user.setStatus("active");
		user.setSuspensionReason(null);
		user.setSuspendedAt(null);
		updateUser(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getSuspendedUsers() {
		return userRepository.findUsersByStatus("suspended");
	}

	@Override
	@Transactional
	public void activateUser(Integer userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		user.setStatus("active");
		updateUser(user);
	}

	@Override
	@Transactional
	public void deactivateUser(Integer userId, String reason) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		user.setStatus("inactive");
		user.setSuspensionReason(reason);
		user.setSuspendedAt(LocalDateTime.now());
		updateUser(user);
	}

	// ===== Ban Management =====

	@Override
	@Transactional
	public void banUser(Integer userId, String reason, String currentStatus, Integer adminId) {
		if (userId == null || adminId == null) {
			throw new IllegalArgumentException("User ID and Admin ID cannot be null");
		}

		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		// Check if already banned
		if (isUserBanned(userId)) {
			throw new IllegalStateException("User is already banned");
		}

		user.setStatus("suspended");
		updateUser(user);

		BannedUser bannedUser = new BannedUser();
		bannedUser.setUser(user);
		bannedUser.setBannedBy(adminId.longValue());
		bannedUser.setReason(reason != null ? reason : "No reason provided");
		bannedUser.setBannedAt(LocalDateTime.now());
		bannedUser.setExpiresAt(null); // Permanent ban

		bannedUserRepository.save(bannedUser);
	}

	@Override
	@Transactional
	public void banUserWithDuration(Integer userId, String reason, Integer durationDays, Integer adminId) {
		if (userId == null || adminId == null) {
			throw new IllegalArgumentException("User ID and Admin ID cannot be null");
		}

		User user = getById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}

		if (isUserBanned(userId)) {
			throw new IllegalStateException("User is already banned");
		}

		user.setStatus("suspended");
		updateUser(user);

		BannedUser bannedUser = new BannedUser();
		bannedUser.setUser(user);
		bannedUser.setBannedBy(adminId.longValue());
		bannedUser.setReason(reason != null ? reason : "No reason provided");
		bannedUser.setBannedAt(LocalDateTime.now());

		// Set expiry date if duration is provided
		if (durationDays != null && durationDays > 0) {
			bannedUser.setExpiresAt(LocalDateTime.now().plusDays(durationDays));
		} else {
			bannedUser.setExpiresAt(null); // Permanent ban
		}

		bannedUserRepository.save(bannedUser);
	}

	@Override
	@Transactional
	public void unbanUser(Integer userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}

		User user = getById(userId);
		if (user != null) {
			user.setStatus("active");
			updateUser(user);
		}

		BannedUser activeBan = bannedUserRepository.findActiveBanByUserId(userId);
		if (activeBan != null) {
			activeBan.setExpiresAt(LocalDateTime.now());
			bannedUserRepository.save(activeBan);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<BannedUser> getAllBannedUsers() {
		return bannedUserRepository.findAllActiveBans();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BannedUser> getActiveBans() {
		return bannedUserRepository.findAllActiveBans();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BannedUser> getExpiredBans() {
		return bannedUserRepository.findExpiredBans();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUserBanned(Integer userId) {
		if (userId == null) {
			return false;
		}
		return bannedUserRepository.findActiveBanByUserId(userId) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public BannedUser getBanInfo(Integer userId) {
		if (userId == null) {
			return null;
		}
		return bannedUserRepository.findActiveBanByUserId(userId);
	}

	// ===== Follow/Unfollow System =====

	@Override
	@Transactional
	public void followUser(Integer followerId, Integer followingId) {
		if (followerId == null || followingId == null) {
			throw new IllegalArgumentException("Follower and Following IDs cannot be null");
		}
		if (followerId.equals(followingId)) {
			throw new IllegalArgumentException("You cannot follow yourself");
		}

		// Check if users exist
		if (!exists(followerId)) {
			throw new IllegalArgumentException("Follower user not found with ID: " + followerId);
		}
		if (!exists(followingId)) {
			throw new IllegalArgumentException("Following user not found with ID: " + followingId);
		}

		// Check if already following
		if (isFollowing(followerId, followingId)) {
			throw new IllegalStateException("You are already following this user");
		}

		userRepository.follow(followerId.longValue(), followingId.longValue());
	}

	@Override
	@Transactional
	public void unfollowUser(Integer followerId, Integer followingId) {
		if (followerId == null || followingId == null) {
			throw new IllegalArgumentException("Follower and Following IDs cannot be null");
		}
		if (followerId.equals(followingId)) {
			throw new IllegalArgumentException("You cannot unfollow yourself");
		}

		// Check if following exists before unfollowing
		if (!isFollowing(followerId, followingId)) {
			throw new IllegalStateException("You are not following this user");
		}

		userRepository.unfollow(followerId.longValue(), followingId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isFollowing(Integer followerId, Integer followingId) {
		if (followerId == null || followingId == null) {
			return false;
		}
		return userRepository.isFollowing(followerId.longValue(), followingId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public long countFollowers(Integer userId) {
		if (userId == null) {
			return 0;
		}
		return userRepository.countFollowers(userId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public long countFollowing(Integer userId) {
		if (userId == null) {
			return 0;
		}
		return userRepository.countFollowing(userId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getFollowers(Integer userId) {
		if (userId == null) {
			return new ArrayList<>();
		}
		return userRepository.getFollowers(userId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getFollowing(Integer userId) {
		if (userId == null) {
			return new ArrayList<>();
		}
		return userRepository.getFollowing(userId.longValue());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasFollowers(Integer userId) {
		return countFollowers(userId) > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasFollowing(Integer userId) {
		return countFollowing(userId) > 0;
	}

	// ===== Statistics and Analytics =====

	@Override
	@Transactional(readOnly = true)
	public Map<LocalDateTime, Long> getUserRegistrationStats(LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate == null || endDate == null) {
			return new HashMap<>();
		}
		List<Object[]> results = userRepository.getUserRegistrationStats(startDate, endDate);
		return results.stream().collect(Collectors.toMap(arr -> (LocalDateTime) arr[0], arr -> (Long) arr[1]));
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Long> getUserRoleDistribution() {
		List<Object[]> results = userRepository.getUserRoleDistribution();
		return results.stream().collect(Collectors.toMap(arr -> (String) arr[0], arr -> (Long) arr[1]));
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Long> getUserStatusDistribution() {
		List<User> users = getAllUsers();
		return users.stream().collect(Collectors
				.groupingBy(user -> user.getStatus() != null ? user.getStatus() : "unknown", Collectors.counting()));
	}

	@Override
	@Transactional(readOnly = true)
	public long getActiveUserCount() {
		return userRepository.countByStatus("active");
	}

	@Override
	@Transactional(readOnly = true)
	public long getInactiveUserCount() {
		return userRepository.countByStatus("inactive");
	}

	@Override
	@Transactional(readOnly = true)
	public long getSuspendedUserCount() {
		return userRepository.countByStatus("suspended");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getMostFollowedUsers(int limit) {
		if (limit <= 0) {
			return new ArrayList<>();
		}
		// This would require a custom query in UserRepository
		// For now, return empty list
		return new ArrayList<>();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getMostActiveUsers(int limit) {
		if (limit <= 0) {
			return new ArrayList<>();
		}
		// This would require a custom query in UserRepository
		// For now, return empty list
		return new ArrayList<>();
	}

	// ===== Validation Methods =====

	@Override
	@Transactional(readOnly = true)
	public boolean isUsernameTaken(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		return userRepository.isUsernameTaken(username.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isEmailTaken(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return userRepository.isEmailTaken(email.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isPhoneTaken(String phone) {
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		}
		return userRepository.isPhoneTaken(phone.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public User validateCredentials(String username, String password) {
		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			return null;
		}

		User user = findByUsername(username.trim());
		if (user == null || !"active".equals(user.getStatus())) {
			return null;
		}

		// In real app, compare hashed passwords
		if (password.equals(user.getPasswordHash())) {
			return user;
		}
		return null;
	}

	// ===== Bulk Operations =====

	@Override
	@Transactional
	public int bulkUpdateStatus(List<Integer> userIds, String status, Integer adminId) {
		if (userIds == null || userIds.isEmpty()) {
			return 0;
		}
		if (status == null || status.trim().isEmpty()) {
			throw new IllegalArgumentException("Status cannot be empty");
		}
		if (adminId == null) {
			throw new IllegalArgumentException("Admin ID cannot be null");
		}

		// Validate all users exist
		for (Integer userId : userIds) {
			if (!exists(userId)) {
				throw new IllegalArgumentException("User not found with ID: " + userId);
			}
		}

		return userRepository.bulkUpdateStatus(userIds, status);
	}

	@Override
	@Transactional
	public int deleteInactiveUsers(int daysInactive, Integer adminId) {
		if (daysInactive <= 0) {
			throw new IllegalArgumentException("Days inactive must be positive");
		}
		if (adminId == null) {
			throw new IllegalArgumentException("Admin ID cannot be null");
		}

		LocalDateTime cutoff = LocalDateTime.now().minusDays(daysInactive);
		return userRepository.deleteInactiveUsers(cutoff);
	}

	@Override
	@Transactional
	public void sendNotificationToAllUsers(String subject, String message, Integer adminId) {
		if (subject == null || subject.trim().isEmpty()) {
			throw new IllegalArgumentException("Subject cannot be empty");
		}
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Message cannot be empty");
		}
		if (adminId == null) {
			throw new IllegalArgumentException("Admin ID cannot be null");
		}

		List<User> users = getAllUsers();
		// In real app, send email/SMS notifications
		// For now, just log
		System.out.println("Sending notification to " + users.size() + " users");
		System.out.println("Subject: " + subject);
		System.out.println("Message: " + message);
		System.out.println("Sent by Admin: " + adminId);
	}
}