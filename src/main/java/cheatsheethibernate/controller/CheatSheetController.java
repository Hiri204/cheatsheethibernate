package cheatsheethibernate.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

	@GetMapping("/category/{name}")
	public String getCategorySheets(@PathVariable("name") String name, Model model) {
		model.addAttribute("sheets", cheatSheetService.getCheatSheetsByCategory(name));
		model.addAttribute("categoryName", name);
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
	@PostMapping("/save")
	public String saveCheatSheet(@ModelAttribute("cheatSheet") CheatSheet formSheet,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "tagIds", required = false) List<Integer> tagIds,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session,
			HttpServletRequest request) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		CheatSheet targetSheet;

		if (formSheet.getId() != null) {
			targetSheet = cheatSheetService.getCheatSheetById(formSheet.getId());
			if (targetSheet == null) {
				return "redirect:/cheatsheets/my-cheatsheets";
			}
		} else {
			targetSheet = new CheatSheet();
		}

		targetSheet.setTitle(formSheet.getTitle());
		targetSheet.setContent(formSheet.getContent());
		targetSheet.setStatus(formSheet.getStatus());
		targetSheet.setUser(loginUser);

		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String uploadRootPath = request.getServletContext().getRealPath("/uploads/");
				File uploadDir = new File(uploadRootPath);
				if (!uploadDir.exists())
					uploadDir.mkdirs();

				String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
				File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + fileName);
				imageFile.transferTo(serverFile);

				targetSheet.setFileUrl("/uploads/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (categoryId != null) {
			Category category = adminContentService.findCategoryById(categoryId);
			if (category != null) {
				targetSheet.setCategory(category);
			}
		} else if (formSheet.getCategory() != null && formSheet.getCategory().getCategoryId() != null) {
			Category category = adminContentService.findCategoryById(formSheet.getCategory().getCategoryId());
			if (category != null) {
				targetSheet.setCategory(category);
			}
		}

		List<Tag> selectedTags = new ArrayList<>();
		if (tagIds != null && !tagIds.isEmpty()) {
			for (Integer tagId : tagIds) {
				Tag tag = adminContentService.findTagById(tagId);
				if (tag != null)
					selectedTags.add(tag);
			}
		}
		targetSheet.setTags(selectedTags);

		cheatSheetService.saveCheatSheet(targetSheet);

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
	        // 🎯 Object Type (Integer/Long) ကွဲလွဲမှုကို ကာကွယ်ရန် String သို့ ပြောင်းလဲစစ်ဆေးခြင်း
	        String ownerId = String.valueOf(cheatSheet.getUser().getUserId());
	        String currentUserId = String.valueOf(loginUser.getUserId());
	        
	        if (ownerId.equals(currentUserId)) {
	            try {
	                // 🎯 အဆင့်ဆင့် Cascade ချိတ်ဆက်ထားသောကြောင့် Review နှင့် Comment များပါ တစ်ခါတည်း အပြီးပြတ်သွားပါမည်
	                cheatSheetService.deleteCheatSheet(cheatSheet);
	            } catch (Exception e) {
	                // 🎯 Console တွင် Error ကို သေချာမြင်နိုင်ရန် လော့ဂ်ထုတ်ခြင်း
	                System.err.println("❌ Error occurred while deleting CheatSheet ID: " + id);
	                e.printStackTrace();
	                // လိုအပ်ပါက ဤနေရာတွင် RedirectAttributes သုံးပြီး UI ဘက်သို့ Error Message ပြန်သယ်သွားနိုင်ပါသည်
	            }
	        }
	    }
	    return "redirect:/cheatsheets/my-cheatsheets";
	}

	/**
	 * 🔍 View Details - CheatSheet အသေးစိတ်နှင့်အတူ Ratings, Reviews နှင့် Discussion (Comments) ပါ ပြသခြင်း
	 */
	@GetMapping("/view/{id}")
	public String viewCheatSheetDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
		CheatSheet cheatSheet = cheatSheetService.getCheatSheetDetailsById(id);
		if (cheatSheet == null) {
			return "redirect:/dashboard"; 
		}
		
		Double avgRating = cheatSheetService.getAverageRating(id);
		List<Review> reviews = cheatSheetService.getReviewsByCheatSheet(id);
		
		// 🌟 FIX/ADDITION: ပင်မ (parent_id IS NULL) ဖြစ်သော Comment စာရင်းကိုပါ ဆွဲထုတ်ခြင်း
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
	 * 🌟 Submit Review - အသုံးပြုသူတစ်ဦးလျှင် တစ်ကြိမ်သာ ပေးခွင့်ပြုပြီး အဟောင်းရှိပါက ပြင်ဆင်ပေးမည့် Logic
	 */
	@PostMapping("/view/{id}/review")
	public String submitReview(@PathVariable("id") Long id,
							   @RequestParam("rating") Integer rating,
							   @RequestParam("reviewText") String reviewText,
							   HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login"; 
		}
		
		cheatSheetService.addOrUpdateReview(id, loginUser, rating, reviewText);
		return "redirect:/cheatsheets/view/" + id; 
	}

	/**
	 * 💬 ဖြည့်စွက်ချက်- Comment သို့မဟုတ် Reply မက်ဆေ့ခ်ျများအား သိမ်းဆည်းပေးမည့် Logic
	 */
	@PostMapping("/view/{id}/comment")
	public String submitComment(@PathVariable("id") Long id,
								@RequestParam("content") String content,
								@RequestParam(value = "parentId", required = false) Integer parentId,
								HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}
		
		// Service ထံသို့ CheatSheet ID, လက်ရှိ User, စာသား နှင့် Parent ID (ရှိလျှင်) ပေးပို့သိမ်းဆည်းခြင်း
		cheatSheetService.addComment(id, loginUser, content, parentId);
		
		return "redirect:/cheatsheets/view/" + id;
	}
}