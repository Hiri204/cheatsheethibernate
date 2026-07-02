package cheatsheethibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cheatsheethibernate.entity.BannedUser;
import cheatsheethibernate.entity.User;
import cheatsheethibernate.repository.BannedUserRepository;
import cheatsheethibernate.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BannedUserServiceImpl implements BannedUserService {

	@Autowired
	private BannedUserRepository bannedUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void banUser(Integer userId, Integer adminId, String reason, Integer durationDays) {
		User user = userRepository.getById(userId);
		if (user != null) {
			BannedUser bannedUser = new BannedUser();
			bannedUser.setUser(user);
			bannedUser.setBannedBy(Long.valueOf(adminId));
			bannedUser.setReason(reason);
			bannedUser.setBannedAt(LocalDateTime.now());

			if (durationDays != null && durationDays > 0) {
				bannedUser.setExpiresAt(LocalDateTime.now().plusDays(durationDays));
			} else {
				bannedUser.setExpiresAt(null);
			}
			bannedUserRepository.save(bannedUser);
		}
	}

	@Override
	public void banUser(Integer userId, Integer adminId, String reason, String banDuration, LocalDateTime expiresAt) {
		User user = userRepository.getById(userId);
		if (user != null) {
			BannedUser bannedUser = new BannedUser();
			bannedUser.setUser(user);
			bannedUser.setBannedBy(Long.valueOf(adminId));
			bannedUser.setReason(reason);
			bannedUser.setBannedAt(LocalDateTime.now());
			bannedUser.setExpiresAt(expiresAt);
			bannedUserRepository.save(bannedUser);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUserBanned(Integer userId) {
		BannedUser activeBan = bannedUserRepository.findActiveBanByUserId(userId);
		return activeBan != null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BannedUser> getAllActiveBans() {
		return bannedUserRepository.findAllActiveBans();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BannedUser> getExpiredBans() {
		return bannedUserRepository.findExpiredBans();
	}

	@Override
	@Transactional
	public void unbanUser(Integer userId) {
		// Find active ban
		BannedUser activeBan = bannedUserRepository.findActiveBanByUserId(userId);
		if (activeBan != null) {
			// Soft delete by setting expiresAt to now
			activeBan.setExpiresAt(LocalDateTime.now());
			bannedUserRepository.save(activeBan);
		}
	}
}