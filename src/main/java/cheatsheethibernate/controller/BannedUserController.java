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
     * 🎯 ၁။ User အား Ban လိုက်သည့်ပုံစံ ချက်ချင်းပြောင်းလဲသွားစေရန်
     * URL: POST /admin/user/ban
     */
    @PostMapping("/ban")
    public String banUser(
            @RequestParam("userId") Integer userId,
            @RequestParam("reason") String reason,
            @RequestParam("banDuration") String banDuration, // 🎯 user-management_2.jsp ရှိ name="banDuration" အတိုင်း ဖတ်ယူခြင်း
            HttpSession session) {
        
        User admin = (User) session.getAttribute("loginUser");
        if (admin == null) {
            return "redirect:/login"; 
        }
        Integer adminId = admin.getUserId();

        try {
            // ၁။ Users table ထဲရှိ သက်ဆိုင်ရာ user ၏ status အား 'suspended' ဟု ပြောင်းလဲခြင်း
            User targetUser = userService.getById(userId); 
            if (targetUser != null) {
                targetUser.setStatus("suspended"); // UI တွင် Suspended Badge ပေါ်လာစေရန်
                userService.updateUser(targetUser);
            }

            // 📅 Admin ရွေးချယ်လိုက်သော Duration အပေါ်မူတည်ပြီး ကုန်ဆုံးမည့် ရက်စွဲကို တွက်ချက်ခြင်း
            LocalDateTime expiresAt = null; 
            if ("3 Days".equals(banDuration)) {
                expiresAt = LocalDateTime.now().plusDays(3);
            } else if ("7 Days".equals(banDuration)) {
                expiresAt = LocalDateTime.now().plusDays(7);
            } else if ("30 Days".equals(banDuration)) {
                expiresAt = LocalDateTime.now().plusDays(30);
            } // "Permanent" ဆိုပါက expiresAt သည် null အတိုင်း ကျန်ရှိနေမည်ဖြစ်သည်။

            // ၂။ Banned_users table ထဲသို့ record သစ်ထည့်ခြင်း
            // 💡 သင့် BannedUserServiceImpl ထဲတွင် expiresAt (သို့) banDuration ပါ သိမ်းဆည်းရန် ဖြည့်စွက်ပေးပါ
            bannedUserService.banUser(userId, adminId, reason, banDuration, expiresAt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 🎯 ပြီးလျှင် User list သို့ page refresh ပုံစံဖြင့် ပြန်မောင်းနှင်မည်
        return "redirect:/user/list"; 
    }

    /**
     * 🎯 ၂။ User အား Unban လိုက်လျှင် List မှ ချက်ချင်းပျောက်သွားစေရန်
     * URL: POST /admin/user/unban
     */
    @PostMapping("/unban")
    public String unbanUser(@RequestParam("userId") Integer userId, HttpSession session) {
        
        User admin = (User) session.getAttribute("loginUser");
        if (admin == null) {
            return "redirect:/login";
        }

        try {
            // ၁။ Users table ထဲရှိ status အား ပုံမှန် 'active' ပြန်ပြောင်းခြင်း
            User targetUser = userService.getById(userId); 
            if (targetUser != null) {
                targetUser.setStatus("active"); 
                userService.updateUser(targetUser);
            }

            // ၂။ Banned_users table ထဲမှ Active ဖြစ်နေသော သက်တမ်းကို ကုန်ဆုံးစေခြင်း
            // ((BannedUserServiceImpl) bannedUserService).unbanUser(userId); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🎯 ပြီးလျှင် User list သို့ ပြန်လှည့်ခြင်းဖြင့် List များ Update ချက်ချင်းဖြစ်သွားပါမည်
        return "redirect:/user/list";
    }
}