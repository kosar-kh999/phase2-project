package ir.maktab.data.model;

import ir.maktab.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class OrderSystem extends BaseEntity {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<SubServices> subServices = new ArrayList<>();
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
