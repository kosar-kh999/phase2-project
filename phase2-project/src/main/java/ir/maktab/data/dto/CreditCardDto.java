package ir.maktab.data.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreditCardDto {

    @Pattern(regexp = "^[0-9]{16}$")
    private String cardNumber;

    @Pattern(regexp = "^[0-9]{3,4}$")
    private String cvv2;

    @Pattern(regexp = "^[0-9]{7}$")
    private String secondPassword;

    private String captcha;

}
