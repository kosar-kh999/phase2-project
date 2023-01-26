package ir.maktab.data.model;

import jakarta.persistence.Entity;
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
public class Services extends BaseEntity {

    String name;
}
