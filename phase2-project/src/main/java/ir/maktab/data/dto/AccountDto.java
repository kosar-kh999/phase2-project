package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDto {

    @Pattern(regexp = "^[0-9]{16}$")
    private String cardNumber;

    @Pattern(regexp = "^[0-9]{3,4}$")
    private String cvv2;

    @Pattern(regexp = "^[0-9]{7}$")
    private String secondPassword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    private double amount;
}
