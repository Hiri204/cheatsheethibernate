package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.Report.ReportStatus;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ReportRepositoryImpl implements ReportRepository {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	// ===== Basic CRUD Operations =====

	@Override
	public void save(Report report) {
		getCurrentSession().saveOrUpdate(report);
	}

	@Override
	public List<Report> findAll() {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "ORDER BY r.createdAt DESC", Report.class)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public Report findById(Integer id) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.id = :id", Report.class)
					.setParameter("id", id).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Optional<Report> findByIdOptional(Integer id) {
		return Optional.ofNullable(findById(id));
	}

	@Override
	public void update(Report report) {
		getCurrentSession().update(report);
	}

	@Override
	public void delete(Integer id) {
		Report report = findById(id);
		if (report != null) {
			getCurrentSession().delete(report);
		}
	}

	@Override
	public boolean exists(Integer id) {
		try {
			Long count = getCurrentSession().createQuery("SELECT COUNT(r) FROM Report r WHERE r.id = :id", Long.class)
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
			return getCurrentSession().createQuery("SELECT COUNT(r) FROM Report r", Long.class).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ===== Status-Based Queries =====

	@Override
	public List<Report> findByStatus(String status) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession().createQuery("SELECT DISTINCT r FROM Report r "
					+ "LEFT JOIN FETCH r.cheatSheet c " + "LEFT JOIN FETCH r.reportedBy u "
					+ "WHERE r.status = :status " + "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByStatusWithPagination(String status, int offset, int limit) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.status = :status "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("status", status).setFirstResult(offset).setMaxResults(limit).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public long countByStatus(String status) {
		try {
			return getCurrentSession().createQuery("SELECT COUNT(r) FROM Report r WHERE r.status = :status", Long.class)
					.setParameter("status", status).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Object[]> getStatusDistribution() {
		try {
			return getCurrentSession()
					.createQuery("SELECT r.status, COUNT(r) FROM Report r GROUP BY r.status", Object[].class)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== Sheet-Related Queries =====

	@Override
	public List<Report> findByCheatSheetId(Long cheatSheetId) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.cheatSheet.id = :cheatSheetId "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("cheatSheetId", cheatSheetId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByCheatSheetIdAndStatus(Long cheatSheetId, String status) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u "
							+ "WHERE r.cheatSheet.id = :cheatSheetId AND r.status = :status "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("cheatSheetId", cheatSheetId).setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public boolean hasPendingReports(Long cheatSheetId) {
		try {
			Long count = getCurrentSession()
					.createQuery("SELECT COUNT(r) FROM Report r "
							+ "WHERE r.cheatSheet.id = :cheatSheetId AND r.status = :status", Long.class)
					.setParameter("cheatSheetId", cheatSheetId).setParameter("status", ReportStatus.PENDING.getValue())
					.getSingleResult();
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Report> findUnresolvedByCheatSheetId(Long cheatSheetId) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u "
							+ "WHERE r.cheatSheet.id = :cheatSheetId AND r.status IN (:statuses) "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("cheatSheetId", cheatSheetId)
					.setParameter("statuses",
							List.of(ReportStatus.PENDING.getValue(), ReportStatus.UNDER_REVIEW.getValue()))
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== User-Related Queries =====

	@Override
	public List<Report> findByReportedBy(Integer userId) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession().createQuery("SELECT DISTINCT r FROM Report r "
					+ "LEFT JOIN FETCH r.cheatSheet c " + "LEFT JOIN FETCH r.reportedBy u "
					+ "WHERE r.reportedBy.id = :userId " + "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("userId", userId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByReportedByAndStatus(Integer userId, String status) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession().createQuery(
					"SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u "
							+ "WHERE r.reportedBy.id = :userId AND r.status = :status " + "ORDER BY r.createdAt DESC",
					Report.class).setParameter("userId", userId).setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public long countByReportedBy(Integer userId) {
		try {
			return getCurrentSession()
					.createQuery("SELECT COUNT(r) FROM Report r WHERE r.reportedBy.id = :userId", Long.class)
					.setParameter("userId", userId).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Report> findByResolvedBy(Integer adminId) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.resolvedBy = :adminId "
							+ "ORDER BY r.resolvedAt DESC", Report.class)
					.setParameter("adminId", adminId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== Date-Range Queries =====

	@Override
	public List<Report> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.createdAt BETWEEN :startDate AND :endDate "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByCreatedAtAfter(LocalDateTime date) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession().createQuery("SELECT DISTINCT r FROM Report r "
					+ "LEFT JOIN FETCH r.cheatSheet c " + "LEFT JOIN FETCH r.reportedBy u "
					+ "WHERE r.createdAt > :date " + "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("date", date).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByCreatedAtBefore(LocalDateTime date) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession().createQuery("SELECT DISTINCT r FROM Report r "
					+ "LEFT JOIN FETCH r.cheatSheet c " + "LEFT JOIN FETCH r.reportedBy u "
					+ "WHERE r.createdAt < :date " + "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("date", date).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByResolvedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.resolvedAt BETWEEN :startDate AND :endDate "
							+ "ORDER BY r.resolvedAt DESC", Report.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	// ===== Advanced Query Methods =====

	@Override
	public List<Report> findAllWithDetails() {
		try {
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "ORDER BY r.createdAt DESC", Report.class)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findByStatusIn(List<String> statuses) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.status IN (:statuses) "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("statuses", statuses).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findRecentReports(int limit) {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "ORDER BY r.createdAt DESC", Report.class)
					.setMaxResults(limit).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> searchByKeyword(String keyword) {
		try {
			String pattern = "%" + keyword.toLowerCase() + "%";
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u "
							+ "WHERE LOWER(r.reason) LIKE :keyword OR LOWER(r.resolutionNotes) LIKE :keyword "
							+ "ORDER BY r.createdAt DESC", Report.class)
					.setParameter("keyword", pattern).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findReportsNeedingAttention() {
		try {
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.status IN (:statuses) "
							+ "ORDER BY r.createdAt ASC", Report.class)
					.setParameter("statuses",
							List.of(ReportStatus.PENDING.getValue(), ReportStatus.UNDER_REVIEW.getValue()))
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Report> findPendingReportsOlderThan(int days) {
		try {
			LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
			// 🎯 FIX: Use JOIN FETCH to eagerly load cheatSheet and reportedBy
			return getCurrentSession()
					.createQuery("SELECT DISTINCT r FROM Report r " + "LEFT JOIN FETCH r.cheatSheet c "
							+ "LEFT JOIN FETCH r.reportedBy u " + "WHERE r.status = :status AND r.createdAt < :cutoff "
							+ "ORDER BY r.createdAt ASC", Report.class)
					.setParameter("status", ReportStatus.PENDING.getValue()).setParameter("cutoff", cutoff)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public int bulkUpdateStatus(List<Integer> reportIds, String status) {
		try {
			return getCurrentSession().createQuery(
					"UPDATE Report r SET r.status = :status, r.updatedAt = :updatedAt WHERE r.id IN (:reportIds)")
					.setParameter("status", status).setParameter("updatedAt", LocalDateTime.now())
					.setParameter("reportIds", reportIds).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deleteByCheatSheetId(Long cheatSheetId) {
		try {
			return getCurrentSession().createQuery("DELETE FROM Report r WHERE r.cheatSheet.id = :cheatSheetId")
					.setParameter("cheatSheetId", cheatSheetId).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deleteOlderThan(LocalDateTime date) {
		try {
			return getCurrentSession().createQuery("DELETE FROM Report r WHERE r.createdAt < :date")
					.setParameter("date", date).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// ===== Statistics Methods =====

	@Override
	public List<Object[]> getDailyReportStats(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			return getCurrentSession()
					.createQuery("SELECT FUNCTION('DATE', r.createdAt), COUNT(r) FROM Report r "
							+ "WHERE r.createdAt BETWEEN :startDate AND :endDate "
							+ "GROUP BY FUNCTION('DATE', r.createdAt) " + "ORDER BY FUNCTION('DATE', r.createdAt)",
							Object[].class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Object[]> getReportStatsByCheatSheet() {
		try {
			return getCurrentSession().createQuery(
					"SELECT r.cheatSheet.id, COUNT(r) FROM Report r GROUP BY r.cheatSheet.id ORDER BY COUNT(r) DESC",
					Object[].class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public List<Object[]> getReportStatsByUser() {
		try {
			return getCurrentSession().createQuery(
					"SELECT r.reportedBy.id, COUNT(r) FROM Report r GROUP BY r.reportedBy.id ORDER BY COUNT(r) DESC",
					Object[].class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	@Override
	public Double getAverageResolutionTime() {
		try {
			return getCurrentSession()
					.createQuery(
							"SELECT AVG(FUNCTION('TIMESTAMPDIFF', HOUR, r.createdAt, r.resolvedAt)) "
									+ "FROM Report r WHERE r.status = :status AND r.resolvedAt IS NOT NULL",
							Double.class)
					.setParameter("status", ReportStatus.RESOLVED.getValue()).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}