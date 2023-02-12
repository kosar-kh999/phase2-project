package ir.maktab.data.dto;

import ir.maktab.data.enums.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerFilterDto {

    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
