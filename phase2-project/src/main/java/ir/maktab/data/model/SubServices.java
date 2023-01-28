package ir.maktab.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class SubServices extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    MainService mainService;

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
