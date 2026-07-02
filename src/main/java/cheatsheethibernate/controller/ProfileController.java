package cheatsheethibernate.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.UserService;
import cheatsheethibernate.service.FileUploadService;
import cheatsheethibernate.service.CheatSheetService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private CheatSheetService cheatSheetService;

	@GetMapping("/view")
	public String viewProfile(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// Get latest user data from database
		User user = userService.getById(loginUser.getUserId());
		if (user == null) {
			session.invalidate();
			return "redirect:/login";
		}

		// Add user data to model
		model.addAttribute("user", user);

		// Add statistics using service methods
		List<CheatSheet> userSheets = cheatSheetService.getCheatSheetsByUser(user);
		model.addAttribute("sheetCount", userSheets != null ? userSheets.size() : 0);

		// Use service methods for follower/following counts
		long followerCount = userService.countFollowers(user.getUserId());
		long followingCount = userService.countFollowing(user.getUserId());
		model.addAttribute("followerCount", followerCount);
		model.addAttribute("followingCount", followingCount);

		return "profile";
	}

	@PostMapping("/update")
	public String updateProfile(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage, HttpSession session,
			HttpServletRequest request, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		User user = userService.getById(loginUser.getUserId());
		if (user == null) {
			session.invalidate();
			return "redirect:/login";
		}

		try {
			// Update basic information
			user.setUsername(username);
			user.setEmail(email);

			// Handle phone number - prevent duplicate entry issues
			if (phone == null || phone.trim().isEmpty()) {
				user.setPhone(null);
			} else {
				user.setPhone(phone.trim());
			}

			// Update password if provided
			if (password != null && !password.trim().isEmpty()) {
				if (password.length() < 6) {
					model.addAttribute("errorMsg", "Password must be at least 6 characters");
					return "redirect:/profile/view?error=true";
				}
				user.setPasswordHash(password);
			}

			// Handle profile image upload
			if (profileImage != null && !profileImage.isEmpty()) {
				// Validate file size (max 5MB)
				if (profileImage.getSize() > 5 * 1024 * 1024) {
					model.addAttribute("errorMsg", "Image size exceeds 5MB limit");
					return "redirect:/profile/view?error=true";
				}
				String uploadedFileName = fileUploadService.uploadProfileImage(profileImage, request);
				if (uploadedFileName != null) {
					user.setProfileImg(uploadedFileName);
				}
			}

			// Save updates
			userService.updateUser(user);

			// Update session with new user data
			session.setAttribute("loginUser", user);
			session.setAttribute("successMsg", "Profile updated successfully!");

			return "redirect:/profile/view?success=true";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Profile update failed: " + e.getMessage());
			return "redirect:/profile/view?error=true";
		}
	}

	@GetMapping("/delete-account")
	public String deleteAccount(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", loginUser);
		return "delete-account-confirmation";
	}

	@PostMapping("/delete-account")
	public String confirmDeleteAccount(HttpSession session,
			@RequestParam(value = "confirm", required = false) Boolean confirm) {

		if (confirm == null || !confirm) {
			return "redirect:/profile/view";
		}

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		try {
			userService.deleteUser(loginUser.getUserId());
			session.invalidate();
			return "redirect:/login?accountDeleted=true";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/profile/view?error=deleteFailed";
		}
	}
}