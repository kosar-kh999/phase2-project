package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDoneDateDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date doneDate;
}
