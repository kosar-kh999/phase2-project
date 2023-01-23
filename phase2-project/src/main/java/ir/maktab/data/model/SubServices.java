package ir.maktab.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class SubServices extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    Services services;
    @Column(nullable = false)
    String subName;
    @Column(nullable = false)
    double price;
    @Column(nullable = false)
    String briefExplanation;

    public SubServices(double price, String briefExplanation) {
        this.price = price;
        this.briefExplanation = briefExplanation;
    }
}
