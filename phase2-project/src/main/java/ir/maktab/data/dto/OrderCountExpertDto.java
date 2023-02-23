package ir.maktab.data.dto;

import ir.maktab.data.enums.OrderStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCountExpertDto {
    private String email;
    private OrderStatus orderStatus;
}
