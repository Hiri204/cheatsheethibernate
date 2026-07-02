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

    // 🎯 FIX 1: CheatSheet ကို ဖျက်လျှင် ၎င်းနှင့်သက်ဆိုင်သော Review များပါ တစ်ခါတည်း အပြီးပြတ်စေရန် Cascade ALL ထည့်သွင်းခြင်း
    @ToString.Exclude
    @OneToMany(mappedBy = "cheatSheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // 🎯 FIX 2: CheatSheet ကို ဖျက်လျှင် ၎င်းနှင့်သက်ဆိုင်သော Comment များပါ တစ်ခါတည်း အပြီးပြတ်စေရန် Cascade ALL ထည့်သွင်းခြင်း
    @ToString.Exclude
    @OneToMany(mappedBy = "cheatSheet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('draft', 'published', 'archived') default 'draft'")
    private Status status = Status.draft;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 🎯 REMOVED: deleted_at column ကို Hard Delete သုံးမည်ဖြစ်၍ ဖယ်ထုတ်လိုက်ပါသည်

    public enum Status {
        draft, published, archived
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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
}