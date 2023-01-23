package ir.maktab.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class Opinion extends BaseEntity {
    int score;
    String viewpoint;
}
