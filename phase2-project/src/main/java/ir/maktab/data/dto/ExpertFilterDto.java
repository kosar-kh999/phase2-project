package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.maktab.data.enums.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpertFilterDto {

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private String subName;

    private double maxScore;

    private double minScore;
}
