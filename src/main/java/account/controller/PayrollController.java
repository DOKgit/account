package account.controller;

import account.dto.*;
import account.dto.StatusDTO;
import account.entity.Payroll;
import account.mapper.AccountMapper;
import account.service.PayrollService;
import account.service.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PayrollController {

    @Autowired
    PayrollService payrollService;
    @Autowired
    AccountMapper accountMapper;


    @PostMapping("api/acct/payments")
    public StatusDTO postPayments(@Valid @RequestBody List<PayrollDTO> list, @AuthenticationPrincipal UserPrincipal user) {
        payrollService.addNewPayrolls(list, user);
        return new StatusDTO("Added successfully!");
    }

    @PutMapping("api/acct/payments")
    public StatusDTO putPayment(@Valid @RequestBody PayrollDTO payrollDTO, @AuthenticationPrincipal UserPrincipal user) {
        payrollService.putPayroll(payrollDTO, user);
        return new StatusDTO("Updated successfully!");
    }

    @GetMapping("/api/empl/payment")
    public Optional<Object> getPaymentsByPeriod(@RequestParam Optional<String> period, @AuthenticationPrincipal UserPrincipal user) {
        List<Payroll> list = payrollService.getPayrollsByPeriod(period, user.getUsername(), user);
        return list.size() == 1 ? Optional.of(accountMapper.toResponseAccountForPeriodDTOFromAccount(list.get(0))) : Optional.of(list.stream().map(payroll -> accountMapper.toResponseAccountForPeriodDTOFromAccount(payroll)).collect(Collectors.toList()));
    }
}
