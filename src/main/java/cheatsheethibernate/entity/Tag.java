package cheatsheethibernate.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tags")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id") // 🎯 Diagram အတိုင်း PK ညွှန်ခြင်း
    private Integer tagId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String slug;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<CheatSheet> cheatSheets;
}