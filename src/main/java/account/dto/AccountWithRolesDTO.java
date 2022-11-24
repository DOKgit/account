package account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class AccountWithRolesDTO {
    long id;
    String name;
    String lastname;
    String email;
    List<String> roles;
}
