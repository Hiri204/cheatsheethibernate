package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Report;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(Report report) {
		sessionFactory.getCurrentSession().saveOrUpdate(report);
	}

	@Override
	public List<Report> findAll() {
		return sessionFactory.getCurrentSession().createQuery("FROM Report r ORDER BY r.createdAt DESC", Report.class)
				.getResultList();
	}

	@Override
	public Report findById(Integer id) {
		return sessionFactory.getCurrentSession().get(Report.class, id);
	}

	@Override
	public void update(Report report) {
		sessionFactory.getCurrentSession().update(report);
	}

	@Override
	public void delete(Integer id) {
		Report report = findById(id);
		if (report != null) {
			sessionFactory.getCurrentSession().delete(report);
		}
	}
}