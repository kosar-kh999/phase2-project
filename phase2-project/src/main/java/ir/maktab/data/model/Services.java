package ir.maktab.data.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class Services extends BaseEntity {

    String name;
}
