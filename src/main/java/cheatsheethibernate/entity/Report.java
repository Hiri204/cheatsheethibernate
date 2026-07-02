package cheatsheethibernate.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reports", indexes = { @Index(name = "idx_report_status", columnList = "status"),
		@Index(name = "idx_report_cheatsheet", columnList = "cheatsheet_id"),
		@Index(name = "idx_report_reported_by", columnList = "reported_by"),
		@Index(name = "idx_report_created_at", columnList = "created_at") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cheatsheet_id", nullable = false)
	private CheatSheet cheatSheet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_by", nullable = false)
	private User reportedBy;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String reason;

	@Column(name = "status", nullable = false)
	private String status = ReportStatus.PENDING.getValue();

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "resolved_at")
	private LocalDateTime resolvedAt;

	@Column(name = "resolution_notes", columnDefinition = "TEXT")
	private String resolutionNotes;

	@Column(name = "resolved_by")
	private Integer resolvedBy;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		if (this.status == null) {
			this.status = ReportStatus.PENDING.getValue();
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
		// If status is being changed to resolved, set resolvedAt
		if (ReportStatus.RESOLVED.getValue().equals(this.status) && this.resolvedAt == null) {
			this.resolvedAt = LocalDateTime.now();
		}
		// If status is being changed back from resolved, clear resolvedAt
		if (!ReportStatus.RESOLVED.getValue().equals(this.status)) {
			this.resolvedAt = null;
		}
	}

	// Helper methods for status management
	public boolean isPending() {
		return ReportStatus.PENDING.getValue().equals(this.status);
	}

	public boolean isResolved() {
		return ReportStatus.RESOLVED.getValue().equals(this.status);
	}

	public boolean isRejected() {
		return ReportStatus.REJECTED.getValue().equals(this.status);
	}

	public void setPending() {
		this.status = ReportStatus.PENDING.getValue();
		this.resolvedAt = null;
	}

	public void setResolved(String notes, Integer adminId) {
		this.status = ReportStatus.RESOLVED.getValue();
		this.resolutionNotes = notes;
		this.resolvedBy = adminId;
		this.resolvedAt = LocalDateTime.now();
	}

	public void setRejected(String notes, Integer adminId) {
		this.status = ReportStatus.REJECTED.getValue();
		this.resolutionNotes = notes;
		this.resolvedBy = adminId;
		this.resolvedAt = LocalDateTime.now();
	}

	/**
	 * Inner enum for report status constants
	 */
	public enum ReportStatus {
		PENDING("pending"), RESOLVED("resolved"), REJECTED("rejected"), UNDER_REVIEW("under_review");

		private final String value;

		ReportStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static ReportStatus fromValue(String value) {
			for (ReportStatus status : ReportStatus.values()) {
				if (status.value.equals(value)) {
					return status;
				}
			}
			throw new IllegalArgumentException("Unknown status: " + value);
		}
	}

	// Override toString for better logging
	@Override
	public String toString() {
		return String.format("Report{id=%d, status='%s', reason='%s', createdAt=%s}", id, status, reason, createdAt);
	}

	// Builder pattern for easier object creation
	public static class ReportBuilder {
		private Report report;

		public ReportBuilder() {
			this.report = new Report();
		}

		public ReportBuilder cheatSheet(CheatSheet cheatSheet) {
			report.setCheatSheet(cheatSheet);
			return this;
		}

		public ReportBuilder reportedBy(User reportedBy) {
			report.setReportedBy(reportedBy);
			return this;
		}

		public ReportBuilder reason(String reason) {
			report.setReason(reason);
			return this;
		}

		public ReportBuilder status(String status) {
			report.setStatus(status);
			return this;
		}

		public Report build() {
			return report;
		}
	}

	public static ReportBuilder builder() {
		return new ReportBuilder();
	}
}