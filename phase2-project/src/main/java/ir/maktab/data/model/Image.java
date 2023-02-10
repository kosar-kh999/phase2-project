package ir.maktab.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class Image extends BaseEntity {

    private String type;

    @Lob
    private byte[] imageData;
}
