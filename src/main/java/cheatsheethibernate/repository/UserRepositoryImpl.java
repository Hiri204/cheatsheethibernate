package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Follow;
import cheatsheethibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private FollowRepository followRepository;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	// ===== Basic CRUD Operations =====

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		try {
			return getCurrentSession().createQuery("FROM User u ORDER BY u.userId DESC").list();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public void saveUser(User user) {
		getCurrentSession().saveOrUpdate(user);
		getCurrentSession().flush();
	}

	@Override
	public User getById(Integer id) {
		try {
			return getCurrentSession().get(User.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Optional<User> findByIdOptional(Integer id) {
		return Optional.ofNullable(getById(id));
	}

	@Override
	public void updateUser(User user) {
		getCurrentSession().update(user);
		getCurrentSession().flush();
	}

	@Override
	public void deleteUser(Integer id) {
		Session session = getCurrentSession();
		User user = session.get(User.class, id);
		if (user != null) {
			// Delete follows using native query
			session.createNativeQuery("DELETE FROM follows WHERE follower_id = :userId OR following_id = :userId")
					.setParameter("userId", id).executeUpdate();
			session.delete(user);
			session.flush();
		}
	}

	@Override
	public boolean exists(Integer id) {
		try {
			Long count = getCurrentSession().createQuery("SELECT COUNT(u) FROM User u WHERE u.userId = :id", Long.class)
					.setParameter("id", id).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long count() {
		try {
			return getCurrentSession().createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ===== Search and Find Methods =====

	@Override
	public User findByUsername(String username) {
		try {
			return getCurrentSession().createQuery("FROM User u WHERE u.username = :username", User.class)
					.setParameter("username", username).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findByEmail(String email) {
		try {
			return getCurrentSession().createQuery("FROM User u WHERE u.email = :email", User.class)
					.setParameter("email", email).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> searchUsers(String keyword) {
		try {
			String pattern = "%" + keyword.toLowerCase() + "%";
			return getCurrentSession().createQuery(
					"FROM User u WHERE LOWER(u.username) LIKE :keyword OR LOWER(u.email) LIKE :keyword ORDER BY u.username",
					User.class).setParameter("keyword", pattern).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<User> findUsersByStatus(String status) {
		try {
			return getCurrentSession()
					.createQuery("FROM User u WHERE u.status = :status ORDER BY u.userId DESC", User.class)
					.setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public long countByStatus(String status) {
		try {
			return getCurrentSession().createQuery("SELECT COUNT(u) FROM User u WHERE u.status = :status", Long.class)
					.setParameter("status", status).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<User> findUsersByRole(String role) {
		try {
			return getCurrentSession()
					.createQuery("FROM User u WHERE u.role = :role ORDER BY u.userId DESC", User.class)
					.setParameter("role", role).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<User> getRecentUsers(int limit) {
		try {
			return getCurrentSession().createQuery("FROM User u ORDER BY u.createdAt DESC", User.class)
					.setMaxResults(limit).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== Password Reset Methods =====

	@Override
	public int clearExpiredResetCodes() {
		try {
			return getCurrentSession().createQuery(
					"UPDATE User SET resetCode = null, resetCodeExpiry = null WHERE resetCodeExpiry < :currentTime")
					.setParameter("currentTime", LocalDateTime.now()).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void saveResetCode(String email, String resetCode, LocalDateTime expiryDate) {
		try {
			User user = findByEmail(email);
			if (user != null) {
				user.setResetCode(resetCode);
				user.setResetCodeExpiry(expiryDate);
				updateUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findByResetCode(String resetCode) {
		try {
			return getCurrentSession()
					.createQuery("FROM User u WHERE u.resetCode = :resetCode AND u.resetCodeExpiry > :currentTime",
							User.class)
					.setParameter("resetCode", resetCode).setParameter("currentTime", LocalDateTime.now())
					.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ===== User Status Management =====

	@Override
	public void suspendUser(Integer userId, String reason) {
		try {
			User user = getById(userId);
			if (user != null) {
				user.setStatus("suspended");
				user.setSuspensionReason(reason);
				user.setSuspendedAt(LocalDateTime.now());
				updateUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unsuspendUser(Integer userId) {
		try {
			User user = getById(userId);
			if (user != null) {
				user.setStatus("active");
				user.setSuspensionReason(null);
				user.setSuspendedAt(null);
				updateUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getSuspendedUsers() {
		return findUsersByStatus("suspended");
	}

	// ===== Follow/Unfollow Implementation =====

	@Override
	public void follow(Long followerId, Long followingId) {
		// Cannot follow yourself
		if (followerId.equals(followingId)) {
			return;
		}

		// Convert Long to Integer for FollowRepository
		Integer followerInt = followerId.intValue();
		Integer followingInt = followingId.intValue();

		// Check if already following
		if (followRepository.isFollowing(followerInt, followingInt)) {
			return;
		}

		// Get User entities
		User follower = getById(followerInt);
		User following = getById(followingInt);

		if (follower != null && following != null) {
			Follow follow = new Follow();
			follow.setFollower(follower);
			follow.setFollowing(following);
			followRepository.save(follow);
		}
	}

	@Override
	public void unfollow(Long followerId, Long followingId) {
		// Convert Long to Integer
		Integer followerInt = followerId.intValue();
		Integer followingInt = followingId.intValue();

		// Check if following exists before trying to unfollow
		if (followRepository.isFollowing(followerInt, followingInt)) {
			followRepository.deleteByFollowerAndFollowing(followerInt, followingInt);
		}
	}

	@Override
	public boolean isFollowing(Long followerId, Long followingId) {
		return followRepository.isFollowing(followerId.intValue(), followingId.intValue());
	}

	@Override
	public long countFollowers(Long userId) {
		return followRepository.countFollowers(userId.intValue());
	}

	@Override
	public long countFollowing(Long userId) {
		return followRepository.countFollowing(userId.intValue());
	}

	@Override
	public List<User> getFollowers(Long userId) {
		try {
			return followRepository.getFollowers(userId.intValue());
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<User> getFollowing(Long userId) {
		try {
			return followRepository.getFollowing(userId.intValue());
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public boolean hasFollowers(Long userId) {
		return countFollowers(userId) > 0;
	}

	@Override
	public boolean hasFollowing(Long userId) {
		return countFollowing(userId) > 0;
	}

	// ===== Statistics Methods =====

	@Override
	public List<Object[]> getUserRegistrationStats(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			return getCurrentSession()
					.createQuery("SELECT FUNCTION('DATE', u.createdAt), COUNT(u) FROM User u "
							+ "WHERE u.createdAt BETWEEN :startDate AND :endDate "
							+ "GROUP BY FUNCTION('DATE', u.createdAt) " + "ORDER BY FUNCTION('DATE', u.createdAt)",
							Object[].class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Object[]> getUserRoleDistribution() {
		try {
			return getCurrentSession()
					.createQuery("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role", Object[].class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== Bulk Operations =====

	@Override
	public int bulkUpdateStatus(List<Integer> userIds, String status) {
		try {
			return getCurrentSession().createQuery(
					"UPDATE User u SET u.status = :status, u.updatedAt = :updatedAt WHERE u.userId IN (:userIds)")
					.setParameter("status", status).setParameter("updatedAt", LocalDateTime.now())
					.setParameter("userIds", userIds).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deleteInactiveUsers(LocalDateTime cutoffDate) {
		try {
			// First, delete all follows for inactive users
			List<User> inactiveUsers = getCurrentSession()
					.createQuery("FROM User u WHERE u.lastLoginAt < :cutoffDate AND u.status = 'inactive'", User.class)
					.setParameter("cutoffDate", cutoffDate).getResultList();

			for (User user : inactiveUsers) {
				getCurrentSession()
						.createNativeQuery("DELETE FROM follows WHERE follower_id = :userId OR following_id = :userId")
						.setParameter("userId", user.getUserId()).executeUpdate();
			}

			// Then delete the users
			return getCurrentSession()
					.createQuery("DELETE FROM User u WHERE u.lastLoginAt < :cutoffDate AND u.status = 'inactive'")
					.setParameter("cutoffDate", cutoffDate).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ===== Validation Methods =====

	@Override
	public boolean isUsernameTaken(String username) {
		try {
			Long count = getCurrentSession()
					.createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.username) = LOWER(:username)", Long.class)
					.setParameter("username", username).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isEmailTaken(String email) {
		try {
			Long count = getCurrentSession()
					.createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.email) = LOWER(:email)", Long.class)
					.setParameter("email", email).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isPhoneTaken(String phone) {
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		}
		try {
			Long count = getCurrentSession()
					.createQuery("SELECT COUNT(u) FROM User u WHERE u.phone = :phone", Long.class)
					.setParameter("phone", phone).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}