package cheatsheethibernate.service;

import java.time.LocalDateTime;
import java.util.List;
import cheatsheethibernate.entity.BannedUser;

public interface BannedUserService {
	void banUser(Integer userId, Integer adminId, String reason, Integer durationDays);

	void banUser(Integer userId, Integer adminId, String reason, String banDuration, LocalDateTime expiresAt);

	boolean isUserBanned(Integer userId);

	List<BannedUser> getAllActiveBans();

	List<BannedUser> getExpiredBans();

	void unbanUser(Integer userId);
}