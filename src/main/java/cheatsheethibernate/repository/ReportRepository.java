package cheatsheethibernate.repository;

import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.Report.ReportStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Report entity operations. Provides CRUD operations
 * and custom query methods for report management.
 */
public interface ReportRepository {

	// ===== Basic CRUD Operations =====

	/**
	 * Save a new report to the database
	 * 
	 * @param report The report entity to save
	 */
	void save(Report report);

	/**
	 * Find all reports
	 * 
	 * @return List of all reports
	 */
	List<Report> findAll();

	/**
	 * Find a report by its ID
	 * 
	 * @param id The report ID
	 * @return The report, or null if not found
	 */
	Report findById(Integer id);

	/**
	 * Find a report by its ID using Optional for better null handling
	 * 
	 * @param id The report ID
	 * @return Optional containing the report if found
	 */
	Optional<Report> findByIdOptional(Integer id);

	/**
	 * Update an existing report
	 * 
	 * @param report The report with updated values
	 */
	void update(Report report);

	/**
	 * Delete a report by its ID
	 * 
	 * @param id The report ID to delete
	 */
	void delete(Integer id);

	/**
	 * Check if a report exists
	 * 
	 * @param id The report ID
	 * @return true if exists, false otherwise
	 */
	boolean exists(Integer id);

	/**
	 * Get the total count of reports
	 * 
	 * @return Total number of reports
	 */
	long count();

	// ===== Status-Based Queries =====

	/**
	 * Find reports by status
	 * 
	 * @param status The status to filter by
	 * @return List of reports with the given status
	 */
	List<Report> findByStatus(String status);

	/**
	 * Find reports by status using enum for type safety
	 * 
	 * @param status The ReportStatus enum value
	 * @return List of reports with the given status
	 */
	default List<Report> findByStatus(ReportStatus status) {
		return findByStatus(status.getValue());
	}

	/**
	 * Find reports by status with pagination support
	 * 
	 * @param status The status to filter by
	 * @param offset The starting position
	 * @param limit  The maximum number of results
	 * @return List of reports with the given status
	 */
	List<Report> findByStatusWithPagination(String status, int offset, int limit);

	/**
	 * Get count of reports by status
	 * 
	 * @param status The status to count
	 * @return Count of reports with the given status
	 */
	long countByStatus(String status);

	/**
	 * Get count of reports by status using enum
	 * 
	 * @param status The ReportStatus enum value
	 * @return Count of reports with the given status
	 */
	default long countByStatus(ReportStatus status) {
		return countByStatus(status.getValue());
	}

	/**
	 * Get status distribution for dashboard statistics
	 * 
	 * @return List of Object arrays [status, count]
	 */
	List<Object[]> getStatusDistribution();

	// ===== Sheet-Related Queries =====

	/**
	 * Find reports by cheat sheet ID
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of reports for the given cheat sheet
	 */
	List<Report> findByCheatSheetId(Long cheatSheetId);

	/**
	 * Find reports by cheat sheet ID with specific status
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @param status       The status to filter by
	 * @return List of reports for the given cheat sheet with the given status
	 */
	List<Report> findByCheatSheetIdAndStatus(Long cheatSheetId, String status);

	/**
	 * Check if a cheat sheet has pending reports
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return true if there are pending reports, false otherwise
	 */
	boolean hasPendingReports(Long cheatSheetId);

	/**
	 * Get all reports for a cheat sheet that are not resolved
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return List of unresolved reports
	 */
	List<Report> findUnresolvedByCheatSheetId(Long cheatSheetId);

	// ===== User-Related Queries =====

	/**
	 * Find reports reported by a specific user
	 * 
	 * @param userId The user ID who reported
	 * @return List of reports by the user
	 */
	List<Report> findByReportedBy(Integer userId);

	/**
	 * Find reports reported by a specific user with status filter
	 * 
	 * @param userId The user ID who reported
	 * @param status The status to filter by
	 * @return List of reports by the user with the given status
	 */
	List<Report> findByReportedByAndStatus(Integer userId, String status);

	/**
	 * Get count of reports by a specific user
	 * 
	 * @param userId The user ID who reported
	 * @return Count of reports by the user
	 */
	long countByReportedBy(Integer userId);

	/**
	 * Find reports resolved by a specific admin
	 * 
	 * @param adminId The admin ID who resolved
	 * @return List of reports resolved by the admin
	 */
	List<Report> findByResolvedBy(Integer adminId);

	// ===== Date-Range Queries =====

	/**
	 * Find reports created in a specific date range
	 * 
	 * @param startDate The start date (inclusive)
	 * @param endDate   The end date (inclusive)
	 * @return List of reports created in the date range
	 */
	List<Report> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Find reports created after a specific date
	 * 
	 * @param date The date to start from
	 * @return List of reports created after the date
	 */
	List<Report> findByCreatedAtAfter(LocalDateTime date);

	/**
	 * Find reports created before a specific date
	 * 
	 * @param date The date to end at
	 * @return List of reports created before the date
	 */
	List<Report> findByCreatedAtBefore(LocalDateTime date);

	/**
	 * Find reports resolved in a specific date range
	 * 
	 * @param startDate The start date (inclusive)
	 * @param endDate   The end date (inclusive)
	 * @return List of reports resolved in the date range
	 */
	List<Report> findByResolvedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	// ===== Advanced Query Methods =====

	/**
	 * Find all reports with eager loading of related entities
	 * 
	 * @return List of reports with cheat sheet and reporter loaded
	 */
	List<Report> findAllWithDetails();

	/**
	 * Find reports by multiple statuses
	 * 
	 * @param statuses List of statuses to filter by
	 * @return List of reports with any of the given statuses
	 */
	List<Report> findByStatusIn(List<String> statuses);

	/**
	 * Get recent reports with limit
	 * 
	 * @param limit Maximum number of reports to return
	 * @return List of recent reports
	 */
	List<Report> findRecentReports(int limit);

	/**
	 * Search reports by keyword in reason or notes
	 * 
	 * @param keyword The search keyword
	 * @return List of reports containing the keyword
	 */
	List<Report> searchByKeyword(String keyword);

	/**
	 * Get reports that need admin attention (pending and under_review)
	 * 
	 * @return List of reports requiring attention
	 */
	List<Report> findReportsNeedingAttention();

	/**
	 * Get reports that have been pending for more than a specified number of days
	 * 
	 * @param days The number of days
	 * @return List of old pending reports
	 */
	List<Report> findPendingReportsOlderThan(int days);

	/**
	 * Bulk update status for multiple reports
	 * 
	 * @param reportIds List of report IDs
	 * @param status    The new status
	 * @return Number of updated reports
	 */
	int bulkUpdateStatus(List<Integer> reportIds, String status);

	/**
	 * Delete all reports for a specific cheat sheet
	 * 
	 * @param cheatSheetId The cheat sheet ID
	 * @return Number of deleted reports
	 */
	int deleteByCheatSheetId(Long cheatSheetId);

	/**
	 * Delete all reports older than a specific date
	 * 
	 * @param date The date threshold
	 * @return Number of deleted reports
	 */
	int deleteOlderThan(LocalDateTime date);

	// ===== Statistics Methods =====

	/**
	 * Get daily report submission statistics for a date range
	 * 
	 * @param startDate The start date
	 * @param endDate   The end date
	 * @return List of Object arrays [date, count]
	 */
	List<Object[]> getDailyReportStats(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Get report statistics by cheat sheet
	 * 
	 * @return List of Object arrays [cheatSheetId, reportCount]
	 */
	List<Object[]> getReportStatsByCheatSheet();

	/**
	 * Get report statistics by user (who submitted reports)
	 * 
	 * @return List of Object arrays [userId, reportCount]
	 */
	List<Object[]> getReportStatsByUser();

	/**
	 * Get average resolution time for resolved reports (in hours)
	 * 
	 * @return Average resolution time in hours
	 */
	Double getAverageResolutionTime();
}