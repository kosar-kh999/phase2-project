package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderSystemRepository extends JpaRepository<OrderSystem, Long> {
    Optional<OrderSystem> findOrderSystemById(Long id);
    @Transactional
    @Query("select o from OrderSystem o where o.subServices.subName=:subName")
    List<OrderSystem> findBySub(String subName);
}
