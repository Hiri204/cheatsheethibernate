package cheatsheethibernate.controller;

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

import cheatsheethibernate.entity.User;
import cheatsheethibernate.service.UserService;
import cheatsheethibernate.service.FileUploadService; // 🎯 Import ထည့်ပါ

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService; // 🎯 Service ကို ခေါ်ယူအသုံးပြုခြင်း

    @GetMapping("/view")
    public String viewProfile(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 💡 မင်းရဲ့ မူရင်းကုဒ်အတိုင်း loginUser ရဲ့ ID ဖြင့် Database မှ နောက်ဆုံး Data ကို ယူခြင်း
        User user = userService.getById(loginUser.getUserId());
        model.addAttribute("user", user);
        
        return "profile"; 
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("profileImage") MultipartFile profileImage,
            HttpSession session, HttpServletRequest request) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        User user = userService.getById(loginUser.getUserId());
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            
            // 🎯 [CRITICAL FIX] နိုင်ငံတကာစံနှုန်းအရ Unique ဖြစ်သော field များ ဗလာ (Empty String "") အဖြစ် 
            // Database ထဲ ရောက်သွားပါက Duplicate Entry ပြဿနာ တက်တတ်ပါသည်။
            // ထို့ကြောင့် phone တန်ဖိုး မပါလာလျှင် သို့မဟုတ် Space တွေပဲ ရိုက်လာလျှင် Database ထဲသို့ "" အစား null အဖြစ် ပြောင်းလဲသိမ်းဆည်းပေးရပါမည်။
            if (phone == null || phone.trim().isEmpty()) {
                user.setPhone(null); // ✅ null ဖြစ်သွားလျှင် MySQL Unique Constraint က ထပ်တယ်လို့ မသတ်မှတ်တော့ပါ။
            } else {
                user.setPhone(phone.trim());
            }
            
            // 🔐 Password ပြင်ဆင်ခြင်း
            if (password != null && !password.trim().isEmpty()) {
                user.setPasswordHash(password); // မင်းရဲ့ Entity ထဲက Method အတိုင်း ထည့်ပေးထားပါတယ်
            }

            // 📸 ပုံတင်သည့် Logic
            String uploadedFileName = fileUploadService.uploadProfileImage(profileImage, request);
            if (uploadedFileName != null) {
                user.setProfileImg(uploadedFileName);
            }

            // Database တွင် Update လုပ်ပြီး Session အသစ်လဲလှယ်ခြင်း
            userService.updateUser(user);
            session.setAttribute("loginUser", user);
            session.setAttribute("successMsg", "Profile updated successfully!");
        }

        return "redirect:/profile/view";
    }
}