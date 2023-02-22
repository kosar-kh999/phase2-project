package ir.maktab.data.repository;

import ir.maktab.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ExpertRepository extends JpaRepository<Expert, Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findExpertByEmail(String email);

    Optional<Expert> findExpertById(Long id);

    @Query("SELECT u FROM Expert u WHERE u.verificationCode = ?1")
    Expert findByVerificationCode(String code);
}
