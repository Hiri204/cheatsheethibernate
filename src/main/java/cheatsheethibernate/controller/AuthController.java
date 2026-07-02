package cheatsheethibernate.controller;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.CheatSheetService;
import cheatsheethibernate.service.UserService;
import cheatsheethibernate.service.FileUploadService; // 🎯 Import ထည့်ပါ

@Controller
public class AuthController {

    @Autowired
    private CheatSheetService cheatSheetService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService; // 🎯 Service ကို ခေါ်ယူအသုံးပြုခြင်း
    
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; 
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            @RequestParam("username") String username,            
            @RequestParam("email") String email,                  
            @RequestParam("passwordHash") String passwordHash,    
            @RequestParam("profileImage") MultipartFile profileImage, 
            HttpServletRequest request,
            Model model) {
        
        try {
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            user.setRole("user"); 
            user.setStatus("active"); 

            // 📸 🎯 ပုံတင်တဲ့ ကုဒ်အရှည်ကြီးအစား တစ်လိုင်းတည်းဖြင့် ရှင်းပစ်ခြင်း
            String uploadedFileName = fileUploadService.uploadProfileImage(profileImage, request);
            if (uploadedFileName != null) {
                user.setProfileImg(uploadedFileName);
            } else {
                user.setProfileImg("default-avatar.png");
            }

            cheatSheetService.registerUser(user);
            return "redirect:/login"; 

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, 
                             @RequestParam("password") String password, 
                             HttpSession session, Model model) {
        
        User user = cheatSheetService.loginUser(username, password);
        
        if (user != null) {
            if ("suspended".equals(user.getStatus())) {
                List<BannedUser> allBanned = userService.getAllBannedUsers();
                BannedUser banInfo = allBanned.stream()
                    .filter(b -> b.getUser() != null && b.getUser().getUserId().equals(user.getUserId()))
                    .findFirst()
                    .orElse(null);
                
String reason = (banInfo != null && banInfo.getReason() != null) ? banInfo.getReason() : "No reason provided.";
                
                // 🎯 Admin ရွေးချယ်ခဲ့သော Date/Duration ကို ဖော်ပြခြင်း Logic
                String duration = "Permanent";
                if (banInfo != null && banInfo.getExpiresAt() != null) {
                    // 📅 သက်တမ်းကုန်ဆုံးမည့်ရက်စွဲကို လှပသော အင်္ဂလိပ်ပုံစံပြောင်းခြင်း (ဥပမာ- 2026-07-15 14:30)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    duration = "Until " + banInfo.getExpiresAt().format(formatter);
                }

                // 🎯 JSP ရှိ JavaScript မိုဒယ်ပွင့်စေရန် တန်ဖိုးများ လှမ်းပို့ပေးခြင်း (မြန်မာစာလုံးများ ဖယ်ရှားပြီးဖြစ်သည်)
                model.addAttribute("isBanned", "true");
                model.addAttribute("banReason", reason);
                model.addAttribute("banDuration", duration); 
                
                return "login";
            }
            
            session.setAttribute("loginUser", user); 
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
}