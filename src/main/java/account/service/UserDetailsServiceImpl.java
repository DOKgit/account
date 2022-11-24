package account.service;

import account.entity.Account;
import account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findUserByEmailIgnoreCase(email).get();
        if (account == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }
        return new UserDetailsImpl(account);
    }
}
