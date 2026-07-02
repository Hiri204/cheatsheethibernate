package cheatsheethibernate.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    public String uploadProfileImage(MultipartFile profileImage, HttpServletRequest request) {
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String uploadRootPath = request.getServletContext().getRealPath("/uploads");
                File uploadDir = new File(uploadRootPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String originalFileName = profileImage.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
                profileImage.transferTo(serverFile);

                return uniqueFileName; // သိမ်းဆည်းရရှိသော ဖိုင်နာမည်ကို ပြန်ပေးမည်
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}