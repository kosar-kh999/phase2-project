package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSignUpDto {

    private String firstName;

    private String lastName;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeAfter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeBefore;

    private Long count;
}
