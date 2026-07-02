package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Follow;
import cheatsheethibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private FollowRepository followRepository;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return getCurrentSession().createQuery("from User").list();
	}

	@Override
	public void saveUser(User user) {
		getCurrentSession().saveOrUpdate(user);
		getCurrentSession().flush();
	}

	@Override
	public User getById(Integer id) {
		return getCurrentSession().get(User.class, id);
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
			session.delete(user);
			session.flush();
		}
	}

	@Override
	public User findByUsername(String username) {
		String hql = "FROM User u WHERE u.username = :username";
		try {
			Query<User> query = getCurrentSession().createQuery(hql, User.class);
			query.setParameter("username", username);
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findByEmail(String email) {
		String hql = "FROM User u WHERE u.email = :email";
		try {
			Query<User> query = getCurrentSession().createQuery(hql, User.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int clearExpiredResetCodes() {
		String hql = "UPDATE User SET resetCode = null, expiryDate = null WHERE expiryDate < :currentTime";
		try {
			Query<?> query = getCurrentSession().createQuery(hql);
			query.setParameter("currentTime", LocalDateTime.now());
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ===== Follow/Unfollow Implementation =====

	@Override
	public void follow(Long followerId, Long followingId) {
		// Check if already following
		if (followRepository.isFollowing(followerId, followingId)) {
			return;
		}

		// Get User entities - Convert Long to Integer for getById
		User follower = getById(followerId.intValue());
		User following = getById(followingId.intValue());

		if (follower != null && following != null) {
			Follow follow = new Follow();
			follow.setFollower(follower);
			follow.setFollowing(following);
			followRepository.save(follow);
		}
	}

	@Override
	public void unfollow(Long followerId, Long followingId) {
		followRepository.deleteByFollowerAndFollowing(followerId, followingId);
	}

	@Override
	public boolean isFollowing(Long followerId, Long followingId) {
		return followRepository.isFollowing(followerId, followingId);
	}

	@Override
	public long countFollowers(Long userId) {
		return followRepository.countFollowers(userId);
	}

	@Override
	public long countFollowing(Long userId) {
		return followRepository.countFollowing(userId);
	}

	@Override
	public List<User> getFollowers(Long userId) {
		return followRepository.getFollowers(userId);
	}

	@Override
	public List<User> getFollowing(Long userId) {
		return followRepository.getFollowing(userId);
	}
}