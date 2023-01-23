package ir.maktab.data.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class Customer extends User {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<OrderSystem> orderSystems = new ArrayList<>();

}
