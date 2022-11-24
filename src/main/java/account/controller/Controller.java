package account.controller;

import account.dto.*;
import account.dto.AccountSignUpDTO;
import account.dto.NewPasswordDTO;
import account.dto.NewPasswordRespondDTO;
import account.mapper.AccountMapper;
import account.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class Controller {

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountService accountService;

    @Autowired
    PayrollService payrollService;
    @Autowired
    LogService logService;

    @PostMapping("api/auth/signup")
    public AccountDTO returnNewAccount(@Valid @RequestBody AccountSignUpDTO accountSignUpDTO, HttpServletRequest request) {
        logService.addLogSignUp(Event.CREATE_USER, accountSignUpDTO, request);
        return accountMapper.toAccountDTOFromAccount(accountService.saveNewAccount(accountSignUpDTO));
    }

    @PostMapping("api/auth/changepass")
    public NewPasswordRespondDTO setNewPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO,
                                                @AuthenticationPrincipal UserDetails user, HttpServletRequest request) {
        logService.addLogChangePassword(Event.CHANGE_PASSWORD, user.getUsername(), request);
        return accountMapper.toNewPasswordRespondDTO(accountService.setPassword(newPasswordDTO, user));
    }
}
