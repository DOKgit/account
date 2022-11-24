package account.controller;

import account.dto.AccountWithRolesDTO;
import account.dto.DeleteStatusDTO;
import account.dto.RoleOperationDTO;
import account.mapper.AccountMapper;
import account.service.AdminService;
import account.service.Event;
import account.service.LogService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    LogService logService;

    @PutMapping("api/admin/user/role")
    public AccountWithRolesDTO setTheRole(@Valid @RequestBody RoleOperationDTO roleOperationDTO, HttpServletRequest request, HttpMessageNotReadableException exception) {
        System.out.println("HELLO" + exception.getHttpInputMessage());
        if (roleOperationDTO.getOperation().equals("GRANT")) {
            logService.addLogGrantRole(Event.GRANT_ROLE, roleOperationDTO.getUser(), request, roleOperationDTO.getRole());
        } else {
            logService.addLogRemoveRole(Event.REMOVE_ROLE, roleOperationDTO.getUser(), request, roleOperationDTO.getRole());
        }
        return accountMapper.toAccountWithRolesDTOFromAccount(adminService.updateRole(roleOperationDTO));
    }

    @Transactional
    @DeleteMapping("api/admin/user/{email}")
    public DeleteStatusDTO deleteUser(@PathVariable String email, HttpServletRequest request) {
        logService.addLogDeleteUser(Event.DELETE_USER, email, request);
        adminService.deleteAccountByEmail(email);
        return new DeleteStatusDTO(email, "Deleted successfully!");
    }

    @GetMapping("api/admin/user")
    public List<AccountWithRolesDTO> getUsersInformation() {
        return adminService.getAccounts().stream()
                .map(acc -> accountMapper.toAccountWithRolesDTOFromAccount(acc))
                .collect(Collectors.toList());
    }
}
