package cheatsheethibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;

    @Override
    public void sendResetCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        // 🎯 ဒီနေရာတွင် "CheatSheet <အီးမေးလ်>" ပုံစံပြောင်းလိုက်ခြင်းဖြင့် Name ပြောင်းလဲသွားပါမည်
        message.setFrom("CheatSheet <unknown-email@gmail.com>"); 
        message.setTo(toEmail);
        message.setSubject("🔒 CheatSheet Account - Password Reset Request");
        
        String mailContent = "Hello,\n\n"
                + "We received a request to reset the password for your account.\n"
                + "Please use the following 6-digit verification code to complete the process:\n\n"
                + "Verification Code: " + code + "\n\n"
                + "(This code is valid for 5 minutes only.)\n\n"
                + "Thank you,\n"
                + "CheatSheet Team";
                
        message.setText(mailContent);
        
        mailSender.send(message);
    }
}