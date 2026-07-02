package cheatsheethibernate.service;

import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;
import java.util.List;

public interface UserService {
	// Basic CRUD
	List<User> getAllUsers();

	Integer insertUser(User obj);

	User getById(Integer id);

	User updateUser(User obj);

	void deleteUser(Integer id);

	// Ban/Unban
	List<BannedUser> getAllBannedUsers();

	void unbanUser(Integer userId);

	void banUser(Integer userId, String reason, String currentStatus, Integer adminId);

	// User lookup
	User findByEmail(String email);

	int clearExpiredResetCodes();

	// ===== Follow/Unfollow Methods =====
	void followUser(Long followerId, Long followingId);

	void unfollowUser(Long followerId, Long followingId);

	boolean isFollowing(Long followerId, Long followingId);

	long countFollowers(Long userId);

	long countFollowing(Long userId);
}