package ir.maktab.data.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubServiceDto {

    private String subName;

    private double price;

    private String briefExplanation;
}
