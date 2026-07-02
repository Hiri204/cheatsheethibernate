package cheatsheethibernate.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

	/**
	 * Upload a profile image
	 */
	public String uploadProfileImage(MultipartFile profileImage, HttpServletRequest request) {
		if (profileImage == null || profileImage.isEmpty()) {
			return null;
		}

		// Validate file type
		String contentType = profileImage.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("Only image files are allowed for profile photos");
		}

		// Validate file size
		if (profileImage.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("File size exceeds 5MB limit");
		}

		try {
			String uploadRootPath = getUploadPath(request);
			File uploadDir = new File(uploadRootPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String originalFileName = profileImage.getOriginalFilename();
			String fileExtension = "";
			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
			String uniqueFileName = timestamp + "_profile" + fileExtension;

			File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
			profileImage.transferTo(serverFile);

			return uniqueFileName;

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
		}
	}

	/**
	 * Upload a cheat sheet image
	 */
	public String uploadCheatSheetImage(MultipartFile imageFile, HttpServletRequest request) {
		if (imageFile == null || imageFile.isEmpty()) {
			return null;
		}

		// Validate file type
		String contentType = imageFile.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("Only image files are allowed for cheat sheets");
		}

		// Validate file size
		if (imageFile.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("File size exceeds 5MB limit");
		}

		try {
			String uploadRootPath = getUploadPath(request);
			File uploadDir = new File(uploadRootPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String originalFileName = imageFile.getOriginalFilename();
			String fileExtension = "";
			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
			String uniqueFileName = timestamp + "_cheatsheet" + fileExtension;

			File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
			imageFile.transferTo(serverFile);

			return uniqueFileName;

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to upload cheat sheet image: " + e.getMessage());
		}
	}

	/**
	 * Upload a general file (for other purposes)
	 */
	public String uploadFile(MultipartFile file, HttpServletRequest request, String prefix) {
		if (file == null || file.isEmpty()) {
			return null;
		}

		// Validate file size
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new IllegalArgumentException("File size exceeds 5MB limit");
		}

		try {
			String uploadRootPath = getUploadPath(request);
			File uploadDir = new File(uploadRootPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			String originalFileName = file.getOriginalFilename();
			String fileExtension = "";
			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
			String prefixStr = (prefix != null && !prefix.isEmpty()) ? prefix + "_" : "";
			String uniqueFileName = timestamp + "_" + prefixStr + UUID.randomUUID().toString().substring(0, 8)
					+ fileExtension;

			File serverFile = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
			file.transferTo(serverFile);

			return uniqueFileName;

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to upload file: " + e.getMessage());
		}
	}

	/**
	 * Delete a file
	 */
	public boolean deleteFile(String filename, HttpServletRequest request) {
		if (filename == null || filename.isEmpty()) {
			return false;
		}

		try {
			String uploadPath = getUploadPath(request);
			File file = new File(uploadPath + File.separator + filename);
			if (file.exists()) {
				return file.delete();
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the upload directory path
	 */
	private String getUploadPath(HttpServletRequest request) {
		// Try to get real path from servlet context
		String realPath = request.getServletContext().getRealPath("/uploads");
		if (realPath != null && !realPath.isEmpty()) {
			return realPath;
		}

		// Fallback paths
		String userDir = System.getProperty("user.dir");
		String[] possiblePaths = { userDir + File.separator + "uploads",
				userDir + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator
						+ "uploads",
				userDir + File.separator + "target" + File.separator + "classes" + File.separator + "uploads" };

		for (String path : possiblePaths) {
			File dir = new File(path);
			if (dir.exists() || dir.mkdirs()) {
				return path;
			}
		}

		// Last resort: use the current directory
		return userDir + File.separator + "uploads";
	}

	/**
	 * Get the full URL path for a file
	 */
	public String getFileUrl(String filename, HttpServletRequest request) {
		if (filename == null || filename.isEmpty()) {
			return null;
		}
		return request.getContextPath() + "/uploads/" + filename;
	}
}