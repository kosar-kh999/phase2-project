package ir.maktab.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
