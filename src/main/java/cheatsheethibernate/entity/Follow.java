package cheatsheethibernate.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "follows")
@Data
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long followId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "follower_id", nullable = false)
	private User follower;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "following_id", nullable = false)
	private User following;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}