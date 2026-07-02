package cheatsheethibernate.service;

import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.Report.ReportStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Report management. Provides business logic for report
 * submission, retrieval, and administration.
 */
public interface ReportService {

	// ===== Report Submission =====

	/**
	 * Submit a new report for a cheat sheet
	 * 
	 * @param userId       The ID of the user submitting the report
	 * @param cheatSheetId The ID of the cheat sheet being reported
	 * @param reason       The reason for the report
	 * @throws IllegalArgumentException if any parameter is invalid
	 * @throws IllegalStateException    if user has already reported this sheet
	 */
	void submitReport(Integer userId, Long cheatSheetId, String reason);

	/**
	 * Submit a new report with additional details
	 * 
	 * @param userId          The ID of the user submitting the report
	 * @param cheatSheetId    The ID of the cheat sheet being reported
	 * @param reason          The reason for the report
	 * @param additionalNotes Additional notes about the report
	 */
	void submitReportWithNotes(Integer userId, Long cheatSheetId, String reason, String additionalNotes);

	// ===== Report Retrieval =====

	/**
	 * Get all reports
	 * 
	 * @return List of all reports ordered by creation date descending
	 */
	List<Report> getAllReports();

	/**
	 * Get all reports with details (eager loading)
	 * 
	 * @return List of reports with cheat sheet and reporter details
	 */
	List<Report> getAllReportsWithDetails();

	/**
	 * Get a report by its ID
	 * 
	 * @param id The report ID
	 * @return The report, or null if not found
	 */
	Report getReportById(Integer id);

	/**
	 * Get reports by status
	 * 
	 * @param status The status to filter by
	 * @return List of reports with the given status
	 */
	List<Report> getReportsByStatus(String status);

	/**
	 * Get reports by status using enum
	 * 
	 * @param status The ReportStatus enum value
	 * @return List of reports with the given status
	 */
	List<Report> getReportsByStatus(ReportStatus status);

	/**
	 * Get reports by cheat sheet ID
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of reports for the given cheat sheet
	 */
	List<Report> getReportsByCheatSheet(Long cheatSheetId);

	/**
	 * Get reports by user who submitted them
	 * 
	 * @param userId The user ID
	 * @return List of reports submitted by the user
	 */
	List<Report> getReportsByUser(Integer userId);

	/**
	 * Get reports by cheat sheet and status
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param status       The status to filter by
	 * @return List of matching reports
	 */
	List<Report> getReportsByCheatSheetAndStatus(Long cheatSheetId, String status);

	/**
	 * Get reports by multiple statuses
	 * 
	 * @param statuses List of statuses
	 * @return List of reports with any of the given statuses
	 */
	List<Report> getReportsByStatusIn(List<String> statuses);

	/**
	 * Get reports by date range
	 * 
	 * @param startDate The start date
	 * @param endDate   The end date
	 * @return List of reports created in the date range
	 */
	List<Report> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Get recent reports with limit
	 * 
	 * @param limit Maximum number of reports to return
	 * @return List of recent reports
	 */
	List<Report> getRecentReports(int limit);

	/**
	 * Search reports by keyword in reason or notes
	 * 
	 * @param keyword The search keyword
	 * @return List of reports containing the keyword
	 */
	List<Report> searchReports(String keyword);

	/**
	 * Get reports that need admin attention (pending and under_review)
	 * 
	 * @return List of reports requiring attention
	 */
	List<Report> getReportsNeedingAttention();

	/**
	 * Get pending reports older than specified days
	 * 
	 * @param days Number of days
	 * @return List of old pending reports
	 */
	List<Report> getPendingReportsOlderThan(int days);

	// ===== Report Administration =====

	/**
	 * Update report status (admin action)
	 * 
	 * @param reportId The report ID
	 * @param status   The new status
	 * @throws IllegalArgumentException if status is invalid
	 * @throws IllegalStateException    if report is already processed
	 */
	void actionReport(Integer reportId, String status);

	/**
	 * Update report status with admin notes
	 * 
	 * @param reportId   The report ID
	 * @param status     The new status
	 * @param adminNotes Notes from the admin
	 * @param adminId    The ID of the admin performing the action
	 */
	void actionReport(Integer reportId, String status, String adminNotes, Integer adminId);

	/**
	 * Resolve a report
	 * 
	 * @param reportId        The report ID
	 * @param resolutionNotes Notes about the resolution
	 * @param adminId         The ID of the admin resolving the report
	 */
	void resolveReport(Integer reportId, String resolutionNotes, Integer adminId);

	/**
	 * Reject a report
	 * 
	 * @param reportId        The report ID
	 * @param rejectionReason Reason for rejection
	 * @param adminId         The ID of the admin rejecting the report
	 */
	void rejectReport(Integer reportId, String rejectionReason, Integer adminId);

	/**
	 * Bulk update report status
	 * 
	 * @param reportIds List of report IDs
	 * @param status    The new status
	 * @param adminId   The ID of the admin performing the action
	 * @return Number of reports updated
	 */
	int bulkUpdateStatus(List<Integer> reportIds, String status, Integer adminId);

	// ===== Validation and Checks =====

	/**
	 * Check if a user has already reported a specific cheat sheet
	 * 
	 * @param userId       The user ID
	 * @param cheatSheetId The cheat sheet ID
	 * @return true if already reported, false otherwise
	 */
	boolean hasUserReportedSheet(Integer userId, Long cheatSheetId);

	/**
	 * Check if a cheat sheet has pending reports
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return true if there are pending reports, false otherwise
	 */
	boolean hasPendingReports(Long cheatSheetId);

	/**
	 * Get count of reports by status
	 * 
	 * @param status The status to count
	 * @return Count of reports with the given status
	 */
	long countByStatus(String status);

	/**
	 * Get total count of reports
	 * 
	 * @return Total number of reports
	 */
	long getTotalReportCount();

	// ===== Statistics and Analytics =====

	/**
	 * Get report status distribution
	 * 
	 * @return Map with status as key and count as value
	 */
	Map<String, Long> getReportStatusDistribution();

	/**
	 * Get daily report statistics for a date range
	 * 
	 * @param startDate The start date
	 * @param endDate   The end date
	 * @return List of Object arrays [date, count]
	 */
	List<Object[]> getDailyReportStats(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Get report statistics by cheat sheet
	 * 
	 * @return Map with cheat sheet ID as key and report count as value
	 */
	Map<Long, Long> getReportStatsByCheatSheet();

	/**
	 * Get report statistics by user
	 * 
	 * @return Map with user ID as key and report count as value
	 */
	Map<Integer, Long> getReportStatsByUser();

	/**
	 * Get average resolution time for resolved reports
	 * 
	 * @return Average resolution time in hours, or null if no resolved reports
	 */
	Double getAverageResolutionTime();

	/**
	 * Get the most reported cheat sheets
	 * 
	 * @param limit Maximum number to return
	 * @return List of Object arrays [cheatSheetId, reportCount]
	 */
	List<Object[]> getTopReportedSheets(int limit);

	// ===== Report Management =====

	/**
	 * Delete a report (admin only)
	 * 
	 * @param reportId The report ID to delete
	 * @param adminId  The ID of the admin deleting the report
	 */
	void deleteReport(Integer reportId, Integer adminId);

	/**
	 * Delete all reports for a cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param adminId      The ID of the admin performing the action
	 * @return Number of reports deleted
	 */
	int deleteReportsByCheatSheet(Long cheatSheetId, Integer adminId);

	/**
	 * Delete old reports (cleanup)
	 * 
	 * @param daysOld Delete reports older than this many days
	 * @param adminId The ID of the admin performing the cleanup
	 * @return Number of reports deleted
	 */
	int deleteOldReports(int daysOld, Integer adminId);

	/**
	 * Archive resolved reports older than specified days
	 * 
	 * @param daysOld Archive reports resolved older than this many days
	 * @param adminId The ID of the admin performing the archive
	 * @return Number of reports archived
	 */
	int archiveOldResolvedReports(int daysOld, Integer adminId);
}