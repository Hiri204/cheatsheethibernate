package cheatsheethibernate.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.ReportService;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	/**
	 * 🚩 ၁။ User တစ်ယောက်မှ CheatSheet အား Report တင်ရန် လက်ခံသည့် လမ်းကြောင်း URL:
	 * POST /reports/submit Now returns JSON response for AJAX
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
			// Report Service သို့ ပို့ပေးခြင်း
			reportService.submitReport(loginUser.getUserId().longValue(), cheatsheetId, reason);
			response.put("success", true);
			response.put("message", "တိုင်ကြားချက်ကို အောင်မြင်စွာ ပေးပို့ပြီးပါပြီ။ Admin မှ မကြာမီ စစ်ဆေးပေးပါမည်။");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "တိုင်ကြားမှု မအောင်မြင်ပါ: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * 👑 ၂။ Admin Panel ပေါ်တွင် တင်ထားသမျှ Report အားလုံးကို စာရင်းပတ်ကြည့်ရန်
	 * URL: GET /reports/admin/list
	 */
	@GetMapping("/admin/list")
	public String viewAdminReports(Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("reports", reportService.getAllReports());
		return "admin/reports-panel";
	}

	/**
	 * ⚙️ ၃။ Admin မှ တိုင်ကြားစာကို စစ်ဆေးပြီး Status ပြောင်းလဲရန် (Reviewed /
	 * Resolved) URL: GET /reports/admin/action/{id}?status=resolved
	 */
	@GetMapping("/admin/action/{id}")
	public String actionReport(@PathVariable("id") Integer reportId, @RequestParam("status") String status,
			HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		reportService.actionReport(reportId, status);
		return "redirect:/reports/admin/list";
	}
}