package account.bruteforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

    @Resource(name = "BruteForceProtectionService")
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        LOG.info("******** login successfull for user {}", username);
        bruteForceProtectionService.resetBruteForceCounter(username);
    }
}
