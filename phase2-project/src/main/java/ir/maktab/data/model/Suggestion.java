package ir.maktab.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

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

    @OneToOne
    Expert expert;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    Date registrationTime;

    @Column(nullable = false)
    double price;

    @Temporal(value = TemporalType.TIME)
    @Column(nullable = false)
    Date suggestionsStartedTime;

    @Column(nullable = false)
    Duration duration;

    @ManyToOne
    OrderSystem orderSystem;
}
