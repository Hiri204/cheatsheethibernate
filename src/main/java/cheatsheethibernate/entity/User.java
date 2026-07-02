package cheatsheethibernate.entity;

import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "phone", unique = true, length = 20)
	private String phone;

	@Column(name = "role", columnDefinition = "ENUM('user', 'admin') DEFAULT 'user'")
	private String role;

	@Column(name = "profile_img", length = 255)
	private String profileImg;

	@Column(name = "status", columnDefinition = "ENUM('active', 'suspended', 'inactive') DEFAULT 'active'")
	private String status;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	@Generated(GenerationTime.INSERT)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Generated(GenerationTime.ALWAYS)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
	private LocalDateTime deletedAt;

	// ===== Password Reset Fields =====
	@Column(name = "reset_code", length = 10)
	private String resetCode;

	@Column(name = "reset_code_expiry")
	private LocalDateTime resetCodeExpiry;

	// ===== Suspension Fields =====
	@Column(name = "suspension_reason", length = 500)
	private String suspensionReason;

	@Column(name = "suspended_at")
	private LocalDateTime suspendedAt;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	// ===== Transient Fields (not persisted in database) =====
	@Transient
	private Long followerCount;

	@Transient
	private Long followingCount;

	// ===== Constructors =====
	public User() {
	}

	public User(String username, String email, String passwordHash) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.status = "active";
		this.role = "user";
	}

	// ===== Getters and Setters =====
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getResetCode() {
		return resetCode;
	}

	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}

	public LocalDateTime getResetCodeExpiry() {
		return resetCodeExpiry;
	}

	public void setResetCodeExpiry(LocalDateTime resetCodeExpiry) {
		this.resetCodeExpiry = resetCodeExpiry;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public LocalDateTime getSuspendedAt() {
		return suspendedAt;
	}

	public void setSuspendedAt(LocalDateTime suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Long getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Long followerCount) {
		this.followerCount = followerCount;
	}

	public Long getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(Long followingCount) {
		this.followingCount = followingCount;
	}

	// ===== Helper Methods =====
	public boolean isActive() {
		return "active".equals(this.status);
	}

	public boolean isSuspended() {
		return "suspended".equals(this.status);
	}

	public boolean isAdmin() {
		return "admin".equals(this.role);
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}

	public boolean hasResetCode() {
		return this.resetCode != null && this.resetCodeExpiry != null
				&& this.resetCodeExpiry.isAfter(LocalDateTime.now());
	}

	@Override
	public String toString() {
		return String.format("User{userId=%d, username='%s', email='%s', role='%s', status='%s'}", userId, username,
				email, role, status);
	}
}