package cheatsheethibernate.controller;

import cheatsheethibernate.entity.User;
import cheatsheethibernate.entity.CheatSheet;
import cheatsheethibernate.service.UserService;
import cheatsheethibernate.service.BannedUserService;
import cheatsheethibernate.service.CheatSheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BannedUserService bannedUserService;

	@Autowired
	private CheatSheetService cheatSheetService;

	/**
	 * Admin panel - List all users with their ban status URL: GET /user/list
	 */
	@GetMapping("/list")
	public String listUsers(Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return "redirect:/login";
		}

		try {
			List<User> users = userService.getAllUsers();
			model.addAttribute("users", users);
			model.addAttribute("bannedUsers", bannedUserService.getAllActiveBans());
			model.addAttribute("userCount", users.size());
			model.addAttribute("activeUserCount", users.stream().filter(u -> "active".equals(u.getStatus())).count());
			model.addAttribute("suspendedUserCount",
					users.stream().filter(u -> "suspended".equals(u.getStatus())).count());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Error loading users: " + e.getMessage());
		}

		return "admin/user-management";
	}

	/**
	 * Admin action - Update user details URL: POST /user/save
	 */
	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return "redirect:/login";
		}

		try {
			userService.updateUser(user);
			session.setAttribute("successMsg", "User updated successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMsg", "Error updating user: " + e.getMessage());
		}
		return "redirect:/user/list";
	}

	/**
	 * Admin action - Delete user (with confirmation) URL: GET /user/delete/{id}
	 */
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return "redirect:/login";
		}

		try {
			userService.deleteUser(id);
			session.setAttribute("successMsg", "User deleted successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMsg", "Error deleting user: " + e.getMessage());
		}
		return "redirect:/user/list";
	}

	/**
	 * Admin action - Suspend/Unsuspend user URL: POST /user/toggle-suspend/{id}
	 */
	@PostMapping("/toggle-suspend/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleSuspendUser(@PathVariable("id") Integer id,
			@RequestParam("suspend") boolean suspend, @RequestParam(value = "reason", required = false) String reason,
			HttpSession session) {

		Map<String, Object> response = new HashMap<>();

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			response.put("success", false);
			response.put("message", "Unauthorized access");
			return ResponseEntity.status(401).body(response);
		}

		try {
			if (suspend) {
				userService.suspendUser(id, reason != null ? reason : "Suspended by admin");
				response.put("message", "User suspended successfully!");
			} else {
				userService.unsuspendUser(id);
				response.put("message", "User unsuspended successfully!");
			}

			response.put("success", true);
			response.put("suspended", suspend);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * View public user profile - Show other user's profile URL: GET
	 * /user/profile/{userId}
	 */
	@GetMapping("/profile/{userId}")
	public String viewUserProfile(@PathVariable("userId") Integer userId, HttpSession session, Model model) {

		// Convert Integer to Long for getById if needed
		User profileUser = userService.getById(userId);
		if (profileUser == null) {
			return "redirect:/dashboard";
		}

		// Check if user is suspended/banned
		if ("suspended".equals(profileUser.getStatus())) {
			model.addAttribute("errorMsg", "This account has been suspended.");
			return "error";
		}

		try {
			// Get user's published sheets only
			List<CheatSheet> userSheets = cheatSheetService.getCheatSheetsByUser(profileUser).stream()
					.filter(sheet -> "published".equals(sheet.getStatus())).collect(Collectors.toList());

			// Get follower and following counts
			long followerCount = userService.countFollowers(userId);
			long followingCount = userService.countFollowing(userId);

			// Prepare model attributes
			model.addAttribute("profileUser", profileUser);
			model.addAttribute("userSheets", userSheets);
			model.addAttribute("sheetCount", userSheets.size());
			model.addAttribute("followerCount", followerCount);
			model.addAttribute("followingCount", followingCount);
			model.addAttribute("totalSheets", cheatSheetService.getCheatSheetsByUser(profileUser).size());

			// Check follow status for logged-in user
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser != null && !loginUser.getUserId().equals(userId)) {
				boolean isFollowing = userService.isFollowing(loginUser.getUserId(), userId);
				model.addAttribute("isFollowing", isFollowing);
				model.addAttribute("canFollow", true);
			} else {
				model.addAttribute("canFollow", false);
			}

			return "user-profile";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Error loading profile: " + e.getMessage());
			return "error";
		}
	}

	/**
	 * Follow or unfollow a user (AJAX endpoint) URL: POST /user/follow/{userId}
	 */
	@PostMapping("/follow/{userId}")
	@ResponseBody
	public ResponseEntity<FollowResponse> followUser(@PathVariable("userId") Integer userId, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(401).body(new FollowResponse(false, "Please login first", false));
		}

		if (loginUser.getUserId().equals(userId)) {
			return ResponseEntity.badRequest().body(new FollowResponse(false, "You cannot follow yourself", false));
		}

		try {
			boolean isNowFollowing;
			Integer currentUserId = loginUser.getUserId();

			if (userService.isFollowing(currentUserId, userId)) {
				userService.unfollowUser(currentUserId, userId);
				isNowFollowing = false;
			} else {
				userService.followUser(currentUserId, userId);
				isNowFollowing = true;
			}

			long newFollowerCount = userService.countFollowers(userId);
			String message = isNowFollowing ? "Successfully followed!" : "Unfollowed successfully!";

			return ResponseEntity.ok(new FollowResponse(true, message, isNowFollowing, newFollowerCount));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(new FollowResponse(false, "Error: " + e.getMessage(), false));
		}
	}

	/**
	 * Get follow status for a user (AJAX endpoint) URL: GET
	 * /user/follow/status/{userId}
	 */
	@GetMapping("/follow/status/{userId}")
	@ResponseBody
	public ResponseEntity<FollowStatusResponse> getFollowStatus(@PathVariable("userId") Integer userId,
			HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return ResponseEntity.status(401).body(new FollowStatusResponse(false));
		}

		try {
			boolean isFollowing = userService.isFollowing(loginUser.getUserId(), userId);
			long followerCount = userService.countFollowers(userId);
			return ResponseEntity.ok(new FollowStatusResponse(isFollowing, followerCount));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(new FollowStatusResponse(false));
		}
	}

	/**
	 * Get followers list for a user URL: GET /user/followers/{userId}
	 */
	@GetMapping("/followers/{userId}")
	public String getFollowers(@PathVariable("userId") Integer userId, Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		try {
			User profileUser = userService.getById(userId);
			if (profileUser == null) {
				return "redirect:/dashboard";
			}

			List<User> followers = userService.getFollowers(userId);
			model.addAttribute("user", profileUser);
			model.addAttribute("followers", followers);
			model.addAttribute("followerCount", followers.size());
			model.addAttribute("followingCount", userService.countFollowing(userId));
			model.addAttribute("currentUser", loginUser);

			return "followers-list";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Error loading followers: " + e.getMessage());
			return "error";
		}
	}

	/**
	 * Get following list for a user URL: GET /user/following/{userId}
	 */
	@GetMapping("/following/{userId}")
	public String getFollowing(@PathVariable("userId") Integer userId, Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		try {
			User profileUser = userService.getById(userId);
			if (profileUser == null) {
				return "redirect:/dashboard";
			}

			List<User> following = userService.getFollowing(userId);
			model.addAttribute("user", profileUser);
			model.addAttribute("following", following);
			model.addAttribute("followerCount", userService.countFollowers(userId));
			model.addAttribute("followingCount", following.size());
			model.addAttribute("currentUser", loginUser);

			return "following-list";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Error loading following: " + e.getMessage());
			return "error";
		}
	}

	/**
	 * Search users (AJAX endpoint for admin) URL: GET /user/search
	 */
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<List<User>> searchUsers(@RequestParam("query") String query, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return ResponseEntity.status(401).build();
		}

		try {
			List<User> users = userService.searchUsers(query);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}

	// ===== Response Classes for AJAX =====

	static class FollowResponse {
		private boolean success;
		private String message;
		private boolean following;
		private long followerCount;

		public FollowResponse(boolean success, String message, boolean following) {
			this.success = success;
			this.message = message;
			this.following = following;
		}

		public FollowResponse(boolean success, String message, boolean following, long followerCount) {
			this.success = success;
			this.message = message;
			this.following = following;
			this.followerCount = followerCount;
		}

		// Getters and Setters
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

		public long getFollowerCount() {
			return followerCount;
		}

		public void setFollowerCount(long followerCount) {
			this.followerCount = followerCount;
		}
	}

	static class FollowStatusResponse {
		private boolean following;
		private long followerCount;

		public FollowStatusResponse(boolean following) {
			this.following = following;
		}

		public FollowStatusResponse(boolean following, long followerCount) {
			this.following = following;
			this.followerCount = followerCount;
		}

		// Getters and Setters
		public boolean isFollowing() {
			return following;
		}

		public void setFollowing(boolean following) {
			this.following = following;
		}

		public long getFollowerCount() {
			return followerCount;
		}

		public void setFollowerCount(long followerCount) {
			this.followerCount = followerCount;
		}
	}
}