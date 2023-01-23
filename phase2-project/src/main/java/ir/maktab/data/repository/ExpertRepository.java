package ir.maktab.data.repository;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<Expert> findExpertByEmail(String email);

    Expert getExpertByExpertStatus(ExpertStatus expertStatus);
}
