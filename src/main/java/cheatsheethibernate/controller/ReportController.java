package cheatsheethibernate.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cheatsheethibernate.entity.Report;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.CheatSheetService;
import cheatsheethibernate.service.ReportService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private CheatSheetService cheatSheetService;

	/**
	 * Submit a report for a cheat sheet URL: POST /reports/submit Returns JSON
	 * response for AJAX
	 */
	@PostMapping("/submit")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> submitReport(@RequestParam("cheatsheetId") Long cheatsheetId,
			@RequestParam("reason") String reason, HttpSession session) {

		Map<String, Object> response = new HashMap<>();

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.put("success", false);
			response.put("message", "Please login first");
			return ResponseEntity.status(401).body(response);
		}

		try {
			reportService.submitReport(loginUser.getUserId(), cheatsheetId, reason);
			response.put("success", true);
			response.put("message", "Report submitted successfully. Admin will review it shortly.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Failed to submit report: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * Admin panel to view all reports URL: GET /reports/admin/list
	 */
	@GetMapping("/admin/list")
	public String viewAdminReports(Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// Check admin role
		if (!"admin".equals(loginUser.getRole())) {
			return "redirect:/dashboard";
		}

		try {
			model.addAttribute("reports", reportService.getAllReports());
			model.addAttribute("pendingCount", reportService.getReportsByStatus("pending").size());
			model.addAttribute("resolvedCount", reportService.getReportsByStatus("resolved").size());
			model.addAttribute("bannedSheets", cheatSheetService.getBannedSheets());
			model.addAttribute("admin", loginUser);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Error loading reports: " + e.getMessage());
		}

		return "admin/reports-panel";
	}

	/**
	 * Admin action to update report status URL: GET
	 * /reports/admin/action/{id}?status=resolved
	 */
	@GetMapping("/admin/action/{id}")
	public String actionReport(@PathVariable("id") Integer reportId, @RequestParam("status") String status,
			HttpSession session, RedirectAttributes redirectAttributes) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		if (!"admin".equals(loginUser.getRole())) {
			return "redirect:/dashboard";
		}

		try {
			reportService.actionReport(reportId, status);
			redirectAttributes.addFlashAttribute("successMsg", "Report status updated to: " + status);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to update report: " + e.getMessage());
		}

		return "redirect:/reports/admin/list";
	}

	/**
	 * Admin action to ban a cheat sheet from a report URL: POST
	 * /reports/admin/ban/{reportId}
	 */
	@PostMapping("/admin/ban/{reportId}")
	public String banCheatSheetFromReport(@PathVariable("reportId") Integer reportId,
			@RequestParam("banReason") String banReason,
			@RequestParam(value = "banDuration", required = false) String banDuration, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			redirectAttributes.addFlashAttribute("errorMsg", "You must be an admin to ban sheets.");
			return "redirect:/reports/admin/list";
		}

		try {
			// Get the report
			Report report = reportService.getReportById(reportId);
			if (report == null) {
				redirectAttributes.addFlashAttribute("errorMsg", "Report not found!");
				return "redirect:/reports/admin/list";
			}

			// Validate report status
			if (!"pending".equals(report.getStatus())) {
				redirectAttributes.addFlashAttribute("errorMsg", "This report has already been processed!");
				return "redirect:/reports/admin/list";
			}

			// Check if cheat sheet exists
			if (report.getCheatSheet() == null) {
				redirectAttributes.addFlashAttribute("errorMsg", "CheatSheet not found for this report!");
				return "redirect:/reports/admin/list";
			}

			Long sheetId = report.getCheatSheet().getId();

			// Calculate ban expiry
			LocalDateTime expiresAt = calculateBanExpiry(banDuration);

			// Ban the sheet
			cheatSheetService.banSheet(sheetId, banReason, expiresAt, loginUser.getUserId());

			// Update report status
			reportService.actionReport(reportId, "resolved");

			redirectAttributes.addFlashAttribute("successMsg", "Sheet banned successfully!");

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Error banning sheet: " + e.getMessage());
		}

		return "redirect:/reports/admin/list";
	}

	/**
	 * Admin action to unban a cheat sheet URL: POST /reports/admin/unban/{sheetId}
	 */
	@PostMapping("/admin/unban/{sheetId}")
	public String unbanCheatSheet(@PathVariable("sheetId") Long sheetId, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			redirectAttributes.addFlashAttribute("errorMsg", "You must be an admin to unban sheets.");
			return "redirect:/reports/admin/list";
		}

		try {
			cheatSheetService.unbanSheet(sheetId);
			redirectAttributes.addFlashAttribute("successMsg", "Sheet unbanned successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Error unbanning sheet: " + e.getMessage());
		}

		return "redirect:/reports/admin/list";
	}

	/**
	 * Helper method to calculate ban expiry date based on duration
	 */
	private LocalDateTime calculateBanExpiry(String banDuration) {
		if (banDuration == null || banDuration.isEmpty()) {
			return null; // Permanent ban
		}

		switch (banDuration) {
		case "1day":
			return LocalDateTime.now().plusDays(1);
		case "3days":
			return LocalDateTime.now().plusDays(3);
		case "7days":
			return LocalDateTime.now().plusDays(7);
		case "30days":
			return LocalDateTime.now().plusDays(30);
		case "permanent":
		default:
			return null; // Permanent ban
		}
	}

	/**
	 * Get report details for admin view URL: GET /reports/admin/details/{id}
	 */
	@GetMapping("/admin/details/{id}")
	public String viewReportDetails(@PathVariable("id") Integer reportId, Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return "redirect:/login";
		}

		try {
			Report report = reportService.getReportById(reportId);
			if (report == null) {
				model.addAttribute("errorMsg", "Report not found!");
				return "redirect:/reports/admin/list";
			}

			model.addAttribute("report", report);
			return "admin/report-details";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Error loading report details: " + e.getMessage());
			return "redirect:/reports/admin/list";
		}
	}

	/**
	 * Bulk action for multiple reports URL: POST /reports/admin/bulk-action
	 */
	@PostMapping("/admin/bulk-action")
	public String bulkAction(@RequestParam("reportIds") Integer[] reportIds, @RequestParam("action") String action,
			HttpSession session, RedirectAttributes redirectAttributes) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !"admin".equals(loginUser.getRole())) {
			return "redirect:/login";
		}

		try {
			int count = 0;
			for (Integer reportId : reportIds) {
				reportService.actionReport(reportId, action);
				count++;
			}
			redirectAttributes.addFlashAttribute("successMsg",
					"Successfully processed " + count + " reports with action: " + action);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Error processing bulk action: " + e.getMessage());
		}

		return "redirect:/reports/admin/list";
	}
}