package ir.maktab.data.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
}
