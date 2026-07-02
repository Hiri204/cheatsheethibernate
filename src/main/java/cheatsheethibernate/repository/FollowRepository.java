package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Follow;
import cheatsheethibernate.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowRepository {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Save a follow relationship
	 */
	public void save(Follow follow) {
		sessionFactory.getCurrentSession().saveOrUpdate(follow);
	}

	/**
	 * Delete a follow relationship
	 */
	public void delete(Follow follow) {
		sessionFactory.getCurrentSession().delete(follow);
	}

	/**
	 * Find follow by follower and following
	 */
	public Follow findByFollowerAndFollowing(Integer followerId, Integer followingId) {
		String hql = "FROM Follow f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId";
		try {
			return sessionFactory.getCurrentSession().createQuery(hql, Follow.class)
					.setParameter("followerId", followerId).setParameter("followingId", followingId).uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Check if a user is following another user
	 */
	public boolean isFollowing(Integer followerId, Integer followingId) {
		String hql = "SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId";
		Long count = sessionFactory.getCurrentSession().createQuery(hql, Long.class)
				.setParameter("followerId", followerId).setParameter("followingId", followingId).uniqueResult();
		return count != null && count > 0;
	}

	/**
	 * Count followers of a user
	 */
	public long countFollowers(Integer userId) {
		String hql = "SELECT COUNT(f) FROM Follow f WHERE f.following.userId = :userId";
		Long count = sessionFactory.getCurrentSession().createQuery(hql, Long.class).setParameter("userId", userId)
				.uniqueResult();
		return count != null ? count : 0L;
	}

	/**
	 * Count following of a user
	 */
	public long countFollowing(Integer userId) {
		String hql = "SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :userId";
		Long count = sessionFactory.getCurrentSession().createQuery(hql, Long.class).setParameter("userId", userId)
				.uniqueResult();
		return count != null ? count : 0L;
	}

	/**
	 * Get all followers of a user
	 */
	public List<User> getFollowers(Integer userId) {
		String hql = "SELECT f.follower FROM Follow f WHERE f.following.userId = :userId ORDER BY f.createdAt DESC";
		return sessionFactory.getCurrentSession().createQuery(hql, User.class).setParameter("userId", userId)
				.getResultList();
	}

	/**
	 * Get all users that a user is following
	 */
	public List<User> getFollowing(Integer userId) {
		String hql = "SELECT f.following FROM Follow f WHERE f.follower.userId = :userId ORDER BY f.createdAt DESC";
		return sessionFactory.getCurrentSession().createQuery(hql, User.class).setParameter("userId", userId)
				.getResultList();
	}

	/**
	 * Delete follow by follower and following IDs
	 */
	public void deleteByFollowerAndFollowing(Integer followerId, Integer followingId) {
		String hql = "DELETE FROM Follow f WHERE f.follower.userId = :followerId AND f.following.userId = :followingId";
		sessionFactory.getCurrentSession().createQuery(hql).setParameter("followerId", followerId)
				.setParameter("followingId", followingId).executeUpdate();
	}

	/**
	 * Delete all follows involving a user (both as follower and following) This is
	 * used when deleting a user account
	 */
	public void deleteAllByUser(Integer userId) {
		String hql = "DELETE FROM Follow f WHERE f.follower.userId = :userId OR f.following.userId = :userId";
		sessionFactory.getCurrentSession().createQuery(hql).setParameter("userId", userId).executeUpdate();
	}

	/**
	 * Get all follows (for admin or debugging)
	 */
	public List<Follow> findAll() {
		String hql = "FROM Follow f ORDER BY f.createdAt DESC";
		return sessionFactory.getCurrentSession().createQuery(hql, Follow.class).getResultList();
	}
}