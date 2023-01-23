package ir.maktab.data.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@ToString
public class Suggestion extends BaseEntity {
    @OneToOne
    Expert expert;
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    Date registrationTime;
    double price;
    @Temporal(value = TemporalType.TIME)
    Date suggestionsStartedTime;
    Duration duration;
    @ManyToOne
    OrderSystem orderSystem;
}
