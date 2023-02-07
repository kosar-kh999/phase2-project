package ir.maktab.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class Suggestion extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    Expert expert;

    @Column(nullable = false)
    double price;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    Date suggestionsStartedTime;

    @Column(nullable = false)
    Duration duration;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    OrderSystem orderSystem;
}
