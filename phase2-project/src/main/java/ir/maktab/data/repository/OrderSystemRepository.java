package ir.maktab.data.repository;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderSystemRepository extends JpaRepository<OrderSystem, Long>, JpaSpecificationExecutor<OrderSystem> {
    Optional<OrderSystem> findOrderSystemById(Long id);

    @Transactional
    @Query("select o from OrderSystem o where o.subServices=:subServices and o.orderStatus=:orderStatus or " +
            "o.orderStatus=:orderStatus1 ")
    List<OrderSystem> findBySub(SubServices subServices, OrderStatus orderStatus, OrderStatus orderStatus1);

    @Transactional
    @Query("select o from OrderSystem o where o.customer=:customer and o.orderStatus=:orderStatus")
    List<OrderSystem> findByStatus(Customer customer, OrderStatus orderStatus);

    @Transactional
    @Query("select o from OrderSystem o where o.expert=:expert and o.orderStatus=:orderStatus")
    List<OrderSystem> findByStatusExpert(Expert expert, OrderStatus orderStatus);

    @Query("select count(o.customer) from OrderSystem o where o.customer=:customer and o.orderStatus=:orderStatus")
    int calculateOrders(Customer customer, OrderStatus orderStatus);

    @Query("select count(o.expert) from OrderSystem o where o.expert=:expert and o.orderStatus=:orderStatus")
    int calculateOrdersExpert(Expert expert, OrderStatus orderStatus);

    @Query("select o from OrderSystem o where o.customer=:customer")
    List<OrderSystem> findAllOrdersOfCustomer(Customer customer);

    @Query("select o from OrderSystem o where o.expert=:expert")
    List<OrderSystem> findAllOrdersOfExpert(Expert expert);
}
