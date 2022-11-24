package account.service;

import account.dto.AccountSignUpDTO;
import account.dto.NewPasswordDTO;
import account.entity.Account;
import account.exceptions.BadRequestException;
import account.mapper.AccountMapper;
import account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRep;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    private PasswordEncoder encoder;

    public void increaseFailedAttempts(Account account) {
        int newFailAttempts = account.getFailedAttempt() + 1;
        accountRep.updateFailedAttempts(newFailAttempts, account.getEmail());
    }

    public void resetFailedAttempts(String email) {
        accountRep.updateFailedAttempts(0, email);
    }

    public void lock(Account account) {
        account.setAccountNonLocked(false);
        accountRep.save(account);
    }

    public Account saveNewAccount(AccountSignUpDTO accountSignUpDTO) {
        if (accountRep.findUserByEmailIgnoreCase(accountSignUpDTO.getEmail()).isPresent()) {
            throw new BadRequestException("User exist!");
        }
        Account account = accountMapper.toAccountFromAccountSignUpDTO(accountSignUpDTO, encoder);
        if (accountRep.count() == 0) {
            account.addRole("ROLE_ADMINISTRATOR");
        } else {
            account.addRole("ROLE_USER");
        }
        return accountRep.save(account);
    }

    public Account findAccountByEmail(String email) {
        return accountRep.findUserByEmailIgnoreCase(email).get();
    }

    public Account setPassword(NewPasswordDTO newPasswordDTO, UserPrincipal user) {

        if (encoder.matches(newPasswordDTO.getNew_password(), user.getPassword())) {
            throw new BadRequestException("The passwords must be different!");
        }

        Account account = findAccountByEmail(user.getUsername());
        account.setPassword(encoder.encode(newPasswordDTO.getNew_password()));
        accountRep.save(account);
        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRep.findUserByEmailIgnoreCase(email).get();
        if (account == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }
        UserDetails user = User.withUsername(account.getEmail())
                .password(account.getPassword())
                .accountLocked(account.isAccountNonLocked()).build();

        return new UserPrincipal(account);
    }
}
