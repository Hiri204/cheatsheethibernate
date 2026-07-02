package cheatsheethibernate.service;

import java.time.LocalDateTime;
import java.util.List;
import cheatsheethibernate.entity.BannedUser;

public interface BannedUserService {
    void banUser(Integer userId, Integer adminId, String reason, Integer durationDays);
    boolean isUserBanned(Integer userId);
    List<BannedUser> getAllActiveBans();
	void banUser(Integer userId, Integer adminId, String reason, String banDuration, LocalDateTime expiresAt);
}