package ir.maktab.data.dto;

import ir.maktab.data.enums.OrderStatus;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSystemDto {

    private MainServiceDto mainServiceDto;
    private double price;
    private String description;
    private Date timeToDo;
    private String address;
    private OrderStatus orderStatus;
    private ExpertDto expertDto;
    private Date doneDate;
}
