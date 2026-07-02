package cheatsheethibernate.repository;

import cheatsheethibernate.entity.User;
import java.util.List;

public interface UserRepository {
	List<User> getAllUsers();

	void saveUser(User user);

	User getById(Integer id);

	void updateUser(User user);

	void deleteUser(Integer id);

	User findByUsername(String username);

	User findByEmail(String email);

	int clearExpiredResetCodes();

	// ===== Follow/Unfollow Methods =====
	void follow(Long followerId, Long followingId);

	void unfollow(Long followerId, Long followingId);

	boolean isFollowing(Long followerId, Long followingId);

	long countFollowers(Long userId);

	long countFollowing(Long userId);

	List<User> getFollowers(Long userId);

	List<User> getFollowing(Long userId);
}