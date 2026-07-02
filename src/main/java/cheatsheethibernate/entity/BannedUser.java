package cheatsheethibernate.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "banned_users")
public class BannedUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "banned_by", nullable = false)
	private Long bannedBy;

	@Column(columnDefinition = "TEXT")
	private String reason;

	@Column(name = "banned_at", nullable = false, updatable = false)
	private LocalDateTime bannedAt = LocalDateTime.now();

	@Column(name = "expires_at")
	private LocalDateTime expiresAt;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.bannedAt == null) {
			this.bannedAt = LocalDateTime.now();
		}
	}

	// Helper methods
	public boolean isActive() {
		return this.expiresAt == null || this.expiresAt.isAfter(LocalDateTime.now());
	}

	public boolean isExpired() {
		return this.expiresAt != null && this.expiresAt.isBefore(LocalDateTime.now());
	}

	public boolean isPermanent() {
		return this.expiresAt == null;
	}

	public String getDurationText() {
		if (isPermanent()) {
			return "Permanent";
		}
		return "Until " + expiresAt.toString();
	}
}