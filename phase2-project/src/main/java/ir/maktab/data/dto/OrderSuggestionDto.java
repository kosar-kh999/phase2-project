package ir.maktab.data.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSuggestionDto {

    private Long suggestionId;

    private Long orderId;
}
