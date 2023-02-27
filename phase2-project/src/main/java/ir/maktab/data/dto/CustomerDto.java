package ir.maktab.data.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {

    @Pattern(regexp = "^[A-Za-z]\\w{2,29}$")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z]\\w{2,29}$")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]).{8}$")
    private String password;

    private double credit;
}
