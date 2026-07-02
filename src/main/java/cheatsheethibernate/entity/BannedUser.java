package cheatsheethibernate.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🎯 ဤနေရာတွင် Long userId အစား User Entity ဖြင့် ကွက်တိပြောင်းလဲလိုက်ခြင်းဖြင့် 
    // XML ၏ packagesToScan က auto ရှာတွေ့ပြီး ဆောက်ပေးသွားပါလိမ့်မည်။
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

}