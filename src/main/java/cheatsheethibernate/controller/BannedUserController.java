package cheatsheethibernate.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.BannedUserService;
import cheatsheethibernate.service.UserService;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/user")
public class BannedUserController {

	@Autowired
	private BannedUserService bannedUserService;

	@Autowired
	private UserService userService;

	/**
	 * Ban a user URL: POST /admin/user/ban
	 */
	@PostMapping("/ban")
	public String banUser(@RequestParam("userId") Integer userId, @RequestParam("reason") String reason,
			@RequestParam("banDuration") String banDuration, HttpSession session) {

		User admin = (User) session.getAttribute("loginUser");
		if (admin == null) {
			return "redirect:/login";
		}
		Integer adminId = admin.getUserId();

		try {
			// 1. Update user status to 'suspended'
			User targetUser = userService.getById(userId);
			if (targetUser != null) {
				targetUser.setStatus("suspended");
				userService.updateUser(targetUser);
			}

			// 2. Calculate expiry date based on duration
			LocalDateTime expiresAt = null;
			if ("3 Days".equals(banDuration)) {
				expiresAt = LocalDateTime.now().plusDays(3);
			} else if ("7 Days".equals(banDuration)) {
				expiresAt = LocalDateTime.now().plusDays(7);
			} else if ("30 Days".equals(banDuration)) {
				expiresAt = LocalDateTime.now().plusDays(30);
			} // "Permanent" means expiresAt stays null

			// 3. Save ban record
			bannedUserService.banUser(userId, adminId, reason, banDuration, expiresAt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/list";
	}

	/**
	 * Unban a user URL: POST /admin/user/unban
	 */
	@PostMapping("/unban")
	public String unbanUser(@RequestParam("userId") Integer userId, HttpSession session) {

		User admin = (User) session.getAttribute("loginUser");
		if (admin == null) {
			return "redirect:/login";
		}

		try {
			// 1. Update user status to 'active'
			User targetUser = userService.getById(userId);
			if (targetUser != null) {
				targetUser.setStatus("active");
				userService.updateUser(targetUser);
			}

			// 2. Unban the user (soft delete)
			bannedUserService.unbanUser(userId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/list";
	}
}