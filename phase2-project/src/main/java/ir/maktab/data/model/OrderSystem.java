package ir.maktab.data.model;

import ir.maktab.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class OrderSystem extends BaseEntity {

    @ManyToOne
    SubServices subServices;
    double price;
    String description;
    @Temporal(value = TemporalType.TIMESTAMP)
    Date timeToDo;
    String address;

    @Enumerated(value = EnumType.STRING)
    OrderStatus orderStatus;

    @ManyToOne
    Expert expert;

    @Temporal(value = TemporalType.TIMESTAMP)
    Date doneDate;

    public OrderSystem(double price, String description, Date timeToDo, String address) {
        this.price = price;
        this.description = description;
        this.timeToDo = timeToDo;
        this.address = address;
    }
}
