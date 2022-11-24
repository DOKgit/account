package account.dto;

import account.validator.BreachedPasswordConstraint;
import account.validator.LengthPassword;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class AccountSignUpDTO {
    @NotEmpty
    String name;
    @NotEmpty
    String lastname;

    @NotEmpty
    @Pattern(regexp = ".*@acme.com$")
    String email;
    @NotEmpty
    @LengthPassword
    @BreachedPasswordConstraint
    private String password;

}
