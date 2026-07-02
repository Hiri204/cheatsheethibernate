package cheatsheethibernate.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheatsheet_id", nullable = false)
    private CheatSheet cheatSheet;

    // 🌟 Self-Referencing: မိခင် Comment ကို ပြန်ညွှန်းခြင်း (parent_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    // 🌟 မိခင်အောက်က Replies များကို အချိန်အလိုက် စီပြီး တစ်ခါတည်း ဆွဲထုတ်ရန်
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Comment> replies = new ArrayList<>();

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Generated(GenerationTime.INSERT)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Comment() {}
}