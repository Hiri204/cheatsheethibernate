package cheatsheethibernate.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import cheatsheethibernate.entity.User;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        
        String uri = request.getRequestURI();
        
        // ADMIN control လုပ်ဆောင်ချက်များအားလုံးကို စစ်ဆေးခြင်း
        if (uri.contains("/admin/")) {
            // Database ထဲက တန်ဖိုးအတိုင်း အသေး/အကြီး မခွဲခြားဘဲ စစ်ဆေးပါ
            if (user.getRole() == null || !"admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/cheatsheets/dashboard?error=access_denied");
                return false;
            }
        }
        
        return true;
    }
}