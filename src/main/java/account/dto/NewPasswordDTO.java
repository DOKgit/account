package account.dto;

import account.validator.BreachedPasswordConstraint;
import account.validator.LengthPassword;
import lombok.Getter;
import javax.validation.constraints.NotEmpty;

@Getter
public class NewPasswordDTO {
    @NotEmpty
    @LengthPassword
    @BreachedPasswordConstraint
    String new_password;
}
