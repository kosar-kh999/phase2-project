package ir.maktab.data.dto;

import ir.maktab.data.enums.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpertFilterDto {

    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String subName;
    private double score;
}
