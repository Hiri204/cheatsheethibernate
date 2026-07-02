package cheatsheethibernate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.EmailService;
import cheatsheethibernate.service.UserService;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class PasswordResetController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	/**
	 * Show forgot password form URL: GET /forgot-password
	 */
	@GetMapping("/forgot-password")
	public String showForgotForm() {
		return "forgot-password";
	}

	/**
	 * Process forgot password - send reset code to email URL: POST /forgot-password
	 */
	@PostMapping("/forgot-password")
	public String processEmail(@RequestParam("email") String email, Model model) {
		// Validate email
		if (email == null || email.trim().isEmpty()) {
			model.addAttribute("error", "Please enter your email address.");
			return "forgot-password";
		}

		User user = userService.findByEmail(email.trim());

		if (user == null) {
			model.addAttribute("error", "Email address not found. Please check and try again.");
			return "forgot-password";
		}

		// Check if user is active
		if (!"active".equals(user.getStatus())) {
			model.addAttribute("error", "Your account is not active. Please contact support.");
			return "forgot-password";
		}

		try {
			// Generate 6-digit reset code
			String code = String.format("%06d", new Random().nextInt(999999));

			// Save reset code with expiry (15 minutes)
			user.setResetCode(code);
			user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
			userService.updateUser(user);

			// Send email
			emailService.sendResetCode(email.trim(), code);

			model.addAttribute("message", "✅ Verification code has been sent to your email.");
			model.addAttribute("email", email.trim());
			model.addAttribute("codeSent", true);
			return "reset-password";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to send verification code. Please try again.");
			return "forgot-password";
		}
	}

	/**
	 * Verify reset code URL: POST /verify-code
	 */
	@PostMapping("/verify-code")
	public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code, Model model) {

		// Validate inputs
		if (email == null || email.trim().isEmpty() || code == null || code.trim().isEmpty()) {
			model.addAttribute("error", "Email and verification code are required.");
			return "redirect:/forgot-password";
		}

		User user = userService.findByEmail(email.trim());

		if (user == null) {
			model.addAttribute("error", "User not found. Please request a new code.");
			return "redirect:/forgot-password";
		}

		// Check if code matches
		if (user.getResetCode() == null || !code.trim().equals(user.getResetCode())) {
			model.addAttribute("error", "❌ Invalid verification code. Please check and try again.");
			model.addAttribute("email", email.trim());
			return "reset-password";
		}

		// Check if code has expired
		if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
			model.addAttribute("error", "⏰ Verification code has expired. Please request a new one.");
			model.addAttribute("email", email.trim());
			return "reset-password";
		}

		// Code is valid
		model.addAttribute("email", email.trim());
		model.addAttribute("code", code.trim());
		model.addAttribute("codeVerified", true);
		model.addAttribute("message", "✅ Code verified! Please enter your new password.");
		return "reset-password";
	}

	/**
	 * Reset password URL: POST /reset-password
	 */
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("email") String email, @RequestParam("code") String code,
			@RequestParam("newPassword") String newPassword,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword, Model model,
			RedirectAttributes redirectAttr) {

		// Validate inputs
		if (email == null || email.trim().isEmpty()) {
			model.addAttribute("error", "Email is required.");
			return "redirect:/forgot-password";
		}

		if (code == null || code.trim().isEmpty()) {
			model.addAttribute("error", "Verification code is required.");
			return "redirect:/forgot-password";
		}

		if (newPassword == null || newPassword.trim().isEmpty()) {
			model.addAttribute("error", "New password cannot be empty.");
			model.addAttribute("email", email.trim());
			model.addAttribute("code", code.trim());
			return "reset-password";
		}

		// Check password length
		if (newPassword.length() < 6) {
			model.addAttribute("error", "Password must be at least 6 characters.");
			model.addAttribute("email", email.trim());
			model.addAttribute("code", code.trim());
			return "reset-password";
		}

		// Check if passwords match (if confirm password is provided)
		if (confirmPassword != null && !newPassword.trim().equals(confirmPassword.trim())) {
			model.addAttribute("error", "Passwords do not match.");
			model.addAttribute("email", email.trim());
			model.addAttribute("code", code.trim());
			return "reset-password";
		}

		User user = userService.findByEmail(email.trim());

		// Security check
		if (user == null) {
			model.addAttribute("error", "User not found. Please try again.");
			return "redirect:/forgot-password";
		}

		// Verify code again
		if (user.getResetCode() == null || !code.trim().equals(user.getResetCode())) {
			model.addAttribute("error", "Invalid verification code. Please request a new one.");
			return "redirect:/forgot-password";
		}

		// Check if code has expired
		if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
			model.addAttribute("error", "Verification code has expired. Please request a new one.");
			return "redirect:/forgot-password";
		}

		try {
			// Update password
			user.setPasswordHash(newPassword.trim());
			user.setResetCode(null);
			user.setResetCodeExpiry(null);
			userService.updateUser(user);

			redirectAttr.addFlashAttribute("success",
					"✅ Password updated successfully! Please login with your new password.");
			return "redirect:/login";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to update password. Please try again.");
			model.addAttribute("email", email.trim());
			model.addAttribute("code", code.trim());
			return "reset-password";
		}
	}

	/**
	 * Resend verification code URL: POST /resend-code
	 */
	@PostMapping("/resend-code")
	public String resendCode(@RequestParam("email") String email, Model model) {
		if (email == null || email.trim().isEmpty()) {
			model.addAttribute("error", "Email is required.");
			return "redirect:/forgot-password";
		}

		User user = userService.findByEmail(email.trim());

		if (user == null) {
			model.addAttribute("error", "Email address not found.");
			return "redirect:/forgot-password";
		}

		try {
			// Generate new 6-digit code
			String code = String.format("%06d", new Random().nextInt(999999));

			// Update reset code with new expiry (15 minutes)
			user.setResetCode(code);
			user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
			userService.updateUser(user);

			// Send new code
			emailService.sendResetCode(email.trim(), code);

			model.addAttribute("message", "✅ New verification code sent to your email.");
			model.addAttribute("email", email.trim());
			model.addAttribute("codeSent", true);
			return "reset-password";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to send new code. Please try again.");
			return "redirect:/forgot-password";
		}
	}
}