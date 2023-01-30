package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderSystemRepository extends JpaRepository<OrderSystem, Long> {
    Optional<OrderSystem> findOrderSystemById(Long id);
}
