package ir.maktab.service;

import ir.maktab.data.dto.OrderSystemFilterDto;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Opinion;
import ir.maktab.data.model.OrderSystem;

import java.util.List;

public interface OrderSystemServiceInterface {

    void addOrder(OrderSystem orderSystem);

    OrderSystem getOrderById(Long id);

    OrderSystem addOrderWithSubService(Long id, OrderSystem orderSystem, Long customerId);

    OrderSystem changeOrderStatus(Long id);

    List<OrderSystem> findBySubAndStatus(Long subId, OrderStatus orderStatus, OrderStatus orderStatus1);

    Opinion saveOpinionForExpert(Opinion opinion, Long orderId);

    List<OrderSystem> filterOrder(OrderSystemFilterDto dto);

    int calculateOrders(String email, OrderStatus orderStatus);

    int calculateOrdersExpert(String email, OrderStatus orderStatus);

    List<OrderSystem> viewOrderCustomer(String email, OrderStatus orderStatus);

    List<OrderSystem> viewOrderExpert(String email, OrderStatus orderStatus);

    List<OrderSystem> viewOrdersOfCustomer(String email);

    List<OrderSystem> viewOrderOfExpert(String email);
}
