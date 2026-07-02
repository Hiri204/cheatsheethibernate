package cheatsheethibernate.repository;

import java.util.List;
import cheatsheethibernate.entity.BannedUser;

public interface BannedUserRepository {
	void save(BannedUser banUser);

	BannedUser findActiveBanByUserId(Integer userId);

	List<BannedUser> findAllActiveBans();

	List<BannedUser> findExpiredBans();

	void delete(Integer id);
}