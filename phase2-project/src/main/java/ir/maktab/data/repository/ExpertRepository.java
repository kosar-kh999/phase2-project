package ir.maktab.data.repository;

import ir.maktab.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
}
