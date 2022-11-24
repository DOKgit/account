package account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ResponseAccountForPeriodDTO {
    String name;
    String lastname;
    String period;
    String salary;
}
