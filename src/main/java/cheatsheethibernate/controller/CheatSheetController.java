package cheatsheethibernate.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.Review;
import cheatsheethibernate.entity.Category;
import cheatsheethibernate.entity.Tag;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.entity.Comment; // 🌟 Comment Entity အား Import လုပ်ခြင်း
import cheatsheethibernate.service.CheatSheetService;
import cheatsheethibernate.service.AdminContentService;

@Controller
@RequestMapping("/cheatsheets")
public class CheatSheetController {

	@Autowired
	private CheatSheetService cheatSheetService;

	@Autowired
	private AdminContentService adminContentService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(CheatSheet.Status.class, new java.beans.PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text != null) {
					try {
						setValue(CheatSheet.Status.valueOf(text.toLowerCase().trim()));
					} catch (IllegalArgumentException e) {
						setValue(CheatSheet.Status.draft); // Default to draft if invalid
					}
				}
			}
		});
	}
	// In CheatSheetController.java

	@GetMapping("/category/{name}")
	public String getCategorySheets(@PathVariable("name") String name,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "12") int size, Model model) {

		List<CheatSheet> allSheets = cheatSheetService.getCheatSheetsByCategory(name);

		// Calculate pagination
		int totalItems = allSheets.size();
		int totalPages = (int) Math.ceil((double) totalItems / size);
		int currentPage = Math.max(1, Math.min(page, totalPages > 0 ? totalPages : 1));

		int fromIndex = (currentPage - 1) * size;
		int toIndex = Math.min(fromIndex + size, totalItems);

		List<CheatSheet> pagedSheets = allSheets.subList(fromIndex, toIndex);

		model.addAttribute("sheets", pagedSheets);
		model.addAttribute("categoryName", name);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("pageSize", size);

		return "category-list";
	}

	@GetMapping("/tag/{tagName}")
	public String getSheetsByTag(@PathVariable("tagName") String tagName, Model model) {
		model.addAttribute("sheets", cheatSheetService.getCheatSheetsByTag(tagName));
		model.addAttribute("categoryName", "Tag: #" + tagName);
		return "category-list";
	}

	@GetMapping("/my-cheatsheets")
	public String getMyCheatSheets(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}
		List<CheatSheet> mySheets = cheatSheetService.getCheatSheetsByUser(loginUser);
		model.addAttribute("sheets", mySheets);
		model.addAttribute("categoryName", "My Personal Cheat Sheets");
		return "my-cheatsheets-list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("cheatSheet", new CheatSheet());
		model.addAttribute("categories", adminContentService.getAllCategories());
		model.addAttribute("tags", adminContentService.getAllTags());
		return "create-form";
	}

	/**
	 * 💾 CheatSheet သိမ်းဆည်းခြင်းနှင့် ပြင်ဆင်ခြင်း
	 */
	// In CheatSheetController.java - updated save method

	@PostMapping("/save")
	public String saveCheatSheet(@ModelAttribute("cheatSheet") CheatSheet formSheet,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "tagIds", required = false) List<Integer> tagIds,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		CheatSheet targetSheet;

		if (formSheet.getId() != null) {
			targetSheet = cheatSheetService.getCheatSheetById(formSheet.getId());
			if (targetSheet == null) {
				redirectAttributes.addFlashAttribute("errorMsg", "Cheat sheet not found.");
				return "redirect:/cheatsheets/my-cheatsheets";
			}
		} else {
			targetSheet = new CheatSheet();
		}

		targetSheet.setTitle(formSheet.getTitle());
		targetSheet.setContent(formSheet.getContent());
		targetSheet.setStatus(formSheet.getStatus());
		targetSheet.setUser(loginUser);

		// ✅ FIX: Handle image upload properly
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String uploadRootPath = request.getServletContext().getRealPath("/uploads/");
				if (uploadRootPath == null) {
					// Fallback for embedded servers
					uploadRootPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
							+ File.separator + "webapp" + File.separator + "uploads";
				}
				File uploadDir = new File(uploadRootPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// Clean filename
				String originalFilename = imageFile.getOriginalFilename();
				String extension = "";
				if (originalFilename != null && originalFilename.contains(".")) {
					extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				}
				String cleanName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8)
						+ extension;

				File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + cleanName);
				imageFile.transferTo(serverFile);

				// ✅ FIX: Store ONLY the filename
				targetSheet.setFileUrl(cleanName);

			} catch (IOException e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("errorMsg", "Failed to upload image: " + e.getMessage());
			}
		}

		// Set category
		if (categoryId != null) {
			Category category = adminContentService.findCategoryById(categoryId);
			if (category != null) {
				targetSheet.setCategory(category);
			}
		}

		// Set tags
		List<Tag> selectedTags = new ArrayList<>();
		if (tagIds != null && !tagIds.isEmpty()) {
			for (Integer tagId : tagIds) {
				Tag tag = adminContentService.findTagById(tagId);
				if (tag != null)
					selectedTags.add(tag);
			}
		}
		targetSheet.setTags(selectedTags);

		try {
			cheatSheetService.saveCheatSheet(targetSheet);
			redirectAttributes.addFlashAttribute("successMsg", "Cheat sheet saved successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Error saving cheat sheet: " + e.getMessage());
		}

		return "redirect:/cheatsheets/my-cheatsheets";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null)
			return "redirect:/login";

		CheatSheet cheatSheet = cheatSheetService.getCheatSheetById(id);
		if (cheatSheet == null || !cheatSheet.getUser().getUserId().equals(loginUser.getUserId())) {
			return "redirect:/cheatsheets/my-cheatsheets";
		}

		model.addAttribute("cheatSheet", cheatSheet);
		model.addAttribute("categories", adminContentService.getAllCategories());
		model.addAttribute("tags", adminContentService.getAllTags());
		return "edit-form";
	}

	@GetMapping("/delete/{id}")
	public String deleteCheatSheet(@PathVariable("id") Long id, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		CheatSheet cheatSheet = cheatSheetService.getCheatSheetById(id);

		if (cheatSheet != null) {
			// 🎯 Object Type (Integer/Long) ကွဲလွဲမှုကို ကာကွယ်ရန် String သို့
			// ပြောင်းလဲစစ်ဆေးခြင်း
			String ownerId = String.valueOf(cheatSheet.getUser().getUserId());
			String currentUserId = String.valueOf(loginUser.getUserId());

			if (ownerId.equals(currentUserId)) {
				try {
					// 🎯 အဆင့်ဆင့် Cascade ချိတ်ဆက်ထားသောကြောင့် Review နှင့် Comment များပါ
					// တစ်ခါတည်း အပြီးပြတ်သွားပါမည်
					cheatSheetService.deleteCheatSheet(cheatSheet);
				} catch (Exception e) {
					// 🎯 Console တွင် Error ကို သေချာမြင်နိုင်ရန် လော့ဂ်ထုတ်ခြင်း
					System.err.println("❌ Error occurred while deleting CheatSheet ID: " + id);
					e.printStackTrace();
					// လိုအပ်ပါက ဤနေရာတွင် RedirectAttributes သုံးပြီး UI ဘက်သို့ Error Message
					// ပြန်သယ်သွားနိုင်ပါသည်
				}
			}
		}
		return "redirect:/cheatsheets/my-cheatsheets";
	}

	/**
	 * 🔍 View Details - CheatSheet အသေးစိတ်နှင့်အတူ Ratings, Reviews နှင့်
	 * Discussion (Comments) ပါ ပြသခြင်း
	 */
	@GetMapping("/view/{id}")
	public String viewCheatSheetDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
		CheatSheet cheatSheet = cheatSheetService.getCheatSheetDetailsById(id);
		if (cheatSheet == null) {
			return "redirect:/dashboard";
		}

		Double avgRating = cheatSheetService.getAverageRating(id);
		List<Review> reviews = cheatSheetService.getReviewsByCheatSheet(id);

		// 🌟 FIX/ADDITION: ပင်မ (parent_id IS NULL) ဖြစ်သော Comment စာရင်းကိုပါ
		// ဆွဲထုတ်ခြင်း
		List<Comment> comments = cheatSheetService.getMainCommentsByCheatSheet(id);

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null) {
			Review currentReview = null;
			for (Review r : reviews) {
				if (r.getUser().getUserId().equals(loginUser.getUserId())) {
					currentReview = r;
					break;
				}
			}
			model.addAttribute("currentReview", currentReview);
		}

		model.addAttribute("sheet", cheatSheet);
		model.addAttribute("avgRating", Math.round(avgRating * 10) / 10.0);
		model.addAttribute("reviews", reviews);
		model.addAttribute("comments", comments); // 🌟 JSP သို့ Comments List ပေးပို့ခြင်း

		return "cheatsheet-detail";
	}

	/**
	 * 🌟 Submit Review - အသုံးပြုသူတစ်ဦးလျှင် တစ်ကြိမ်သာ ပေးခွင့်ပြုပြီး
	 * အဟောင်းရှိပါက ပြင်ဆင်ပေးမည့် Logic
	 */
	@PostMapping("/view/{id}/review")
	public String submitReview(@PathVariable("id") Long id, @RequestParam("rating") Integer rating,
			@RequestParam("reviewText") String reviewText, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		cheatSheetService.addOrUpdateReview(id, loginUser, rating, reviewText);
		return "redirect:/cheatsheets/view/" + id;
	}

	/**
	 * 💬 ဖြည့်စွက်ချက်- Comment သို့မဟုတ် Reply မက်ဆေ့ခ်ျများအား သိမ်းဆည်းပေးမည့်
	 * Logic
	 */
	@PostMapping("/view/{id}/comment")
	public String submitComment(@PathVariable("id") Long id, @RequestParam("content") String content,
			@RequestParam(value = "parentId", required = false) Integer parentId, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// Service ထံသို့ CheatSheet ID, လက်ရှိ User, စာသား နှင့် Parent ID (ရှိလျှင်)
		// ပေးပို့သိမ်းဆည်းခြင်း
		cheatSheetService.addComment(id, loginUser, content, parentId);

		return "redirect:/cheatsheets/view/" + id;
	}
}