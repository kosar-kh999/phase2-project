package ir.maktab.data.repository;

import ir.maktab.data.model.MainService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainServicesRepository extends JpaRepository<MainService, Long> {
    Optional<MainService> findByName(String name);

    Optional<MainService> findById(Long id);
}
