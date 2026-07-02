package cheatsheethibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.BannedUserRepository;
import cheatsheethibernate.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BannedUserRepository bannedUserRepository;

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	@Override
	@Transactional
	public Integer insertUser(User obj) {
		userRepository.saveUser(obj);
		return obj.getUserId();
	}

	@Override
	@Transactional(readOnly = true)
	public User getById(Integer id) {
		return userRepository.getById(id);
	}

	@Override
	@Transactional
	public User updateUser(User obj) {
		userRepository.updateUser(obj);
		return obj;
	}

	@Override
	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteUser(id);
	}

	@Override
	@Transactional
	public void banUser(Integer userId, String reason, String currentStatus, Integer adminId) {
		User user = userRepository.getById(userId);
		if (user != null) {
			user.setStatus("suspended");
			userRepository.updateUser(user);

			BannedUser bannedUser = new BannedUser();
			bannedUser.setUser(user);
			bannedUser.setBannedBy(Long.valueOf(adminId));
			bannedUser.setReason(reason);
			bannedUser.setBannedAt(LocalDateTime.now());
			bannedUser.setExpiresAt(null);

			bannedUserRepository.save(bannedUser);
		}
	}

	@Override
	@Transactional
	public void unbanUser(Integer userId) {
		User user = userRepository.getById(userId);
		if (user != null) {
			user.setStatus("active");
			userRepository.updateUser(user);
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
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public int clearExpiredResetCodes() {
		return userRepository.clearExpiredResetCodes();
	}

	// ===== Follow/Unfollow Implementation =====

	@Override
	@Transactional
	public void followUser(Long followerId, Long followingId) {
		if (followerId.equals(followingId)) {
			throw new IllegalArgumentException("You cannot follow yourself");
		}
		userRepository.follow(followerId, followingId);
	}

	@Override
	@Transactional
	public void unfollowUser(Long followerId, Long followingId) {
		userRepository.unfollow(followerId, followingId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isFollowing(Long followerId, Long followingId) {
		return userRepository.isFollowing(followerId, followingId);
	}

	@Override
	@Transactional(readOnly = true)
	public long countFollowers(Long userId) {
		return userRepository.countFollowers(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public long countFollowing(Long userId) {
		return userRepository.countFollowing(userId);
	}
}