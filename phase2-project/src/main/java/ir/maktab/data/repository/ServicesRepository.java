package ir.maktab.data.repository;

import ir.maktab.data.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByName(String name);
}
