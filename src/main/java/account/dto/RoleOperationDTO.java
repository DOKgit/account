package account.dto;

import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
public class RoleOperationDTO {
    @NotBlank(message = "User cannot be empty")
    String user;
    String role;
    String operation;
}