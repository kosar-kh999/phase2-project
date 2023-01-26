package ir.maktab.data.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
@SuperBuilder
public class Opinion extends BaseEntity {

    int score;

    String viewpoint;
}
