package ir.maktab.data.model;

import ir.maktab.data.enums.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@SuperBuilder
public class Expert extends User {

    @Enumerated(value = EnumType.STRING)
    ExpertStatus expertStatus;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, targetEntity = SubServices.class)
    @JoinTable(name = "EXPERT_SUB",
            joinColumns = {@JoinColumn(name = "expert_id")},
            inverseJoinColumns = {@JoinColumn(name = "sub_id")})
    List<SubServices> subServices = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Opinion> opinions = new ArrayList<>();

    @Lob
    private byte[] image;
    double score;
    boolean isConfirmed;
}
