package cheatsheethibernate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cheatsheethibernate.entity.Announcement;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.Tag;
import cheatsheethibernate.service.AdminContentService;

@Controller
public class AdminController {

    @Autowired
    private AdminContentService contentService;

    // =================================================================
    // 📢 ၁။ BROADCAST / ANNOUNCEMENT MANAGEMENT
    // =================================================================

    @GetMapping("/admin/broadcast/panel")
    public String showBroadcastPanel(Model model) {
        model.addAttribute("announcements", contentService.getAllAnnouncements());
        model.addAttribute("newAnnouncement", new Announcement());
        return "admin/broadcast-panel";
    }

    @PostMapping("/admin/broadcast/announcement/save")
    public String saveAnnouncement(@ModelAttribute("newAnnouncement") Announcement ann) {
        contentService.saveAnnouncement(ann);
        return "redirect:/admin/broadcast/panel";
    }

    @GetMapping("/admin/broadcast/announcement/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Long id) {
        contentService.deleteAnnouncement(id);
        return "redirect:/admin/broadcast/panel";
    }

    // =================================================================
    // 📂 ၂။ METADATA (CATEGORY & TAG) MANAGEMENT
    // =================================================================

    @GetMapping("/admin/metadata/panel")
    public String showMetadataPanel(Model model) {
        model.addAttribute("categories", contentService.getAllCategories());
        model.addAttribute("tags", contentService.getAllTags());
        model.addAttribute("newCategory", new Category());
        model.addAttribute("newTag", new Tag());
        return "admin/metadata-panel"; 
    }

    @PostMapping("/admin/metadata/category/save")
    public String saveCategory(@ModelAttribute("newCategory") Category category) {
        contentService.saveCategory(category);
        return "redirect:/admin/metadata/panel";
    }

    @GetMapping("/admin/metadata/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        contentService.deleteCategory(id);
        return "redirect:/admin/metadata/panel";
    }

    @PostMapping("/admin/metadata/tag/save")
    public String saveTag(@ModelAttribute("newTag") Tag tag) {
        contentService.saveTag(tag);
        return "redirect:/admin/metadata/panel";
    }

    @GetMapping("/admin/metadata/tag/delete/{id}")
    public String deleteTag(@PathVariable("id") Integer id) {
        contentService.deleteTag(id);
        return "redirect:/admin/metadata/panel";
    }

    @PostMapping("/admin/metadata/map-tag")
    public String mapTagToSheet(@RequestParam("sheetId") Long sheetId, @RequestParam("tagId") Integer tagId) {
        contentService.assignTagToCheatSheet(sheetId, tagId);
        return "redirect:/admin/metadata/panel";
    }

    // =================================================================
    // 📝 ၃။ TAG CLICK HANDLING (လမ်းကြောင်းကို ခွဲထားပေးသည်)
    // =================================================================
    
    // 🎯 Admin ကနေ Tag နှိပ်ရင် ဒီကိုလာမယ်
    @GetMapping("/admin/cheatsheets/tag/{tagName}")
    public String getAdminSheetsByTag(@PathVariable("tagName") String tagName, Model model) {
        model.addAttribute("sheets", contentService.findSheetsByTagName(tagName));
        model.addAttribute("categoryName", "Admin Tag: " + tagName);
        return "category-list";
    }

    // =================================================================
    // 📝 ၄။ METADATA UPDATE (EDIT) MAPPINGS
    // =================================================================

    @PostMapping("/admin/metadata/category/update/{id}")
    public String updateCategory(@PathVariable("id") int id, @RequestParam("name") String name) {
        Category category = contentService.findCategoryById(id);
        if (category != null) {
            category.setName(name);
            contentService.saveCategory(category);
        }
        return "redirect:/admin/metadata/panel";
    }

    @PostMapping("/admin/metadata/tag/update/{id}")
    public String updateTag(@PathVariable("id") Integer id, @RequestParam("name") String name) {
        Tag tag = contentService.findTagById(id);
        if (tag != null) {
            tag.setName(name);
            contentService.saveTag(tag);
        }
        return "redirect:/admin/metadata/panel";
    }
}