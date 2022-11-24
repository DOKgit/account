package account.mapper;

import account.dto.*;
import account.entity.Account;
import account.entity.Payroll;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    public AccountDTO accountDTOFromAccountSignUpDTO(AccountSignUpDTO accountSignUpDTO) {
        return AccountDTO.builder()
                .lastname(accountSignUpDTO.getLastname())
                .email(accountSignUpDTO.getEmail())
                .name(accountSignUpDTO.getName())

                .build();
    }

    public Account toAccountFromAccountSignUpDTO(AccountSignUpDTO accountSignUpDTO, PasswordEncoder encoder) {
        return Account.builder()
                .email(accountSignUpDTO.getEmail().toLowerCase())
                .name(accountSignUpDTO.getName())
                .lastName(accountSignUpDTO.getLastname())
                .password(encoder.encode(accountSignUpDTO.getPassword()))
                .build();
    }

    public AccountDTO toAccountDTOFromAccount(Account account) {
        return AccountDTO.builder()
                .name(account.getName())
                .email(account.getEmail())
                .id(account.getId())
                .lastname(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public AccountWithRolesDTO toAccountWithRolesDTOFromAccount(Account account) {
        return AccountWithRolesDTO.builder()
                .name(account.getName())
                .email(account.getEmail().toLowerCase())
                .id(account.getId())
                .lastname(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public NewPasswordRespondDTO toNewPasswordRespondDTO(Account account) {
        return NewPasswordRespondDTO.builder()
                .email(account.getEmail().toLowerCase())
                .status("The password has been updated successfully")
                .build();
    }

    public List<Payroll> toPayrollListFromPayrollListDTO(List<PayrollDTO> list) {
        return list.stream().map(this::toPayrollFromPayrollDTO).collect(Collectors.toList());
    }

    public Payroll toPayrollFromPayrollDTO(PayrollDTO payrollDTO) {
        return Payroll.builder()
                .employee(payrollDTO.getEmployee())
                .period(payrollDTO.getPeriod())
                .salary(payrollDTO.getSalary())
                .build();
    }

    public ResponseAccountForPeriodDTO toResponseAccountForPeriodDTOFromAccount(Payroll payroll) {
        return ResponseAccountForPeriodDTO.builder()
                .period(payroll.getLocalDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.US) + "-" + payroll.getLocalDate().getYear())
                .lastname(payroll.getAccount().getLastName())
                .name(payroll.getAccount().getName())
                .salary(String.format("%d dollar(s) %d cent(s)", payroll.getSalary() / 100, payroll.getSalary() % 100))
                .build();
    }

    private List<String> toRoleList(List<String> rolesList) {
        return rolesList.stream().map(role -> {
                    role = role.replaceAll("ROLE_", "");
                    role = role.charAt(0) + role.toLowerCase(Locale.ROOT).substring(1, role.length()) + " " + "roles";
                    return role;
                }
        ).collect(Collectors.toList());
    }
}
