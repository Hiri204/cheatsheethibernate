package cheatsheethibernate.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    // 🎯 ဤအပိုင်းကို ထည့်ပေးပါ
    // Category တစ်ခုတွင် CheatSheet များစွာရှိနိုင်သည်
    // CascadeType.ALL ကြောင့် Category ကိုဖျက်လျှင် ဆက်စပ်နေသော Sheet များကိုပါ ဖျက်ပေးမည်
    @ToString.Exclude // StackOverflowError မတက်စေရန်
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CheatSheet> cheatSheets = new ArrayList<>();
}