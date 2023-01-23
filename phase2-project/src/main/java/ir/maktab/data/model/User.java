package ir.maktab.data.model;

import ir.maktab.data.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User extends BaseEntity {

    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(unique = true, nullable = false)
    String email;
    @Enumerated(value = EnumType.STRING)
    Role role;
    @Column(nullable = false, length = 8)
    String password;
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    Date entryDate;
    double credit;
}
