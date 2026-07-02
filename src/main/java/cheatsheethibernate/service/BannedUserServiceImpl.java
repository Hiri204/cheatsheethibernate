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

    /**
     * 🎯 ဖြေရှင်းချက်ထပ်တိုး: User အား Ban မှ ပြန်လည်ပယ်ဖျက်ပေးမည့် Logic
     */
    public void unbanUser(Integer userId) {
        // Active ဖြစ်နေတဲ့ Ban Record ကို ရှာယူခြင်း
        BannedUser activeBan = bannedUserRepository.findActiveBanByUserId(userId);
        if (activeBan != null) {
            // Record ကို Database ထဲမှ လုံးဝဖျက်ထုတ်လိုက်ခြင်း (သို့မဟုတ် သက်တမ်းကုန်ဆုံးအောင် expiresAt ကို ယခုအချိန် ပြောင်းလဲခြင်း)
            // ဤနေရာတွင် လုံးဝ ဖျက်ထုတ်သည့်ပုံစံကို သုံးပါမည်
            // (မှတ်ချက် - သင့် BannedUserRepository ထဲတွင် delete သို့မဟုတ် getCurrentSession().delete ရှိပါက သုံးနိုင်သည်)
            activeBan.setExpiresAt(LocalDateTime.now()); // သက်တမ်းကုန်သွားပြီဟု သတ်မှတ်၍ list မှ ပျောက်စေခြင်း
            bannedUserRepository.save(activeBan);
        }
    }

	@Override
	public void banUser(Integer userId, Integer adminId, String reason, String banDuration, LocalDateTime expiresAt) {
		// TODO Auto-generated method stub
		
	}
}