package cheatsheethibernate.service;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.Report.ReportStatus;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.CheatSheetRepository;
import cheatsheethibernate.repository.ReportRepository;
import cheatsheethibernate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CheatSheetRepository cheatSheetRepository;

	@Autowired
	private UserRepository userRepository;

	// ===== Report Submission =====

	@Override
	@Transactional
	public void submitReport(Integer userId, Long cheatSheetId, String reason) {
		validateSubmission(userId, cheatSheetId, reason);

		User user = userRepository.getById(userId);
		CheatSheet cheatSheet = cheatSheetRepository.findById(cheatSheetId);

		if (user == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
		if (cheatSheet == null) {
			throw new IllegalArgumentException("CheatSheet not found with ID: " + cheatSheetId);
		}

		// Check if user already reported this sheet
		if (hasUserReportedSheet(userId, cheatSheetId)) {
			throw new IllegalStateException("You have already reported this cheat sheet");
		}

		Report report = new Report();
		report.setCheatSheet(cheatSheet);
		report.setReportedBy(user);
		report.setReason(reason);
		report.setStatus(ReportStatus.PENDING.getValue());
		report.setCreatedAt(LocalDateTime.now());
		report.setUpdatedAt(LocalDateTime.now());

		reportRepository.save(report);
	}

	@Override
	@Transactional
	public void submitReportWithNotes(Integer userId, Long cheatSheetId, String reason, String additionalNotes) {
		String fullReason = reason;
		if (additionalNotes != null && !additionalNotes.trim().isEmpty()) {
			fullReason = reason + "\n\nAdditional Notes: " + additionalNotes;
		}
		submitReport(userId, cheatSheetId, fullReason);
	}

	// ===== Report Retrieval =====

	@Override
	@Transactional(readOnly = true)
	public List<Report> getAllReports() {
		return reportRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getAllReportsWithDetails() {
		return reportRepository.findAllWithDetails();
	}

	@Override
	@Transactional(readOnly = true)
	public Report getReportById(Integer id) {
		if (id == null) {
			return null;
		}
		return reportRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByStatus(String status) {
		if (status == null || status.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return reportRepository.findByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByStatus(ReportStatus status) {
		if (status == null) {
			return new ArrayList<>();
		}
		return getReportsByStatus(status.getValue());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByCheatSheet(Long cheatSheetId) {
		if (cheatSheetId == null) {
			return new ArrayList<>();
		}
		return reportRepository.findByCheatSheetId(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByUser(Integer userId) {
		if (userId == null) {
			return new ArrayList<>();
		}
		return reportRepository.findByReportedBy(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByCheatSheetAndStatus(Long cheatSheetId, String status) {
		if (cheatSheetId == null || status == null) {
			return new ArrayList<>();
		}
		return reportRepository.findByCheatSheetIdAndStatus(cheatSheetId, status);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByStatusIn(List<String> statuses) {
		if (statuses == null || statuses.isEmpty()) {
			return new ArrayList<>();
		}
		return reportRepository.findByStatusIn(statuses);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate == null || endDate == null) {
			return new ArrayList<>();
		}
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date must be before end date");
		}
		return reportRepository.findByCreatedAtBetween(startDate, endDate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getRecentReports(int limit) {
		if (limit <= 0) {
			return new ArrayList<>();
		}
		return reportRepository.findRecentReports(limit);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> searchReports(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return reportRepository.searchByKeyword(keyword.trim());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getReportsNeedingAttention() {
		return reportRepository.findReportsNeedingAttention();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Report> getPendingReportsOlderThan(int days) {
		if (days <= 0) {
			return new ArrayList<>();
		}
		return reportRepository.findPendingReportsOlderThan(days);
	}

	// ===== Report Administration =====

	@Override
	@Transactional
	public void actionReport(Integer reportId, String status) {
		if (reportId == null || status == null) {
			throw new IllegalArgumentException("Report ID and status cannot be null");
		}

		Report report = reportRepository.findById(reportId);
		if (report == null) {
			throw new IllegalArgumentException("Report not found with ID: " + reportId);
		}

		// Validate status transition
		validateStatusTransition(report.getStatus(), status);

		report.setStatus(status);
		report.setUpdatedAt(LocalDateTime.now());
		reportRepository.update(report);
	}

	@Override
	@Transactional
	public void actionReport(Integer reportId, String status, String adminNotes, Integer adminId) {
		if (adminId == null) {
			throw new IllegalArgumentException("Admin ID cannot be null");
		}

		actionReport(reportId, status);

		// Add admin notes
		if (adminNotes != null && !adminNotes.trim().isEmpty()) {
			Report report = reportRepository.findById(reportId);
			if (report != null) {
				String currentNotes = report.getResolutionNotes();
				String timestamp = LocalDateTime.now()
						.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				String newNote = String.format("[%s] Admin %d: %s", timestamp, adminId, adminNotes);
				report.setResolutionNotes(currentNotes != null ? currentNotes + "\n" + newNote : newNote);
				report.setResolvedBy(adminId);
				reportRepository.update(report);
			}
		}
	}

	@Override
	@Transactional
	public void resolveReport(Integer reportId, String resolutionNotes, Integer adminId) {
		if (reportId == null || adminId == null) {
			throw new IllegalArgumentException("Report ID and Admin ID cannot be null");
		}

		Report report = reportRepository.findById(reportId);
		if (report == null) {
			throw new IllegalArgumentException("Report not found with ID: " + reportId);
		}

		if (ReportStatus.RESOLVED.getValue().equals(report.getStatus())) {
			throw new IllegalStateException("Report is already resolved");
		}

		report.setResolved(resolutionNotes, adminId);
		reportRepository.update(report);
	}

	@Override
	@Transactional
	public void rejectReport(Integer reportId, String rejectionReason, Integer adminId) {
		if (reportId == null || adminId == null) {
			throw new IllegalArgumentException("Report ID and Admin ID cannot be null");
		}

		Report report = reportRepository.findById(reportId);
		if (report == null) {
			throw new IllegalArgumentException("Report not found with ID: " + reportId);
		}

		if (ReportStatus.REJECTED.getValue().equals(report.getStatus())) {
			throw new IllegalStateException("Report is already rejected");
		}

		report.setRejected(rejectionReason, adminId);
		reportRepository.update(report);
	}

	@Override
	@Transactional
	public int bulkUpdateStatus(List<Integer> reportIds, String status, Integer adminId) {
		if (reportIds == null || reportIds.isEmpty()) {
			return 0;
		}
		if (status == null || status.trim().isEmpty()) {
			throw new IllegalArgumentException("Status cannot be empty");
		}
		if (adminId == null) {
			throw new IllegalArgumentException("Admin ID cannot be null");
		}

		// Validate all reports exist
		for (Integer reportId : reportIds) {
			Report report = reportRepository.findById(reportId);
			if (report == null) {
				throw new IllegalArgumentException("Report not found with ID: " + reportId);
			}
		}

		int updated = reportRepository.bulkUpdateStatus(reportIds, status);

		// Add admin notes for each updated report
		String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		String note = String.format("[%s] Admin %d bulk updated status to: %s", timestamp, adminId, status);
		for (Integer reportId : reportIds) {
			Report report = reportRepository.findById(reportId);
			if (report != null) {
				String currentNotes = report.getResolutionNotes();
				report.setResolutionNotes(currentNotes != null ? currentNotes + "\n" + note : note);
				report.setResolvedBy(adminId);
				reportRepository.update(report);
			}
		}

		return updated;
	}

	// ===== Validation and Checks =====

	@Override
	@Transactional(readOnly = true)
	public boolean hasUserReportedSheet(Integer userId, Long cheatSheetId) {
		if (userId == null || cheatSheetId == null) {
			return false;
		}
		List<Report> reports = reportRepository.findByReportedBy(userId);
		return reports.stream().anyMatch(r -> r.getCheatSheet().getId().equals(cheatSheetId));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasPendingReports(Long cheatSheetId) {
		if (cheatSheetId == null) {
			return false;
		}
		return reportRepository.hasPendingReports(cheatSheetId);
	}

	@Override
	@Transactional(readOnly = true)
	public long countByStatus(String status) {
		if (status == null || status.trim().isEmpty()) {
			return 0;
		}
		return reportRepository.countByStatus(status);
	}

	@Override
	@Transactional(readOnly = true)
	public long getTotalReportCount() {
		return reportRepository.count();
	}

	// ===== Statistics and Analytics =====

	@Override
	@Transactional(readOnly = true)
	public Map<String, Long> getReportStatusDistribution() {
		List<Object[]> results = reportRepository.getStatusDistribution();
		return results.stream().collect(Collectors.toMap(arr -> (String) arr[0], arr -> (Long) arr[1]));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getDailyReportStats(LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate == null || endDate == null) {
			return new ArrayList<>();
		}
		return reportRepository.getDailyReportStats(startDate, endDate);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Long> getReportStatsByCheatSheet() {
		List<Object[]> results = reportRepository.getReportStatsByCheatSheet();
		return results.stream().collect(Collectors.toMap(arr -> (Long) arr[0], arr -> (Long) arr[1]));
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, Long> getReportStatsByUser() {
		List<Object[]> results = reportRepository.getReportStatsByUser();
		return results.stream().collect(Collectors.toMap(arr -> (Integer) arr[0], arr -> (Long) arr[1]));
	}

	@Override
	@Transactional(readOnly = true)
	public Double getAverageResolutionTime() {
		return reportRepository.getAverageResolutionTime();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> getTopReportedSheets(int limit) {
		if (limit <= 0) {
			return new ArrayList<>();
		}
		List<Object[]> results = reportRepository.getReportStatsByCheatSheet();
		return results.stream().limit(limit).collect(Collectors.toList());
	}

	// ===== Report Management =====

	@Override
	@Transactional
	public void deleteReport(Integer reportId, Integer adminId) {
		if (reportId == null || adminId == null) {
			throw new IllegalArgumentException("Report ID and Admin ID cannot be null");
		}

		Report report = reportRepository.findById(reportId);
		if (report == null) {
			throw new IllegalArgumentException("Report not found with ID: " + reportId);
		}

		reportRepository.delete(reportId);
	}

	@Override
	@Transactional
	public int deleteReportsByCheatSheet(Long cheatSheetId, Integer adminId) {
		if (cheatSheetId == null || adminId == null) {
			throw new IllegalArgumentException("CheatSheet ID and Admin ID cannot be null");
		}
		return reportRepository.deleteByCheatSheetId(cheatSheetId);
	}

	@Override
	@Transactional
	public int deleteOldReports(int daysOld, Integer adminId) {
		if (daysOld <= 0 || adminId == null) {
			throw new IllegalArgumentException("Days old must be positive and Admin ID cannot be null");
		}
		LocalDateTime cutoff = LocalDateTime.now().minusDays(daysOld);
		return reportRepository.deleteOlderThan(cutoff);
	}

	@Override
	@Transactional
	public int archiveOldResolvedReports(int daysOld, Integer adminId) {
		if (daysOld <= 0 || adminId == null) {
			throw new IllegalArgumentException("Days old must be positive and Admin ID cannot be null");
		}
		// This is a soft archive - we could move to archive table or just mark as
		// archived
		LocalDateTime cutoff = LocalDateTime.now().minusDays(daysOld);
		List<Report> oldReports = reportRepository.findByResolvedAtBetween(LocalDateTime.MIN, cutoff);

		if (oldReports.isEmpty()) {
			return 0;
		}

		List<Integer> reportIds = oldReports.stream().map(Report::getId).collect(Collectors.toList());

		// Could move to archive table here, or just mark as archived
		return reportRepository.bulkUpdateStatus(reportIds, "archived");
	}

	// ===== Private Helper Methods =====

	private void validateSubmission(Integer userId, Long cheatSheetId, String reason) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		if (cheatSheetId == null) {
			throw new IllegalArgumentException("CheatSheet ID cannot be null");
		}
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Reason cannot be empty");
		}
		if (reason.length() < 10) {
			throw new IllegalArgumentException("Reason must be at least 10 characters long");
		}
		if (reason.length() > 1000) {
			throw new IllegalArgumentException("Reason cannot exceed 1000 characters");
		}
	}

	private void validateStatusTransition(String currentStatus, String newStatus) {
		if (currentStatus == null || newStatus == null) {
			throw new IllegalArgumentException("Status cannot be null");
		}

		// Define valid transitions
		Map<String, List<String>> validTransitions = new HashMap<>();
		validTransitions.put(ReportStatus.PENDING.getValue(), Arrays.asList(ReportStatus.UNDER_REVIEW.getValue(),
				ReportStatus.RESOLVED.getValue(), ReportStatus.REJECTED.getValue()));
		validTransitions.put(ReportStatus.UNDER_REVIEW.getValue(),
				Arrays.asList(ReportStatus.RESOLVED.getValue(), ReportStatus.REJECTED.getValue()));
		validTransitions.put(ReportStatus.RESOLVED.getValue(), Arrays.asList(ReportStatus.REJECTED.getValue())); // Can
																													// re-open
																													// if
																													// needed
		validTransitions.put(ReportStatus.REJECTED.getValue(), Arrays.asList(ReportStatus.UNDER_REVIEW.getValue())); // Can
																														// re-open
																														// if
																														// needed

		List<String> allowed = validTransitions.get(currentStatus);
		if (allowed == null) {
			allowed = new ArrayList<>();
		}

		if (!newStatus.equals(currentStatus) && !allowed.contains(newStatus)) {
			throw new IllegalStateException(String.format("Invalid status transition from '%s' to '%s'. Allowed: %s",
					currentStatus, newStatus, allowed));
		}
	}
}