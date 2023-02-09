package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.bind.annotation.Mapping;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSystemDto {

    private double price;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeToDo;
    private String address;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date doneDate;


}
