package ir.maktab.data.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpertSignInDto {

    private String firstName;
    private String lastName;
    private String email;
    private double credit;
}
