package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSystemDto {

    private double price;
    private String description;
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private String timeToDo;
    private String address;
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private String doneDate;
}
