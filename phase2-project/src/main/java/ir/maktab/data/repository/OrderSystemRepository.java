package ir.maktab.data.repository;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderSystemRepository extends JpaRepository<OrderSystem, Long> {
    Optional<OrderSystem> findOrderSystemById(Long id);

    @Transactional
    @Query("select o from OrderSystem o where o.subServices=:subServices and o.orderStatus=:orderStatus or " +
            "o.orderStatus=:orderStatus1 ")
    List<OrderSystem> findBySub(SubServices subServices, OrderStatus orderStatus,OrderStatus orderStatus1);
}
