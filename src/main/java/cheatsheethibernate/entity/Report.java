package cheatsheethibernate.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "reports")
@Data
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cheatsheet_id", nullable = false)
	private CheatSheet cheatSheet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reported_by", nullable = false)
	private User reportedBy;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String reason;

	@Column(name = "status", nullable = false)
	private String status = "pending";

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}