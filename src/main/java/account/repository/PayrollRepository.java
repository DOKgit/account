package account.repository;

import account.entity.Payroll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends CrudRepository<Payroll, Long> {

    Optional<Payroll> findPayrollByEmployeeIgnoreCaseAndPeriod(String employee, String period);
//    Optional<Payroll> findPayrollByEmployeeIgnoreCaseAndLocal_Date(String employee, LocalDate localDate);

    Optional<Payroll> findPayrollByEmployeeIgnoreCase(String employee);

    List<Payroll> findPayrollByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);
}
