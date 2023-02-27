package ir.maktab.data.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerSignInDto {

    private String firstName;

    private String lastName;

    private String email;
}
