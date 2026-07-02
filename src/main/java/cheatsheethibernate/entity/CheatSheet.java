package cheatsheethibernate.entity;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "cheatsheets")
@Data
public class CheatSheet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cheatsheet_id")
	private Long id;

	@Column(nullable = false, length = 150)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "file_url", length = 255)
	private String fileUrl;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = true)
	private Category category;

	@ToString.Exclude
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "cheatsheet_tags", joinColumns = @JoinColumn(name = "cheatsheet_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<>();

	// Cascade ALL for reviews - when cheat sheet is deleted, reviews are also
	// deleted
	@ToString.Exclude
	@OneToMany(mappedBy = "cheatSheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Review> reviews = new ArrayList<>();

	// Cascade ALL for comments - when cheat sheet is deleted, comments are also
	// deleted
	@ToString.Exclude
	@OneToMany(mappedBy = "cheatSheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "ENUM('draft', 'published', 'archived', 'banned') default 'draft'")
	private Status status = Status.draft;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at", nullable = true)
	private LocalDateTime deletedAt;

	// ===== Ban Fields =====
	@Column(name = "ban_reason", nullable = true, length = 500)
	private String banReason;

	@Column(name = "banned_at", nullable = true)
	private LocalDateTime bannedAt;

	@Column(name = "ban_expires_at", nullable = true)
	private LocalDateTime banExpiresAt;

	@Column(name = "banned_by", nullable = true)
	private Integer bannedBy;

	// ===== Interaction Counts =====
	@Column(name = "like_count", nullable = false)
	private Integer likeCount = 0;

	@Column(name = "comment_count", nullable = false)
	private Integer commentCount = 0;

	@Column(name = "bookmark_count", nullable = false)
	private Integer bookmarkCount = 0;

	@Column(name = "view_count", nullable = false)
	private Integer viewCount = 0;

	@Column(name = "rating_sum", nullable = false)
	private Integer ratingSum = 0;

	@Column(name = "rating_count", nullable = false)
	private Integer ratingCount = 0;

	public enum Status {
		draft, published, archived, banned
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		initializeCounts();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	// ===== Initialization Methods =====

	private void initializeCounts() {
		if (this.likeCount == null)
			this.likeCount = 0;
		if (this.commentCount == null)
			this.commentCount = 0;
		if (this.bookmarkCount == null)
			this.bookmarkCount = 0;
		if (this.viewCount == null)
			this.viewCount = 0;
		if (this.ratingSum == null)
			this.ratingSum = 0;
		if (this.ratingCount == null)
			this.ratingCount = 0;
	}

	// ===== Status Check Methods =====

	public boolean isDraft() {
		return Status.draft.equals(this.status);
	}

	public boolean isPublished() {
		return Status.published.equals(this.status);
	}

	public boolean isArchived() {
		return Status.archived.equals(this.status);
	}

	public boolean isBanned() {
		return Status.banned.equals(this.status);
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}

	public boolean isActive() {
		return !isDeleted() && isPublished();
	}

	public boolean isPublic() {
		return isPublished() && !isDeleted();
	}

	// ===== Ban Management Methods =====

	public boolean isBanExpired() {
		if (this.banExpiresAt == null)
			return false;
		return LocalDateTime.now().isAfter(this.banExpiresAt);
	}

	public boolean isPermanentlyBanned() {
		return this.banExpiresAt == null && Status.banned.equals(this.status);
	}

	public boolean isTemporarilyBanned() {
		return this.banExpiresAt != null && Status.banned.equals(this.status);
	}

	public boolean isBanActive() {
		return isBanned() && !isBanExpired();
	}

	/**
	 * Ban this cheat sheet
	 * 
	 * @param reason    The reason for banning
	 * @param expiresAt The expiry date (null for permanent)
	 * @param adminId   The ID of the admin who banned it
	 */
	public void ban(String reason, LocalDateTime expiresAt, Integer adminId) {
		this.status = Status.banned;
		this.banReason = reason;
		this.bannedAt = LocalDateTime.now();
		this.banExpiresAt = expiresAt;
		this.bannedBy = adminId;
	}

	/**
	 * Unban this cheat sheet - restore to published status
	 */
	public void unban() {
		this.status = Status.published;
		this.banReason = null;
		this.bannedAt = null;
		this.banExpiresAt = null;
		this.bannedBy = null;
	}

	// ===== Soft Delete Methods =====

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.status = Status.archived;
	}

	public void restore() {
		this.deletedAt = null;
		this.status = Status.published;
	}

	// ===== View/Interaction Methods =====

	public void incrementViewCount() {
		if (this.viewCount == null)
			this.viewCount = 0;
		this.viewCount++;
	}

	public void incrementLikeCount() {
		if (this.likeCount == null)
			this.likeCount = 0;
		this.likeCount++;
	}

	public void decrementLikeCount() {
		if (this.likeCount == null || this.likeCount <= 0) {
			this.likeCount = 0;
		} else {
			this.likeCount--;
		}
	}

	public void incrementCommentCount() {
		if (this.commentCount == null)
			this.commentCount = 0;
		this.commentCount++;
	}

	public void decrementCommentCount() {
		if (this.commentCount == null || this.commentCount <= 0) {
			this.commentCount = 0;
		} else {
			this.commentCount--;
		}
	}

	public void incrementBookmarkCount() {
		if (this.bookmarkCount == null)
			this.bookmarkCount = 0;
		this.bookmarkCount++;
	}

	public void decrementBookmarkCount() {
		if (this.bookmarkCount == null || this.bookmarkCount <= 0) {
			this.bookmarkCount = 0;
		} else {
			this.bookmarkCount--;
		}
	}

	// ===== Rating Methods =====

	public void addRating(int rating) {
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}
		if (this.ratingSum == null)
			this.ratingSum = 0;
		if (this.ratingCount == null)
			this.ratingCount = 0;
		this.ratingSum += rating;
		this.ratingCount++;
	}

	public void removeRating(int rating) {
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}
		if (this.ratingSum == null || this.ratingCount == null || this.ratingCount <= 0) {
			this.ratingSum = 0;
			this.ratingCount = 0;
			return;
		}
		this.ratingSum -= rating;
		this.ratingCount--;
		if (this.ratingCount < 0)
			this.ratingCount = 0;
		if (this.ratingSum < 0)
			this.ratingSum = 0;
	}

	public double getAverageRating() {
		if (ratingCount == null || ratingCount == 0) {
			return 0.0;
		}
		return (double) ratingSum / ratingCount;
	}

	public String getFormattedAverageRating() {
		return String.format("%.1f", getAverageRating());
	}

	public String getRatingStars() {
		double avg = getAverageRating();
		int fullStars = (int) avg;
		boolean hasHalfStar = (avg - fullStars) >= 0.5;
		StringBuilder stars = new StringBuilder();
		for (int i = 0; i < fullStars; i++) {
			stars.append("★");
		}
		if (hasHalfStar) {
			stars.append("½");
		}
		int emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
		for (int i = 0; i < emptyStars; i++) {
			stars.append("☆");
		}
		return stars.toString();
	}

	// ===== Formatted Date Methods =====

	public String getFormattedCreatedDate() {
		if (this.createdAt == null)
			return "";
		return this.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public String getFormattedUpdatedDate() {
		if (this.updatedAt == null)
			return "";
		return this.updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public String getFormattedDeletedDate() {
		if (this.deletedAt == null)
			return "";
		return this.deletedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public String getFormattedBannedDate() {
		if (this.bannedAt == null)
			return "";
		return this.bannedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public String getFormattedBanExpiryDate() {
		if (this.banExpiresAt == null)
			return "Permanent";
		return this.banExpiresAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	// ===== Tag Management Methods =====

	public void addTag(Tag tag) {
		if (tag == null)
			return;
		if (this.tags == null)
			this.tags = new ArrayList<>();
		if (!this.tags.contains(tag)) {
			this.tags.add(tag);
		}
	}

	public void removeTag(Tag tag) {
		if (tag == null || this.tags == null)
			return;
		this.tags.remove(tag);
	}

	public boolean hasTag(Tag tag) {
		return this.tags != null && this.tags.contains(tag);
	}

	// ===== Helper Methods for Strings =====

	public String getContentPreview(int maxLength) {
		if (this.content == null)
			return "";
		if (this.content.length() <= maxLength)
			return this.content;
		return this.content.substring(0, maxLength) + "...";
	}

	public String getContentPreview() {
		return getContentPreview(120);
	}

	public String getTitleWithStatus() {
		String title = this.title != null ? this.title : "Untitled";
		if (isBanned()) {
			return title + " [BANNED]";
		}
		if (isArchived()) {
			return title + " [ARCHIVED]";
		}
		if (isDraft()) {
			return title + " [DRAFT]";
		}
		return title;
	}

	// ===== Override toString for better logging =====

	@Override
	public String toString() {
		return String.format("CheatSheet{id=%d, title='%s', status=%s, user=%s, createdAt=%s}", id, title, status,
				user != null ? user.getUsername() : "null", createdAt);
	}
}