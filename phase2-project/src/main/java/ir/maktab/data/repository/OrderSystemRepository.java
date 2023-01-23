package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSystemRepository extends JpaRepository<OrderSystem, Long> {
}
