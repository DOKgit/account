package account.service;

import account.dto.PayrollDTO;
import account.entity.Account;
import account.entity.Payroll;
import account.exceptions.BadRequestException;
import account.mapper.AccountMapper;
import account.repository.AccountRepository;
import account.repository.PayrollRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PayrollService {

    @Autowired
    AccountRepository accountRep;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    PayrollRepository payrollRep;

    @Transactional
    public boolean addNewPayrolls(@Valid List<PayrollDTO> list, UserPrincipal user) {
        for (Payroll payroll : accountMapper.toPayrollListFromPayrollListDTO(list)) {
            Account account = accountRep.findUserByEmailIgnoreCase(payroll.getEmployee()).orElseThrow(() -> new BadRequestException("Account is not exists"));
            if (payroll.getEmployee().isEmpty() || payroll.getPeriod().isEmpty() || payroll.getSalary() < 0) {
                throw new BadRequestException("Dab requests");
            }
            int month = Integer.parseInt(payroll.getPeriod().split("-")[0]);
            if (month < 0 || month > 12) {
                throw new BadRequestException("Invalid date");
            }

            if (payrollRep.findPayrollByEmployeeIgnoreCaseAndPeriod(payroll.getEmployee(), payroll.getPeriod()).isPresent()) {
                throw new BadRequestException("Payroll is exists");
            }
            payroll.setLocalDate(getLocalDateAfterParsing(payroll.getPeriod()));

            payroll.setAccount(account);
            payrollRep.save(payroll);
        }
        return true;
    }

    // public List<Payroll> getPayrollsByPeriod(Optional<String> period, String employee, UserDetailsImpl user) {
    public List<Payroll> getPayrollsByPeriod(Optional<String> period, String employee, UserPrincipal user) {
        Payroll payroll;
        List<Payroll> payrollList = new ArrayList<>();
        if (period.isEmpty()) {
            payrollList = payrollRep.findPayrollByEmployeeIgnoreCaseOrderByPeriodDesc(employee);
        } else {
            payroll = payrollRep.findPayrollByEmployeeIgnoreCaseAndPeriod(employee, period.get())
                    .orElseThrow(() -> new BadRequestException("Payroll is not exists"));
            payrollList.add(payroll);
        }
        return payrollList;
    }

    private LocalDate getLocalDateAfterParsing(String period) {
        String[] array = period.split("-");
        return LocalDate.parse(String.format("%s-%s-01", array[1], array[0]));
    }

    @Transactional
    public void putPayroll(PayrollDTO payrollDTO, UserPrincipal user) {

        Payroll payroll = payrollRep.findPayrollByEmployeeIgnoreCaseAndPeriod(payrollDTO.getEmployee(), payrollDTO.getPeriod())
                .orElseThrow(() -> new BadRequestException("Payroll dont exists"));

        payroll.setSalary(payrollDTO.getSalary());
        payrollRep.save(payroll);
    }
}
