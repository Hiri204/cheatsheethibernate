package cheatsheethibernate.controller;

import cheatsheethibernate.entity.User;
import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.service.UserService;
import cheatsheethibernate.service.BannedUserService;
import cheatsheethibernate.service.CheatSheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BannedUserService bannedUserService;

	@Autowired
	private CheatSheetService cheatSheetService;

	@GetMapping("/list")
	public String listUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("bannedUsers", bannedUserService.getAllActiveBans());
		return "admin/user-management";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user) {
		userService.updateUser(user);
		return "redirect:/user/list";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id) {
		userService.deleteUser(id);
		return "redirect:/user/list";
	}

	/**
	 * User Profile View - Show other user's profile
	 */
	@GetMapping("/profile/{userId}")
	public String viewUserProfile(@PathVariable("userId") Long userId, HttpSession session, Model model) {
		User profileUser = userService.getById(userId.intValue());
		if (profileUser == null) {
			return "redirect:/dashboard";
		}

		// Get user's published sheets (only published ones)
		List<CheatSheet> userSheets = cheatSheetService.getCheatSheetsByUser(profileUser);
		// Filter only published sheets
		userSheets.removeIf(sheet -> !"published".equals(sheet.getStatus().toString()));

		model.addAttribute("profileUser", profileUser);
		model.addAttribute("userSheets", userSheets);
		model.addAttribute("sheetCount", userSheets.size());

		// Get follower and following counts
		long followerCount = userService.countFollowers(userId);
		long followingCount = userService.countFollowing(userId);
		model.addAttribute("followerCount", followerCount);
		model.addAttribute("followingCount", followingCount);

		// Get current logged in user for follow status
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null) {
			boolean isFollowing = userService.isFollowing(loginUser.getUserId().longValue(), userId);
			model.addAttribute("isFollowing", isFollowing);
		}

		return "user-profile";
	}

	@PostMapping("/follow/{userId}")
	@ResponseBody
	public FollowResponse followUser(@PathVariable("userId") Long userId, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return new FollowResponse(false, "Please login first");
		}

		if (loginUser.getUserId().equals(userId)) {
			return new FollowResponse(false, "You cannot follow yourself");
		}

		try {
			boolean isNowFollowing;
			Long currentUserId = loginUser.getUserId().longValue();

			if (userService.isFollowing(currentUserId, userId)) {
				userService.unfollowUser(currentUserId, userId);
				isNowFollowing = false;
			} else {
				userService.followUser(currentUserId, userId);
				isNowFollowing = true;
			}

			String message = isNowFollowing ? "Successfully followed!" : "Unfollowed successfully!";
			return new FollowResponse(true, message, isNowFollowing);
		} catch (Exception e) {
			e.printStackTrace();
			return new FollowResponse(false, "Error: " + e.getMessage());
		}
	}

	@GetMapping("/follow/status/{userId}")
	@ResponseBody
	public FollowStatusResponse getFollowStatus(@PathVariable("userId") Long userId, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return new FollowStatusResponse(false);
		}

		boolean isFollowing = userService.isFollowing(loginUser.getUserId().longValue(), userId);
		return new FollowStatusResponse(isFollowing);
	}

	// Response classes for AJAX
	static class FollowResponse {
		private boolean success;
		private String message;
		private boolean following;

		public FollowResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		public FollowResponse(boolean success, String message, boolean following) {
			this.success = success;
			this.message = message;
			this.following = following;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isFollowing() {
			return following;
		}

		public void setFollowing(boolean following) {
			this.following = following;
		}
	}

	static class FollowStatusResponse {
		private boolean following;

		public FollowStatusResponse(boolean following) {
			this.following = following;
		}

		public boolean isFollowing() {
			return following;
		}

		public void setFollowing(boolean following) {
			this.following = following;
		}
	}
}