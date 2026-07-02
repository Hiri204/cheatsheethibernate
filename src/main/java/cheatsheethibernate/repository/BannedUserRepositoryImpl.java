package cheatsheethibernate.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import cheatsheethibernate.entity.BannedUser;

@Repository
public class BannedUserRepositoryImpl implements BannedUserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(BannedUser banUser) {
        getCurrentSession().saveOrUpdate(banUser);
    }

    @Override
    public BannedUser findActiveBanByUserId(Integer userId) {

        String hql = "FROM BannedUser b " +
                     "WHERE b.user.userId = :userId " +
                     "AND (b.expiresAt > current_timestamp() OR b.expiresAt IS NULL)";

        try {
            Query<BannedUser> query =
                    getCurrentSession().createQuery(hql, BannedUser.class);

            query.setParameter("userId", userId);

            return query.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BannedUser> findAllActiveBans() {

        String hql = "SELECT b FROM BannedUser b " +
                     "JOIN FETCH b.user " +
                     "WHERE b.expiresAt > current_timestamp() " +
                     "OR b.expiresAt IS NULL";

        Query<BannedUser> query =
                getCurrentSession().createQuery(hql, BannedUser.class);

        return query.list();
    }
}