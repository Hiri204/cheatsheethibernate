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

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService; 

    @Autowired
    private EmailService emailService; 

   
    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "forgot-password"; 
    }

   
    @PostMapping("/forgot-password")
    public String processEmail(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        
        if (user == null) {
            model.addAttribute("error", "Email မှားယွင်းနေပါသည်။");
            return "forgot-password";
        }

        String code = String.valueOf((int)((Math.random() * 900000) + 100000));
        user.setResetCode(code);
        user.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        
        userService.updateUser(user);
        emailService.sendResetCode(email, code);

        model.addAttribute("message", "Verification Code has been sent to your email.");
        model.addAttribute("email", email);
        return "reset-password"; // 
    }

    
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("email") String email,
                             @RequestParam("code") String code,
                             Model model) {
        User user = userService.findByEmail(email);

        if (user == null || !code.equals(user.getResetCode())) {
            model.addAttribute("error", "Invalid verification code.");
            model.addAttribute("email", email);
            return "reset-password"; // မှားပါက Code ပြန်ရိုက်ခိုင်းမည်
        }

        if (user.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Verification code has expired. Please request a new one.");
            model.addAttribute("email", email);
            return "reset-password";
        }

        
        model.addAttribute("email", email);
        model.addAttribute("code", code); 
        model.addAttribute("codeVerified", true); 
        return "reset-password";
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("code") String code,
                                @RequestParam("newPassword") String newPassword,
                                Model model,
                                RedirectAttributes redirectAttr) {
                                    
        User user = userService.findByEmail(email);

        // Security အရ နောက်တစ်ကြိမ် လုံခြုံရေးပြန်စစ်ခြင်း
        if (user == null || !code.equals(user.getResetCode())) {
            model.addAttribute("error", "Security check failed. Please try again.");
            return "forgot-password";
        }

        // Password အသစ်ပြောင်းခြင်း
        user.setPasswordHash(newPassword);
        user.setResetCode(null);
        user.setExpiryDate(null);
        
        userService.updateUser(user);

        redirectAttr.addFlashAttribute("success", "Password updated successfully. Please login.");
        return "redirect:/login";
    }
}