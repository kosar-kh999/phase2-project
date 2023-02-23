package ir.maktab.data.dto;

import ir.maktab.data.enums.OrderStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCountCustomerDto {

    private String email;
    private OrderStatus orderStatus;
}
