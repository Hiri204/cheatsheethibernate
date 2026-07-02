package cheatsheethibernate.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.AdminContentService;
import cheatsheethibernate.service.UserService;

@Controller
public class DashboardController {

    @Autowired
    private AdminContentService adminContentService;

    @Autowired
    private UserService userService;

    /**
     * ၁။ မူလ Dashboard ကို ပြသပေးမည့်နေရာ
     * URL: GET /dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // ဝင်ထားသော User ရှိမရှိ စစ်ဆေးခြင်း
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // Dashboard ပေါ်တွင် ပြသရန် Categories များကို Model ထဲသို့ ထည့်ခြင်း
        model.addAttribute("categories", adminContentService.getAllCategories());
        model.addAttribute("users", userService.getAllUsers());
        
        return "dashboard"; // dashboard.jsp သို့ သွားမည်
    }

    // 💡 ရှင်းလင်းချက် - Ambiguous mapping (URL ထပ်ခြင်း) မဖြစ်စေရန်အတွက် 
    // ယခင်က ပါဝင်ခဲ့သော GET /profile နှင့် POST /profile/update Method များကို 
    // ဤနေရာမှ လုံးဝ (လုံးဝ) ဖယ်ထုတ်/ဖျက်ပစ်လိုက်ပြီ ဖြစ်ပါသည်ဗျာ။
    // ထိုလုပ်ဆောင်ချက်များကို ProfileController.java ထဲတွင် သီးသန့် အလုပ်လုပ်ခိုင်းထားပါသည်။
}