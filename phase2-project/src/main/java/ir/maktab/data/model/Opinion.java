package ir.maktab.data.model;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    int score;

    String viewpoint;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    Expert expert;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    OrderSystem orderSystem;
}
