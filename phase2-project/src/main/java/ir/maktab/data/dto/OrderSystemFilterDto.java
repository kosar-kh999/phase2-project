package ir.maktab.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import ir.maktab.data.enums.OrderStatus;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSystemFilterDto {

    private OrderStatus orderStatus;

    private String subName;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeAfter;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeBefore;

    private double price;

    private String description;

    private String address;

}
