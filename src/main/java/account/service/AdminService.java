package account.service;

import account.dto.RoleOperationDTO;
import account.entity.Account;
import account.exceptions.AccountNotFoundException;
import account.exceptions.BadRequestException;
import account.mapper.AccountMapper;
import account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AdminService {
    @Autowired
    AccountRepository accountRep;
    @Autowired
    AccountMapper accountMapper;

    public List<Account> getAccounts() {
        List<Account> result = new ArrayList<>();
        accountRep.findAll().forEach(result::add);
        return result;
    }

    public void deleteAccountByEmail(String email) {
        getAccountOrThrowExceptionIfUserNotExist(email);
        isAdministratorAccount(email, "Can't remove ADMINISTRATOR role!");
        accountRep.deleteAccountByEmail(email);
    }

    public Account updateRole(@Valid RoleOperationDTO roleOperationDTO) {
        Account account = getAccountOrThrowExceptionIfUserNotExist(roleOperationDTO.getUser());
        return choseOperation(roleOperationDTO, account);
    }

    private Account choseOperation(RoleOperationDTO operation, Account account) {
        switch (operation.getOperation()) {
            case "GRANT":
                if (operation.getRole().equals("ADMINISTRATOR") || operation.getRole().equals("USER")) {
                    throw new BadRequestException("The user cannot combine administrative and business roles!");
                }
                checkIsRoleExists(operation.getRole());
                account.addRole(operation.getRole());
                return accountRep.save(account);
            case "REMOVE":
                checkIsRoleExists(operation.getRole());
                isAdministratorAccount(operation.getUser(), "Can't remove ADMINISTRATOR role!");

                if (!account.getRole().contains(operation.getRole())) {
                    throw new BadRequestException("The user does not have a role!");
                }

                if (account.getRoles().size() == 1) {
                    throw new BadRequestException("The user must have at least one role!");
                }
                account.removeRole(operation.getRole());
                return accountRep.save(account);
            default:
                throw new BadRequestException("Wrong operation, please check your request");
        }
    }

    private void checkIsRoleExists(String role) {
        if (!(role.equals("USER") || role.equals("ACCOUNTANT") || role.equals("ADMINISTRATOR"))) {
            throw new AccountNotFoundException("Role not found!");
        }
    }

    private void isAdministratorAccount(String email, String message) {
        if (accountRep.findUserByEmailIgnoreCase(email).get().getRole().contains("ADMINISTRATOR")) {
            throw new BadRequestException(message);
        }
    }

    private Account getAccountOrThrowExceptionIfUserNotExist(String email) {
        return accountRep.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new AccountNotFoundException("User not found!"));
    }

}