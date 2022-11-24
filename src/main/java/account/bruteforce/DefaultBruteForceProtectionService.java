package account.bruteforce;

import account.entity.Account;
import account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("BruteForceProtectionService")
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    private final int MAX_ATTEMPTS = 5;

    @Autowired
    AccountRepository accountRep;

    @Override
    public void registerLoginFailure(String username) {
        Account account = accountRep.findUserByEmailIgnoreCase(username).get();
        if (account != null && account.isAccountNonLocked()) {
            int failedCounter = account.getFailedAttempt();
            if (MAX_ATTEMPTS < failedCounter + 1) {
                account.setAccountNonLocked(true);
            } else {
                account.setFailedAttempt(failedCounter+1);
            }
            accountRep.save(account);
        }
    }

    @Override
    public void resetBruteForceCounter(String username) {
        Account account = accountRep.findUserByEmailIgnoreCase(username).get();
        if (account != null) {
            account.setFailedAttempt(0);
            account.setAccountNonLocked(false);
            accountRep.save(account);
        }
    }
}
