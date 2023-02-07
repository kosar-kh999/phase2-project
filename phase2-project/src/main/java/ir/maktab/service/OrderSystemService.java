package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.OrderSystemRepository;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final OrderSystemRepository orderSystemRepository;
    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final ExpertService expertService;

    public void addOrder(OrderSystem orderSystem) {
        orderSystemRepository.save(orderSystem);
    }

    public OrderSystem getOrderById(Long id) {
        return orderSystemRepository.findOrderSystemById(id).orElseThrow(() -> new NotFound("Not found this order"));
    }

    @Transactional
    public OrderSystem addOrderWithSubService(Long id, OrderSystem orderSystem, Long customerId) {
        SubServices subServices = subServicesService.findById(id);
        Customer customer = customerService.getById(customerId);
        if (orderSystem.getPrice() < subServices.getPrice())
            throw new OrderException("Price must be more than original");
        if (new Date().after(orderSystem.getTimeToDo()))
            throw new OrderException("time and date must be after than today");
        orderSystem.setSubServices(subServices);
        orderSystem.setCustomer(customer);
        orderSystem.setOrderStatus(OrderStatus.WAITING_ADVICE_EXPERTS);
        return orderSystemRepository.save(orderSystem);
    }

    public OrderSystem changeOrderStatus(Long id) {
        OrderSystem orderSystem = getOrderById(id);
        orderSystem.setOrderStatus(OrderStatus.WAITING_EXPERT_SELECTION);
        return orderSystemRepository.save(orderSystem);
    }

    public List<OrderSystem> findBySubAndStatus(Long subId, OrderStatus orderStatus, OrderStatus orderStatus1) {
        SubServices subServices = subServicesService.findById(subId);
        if (!(orderStatus.equals(OrderStatus.WAITING_EXPERT_SELECTION) || orderStatus1.
                equals(OrderStatus.WAITING_ADVICE_EXPERTS)))
            throw new OrderException("the order status must be WAITING_EXPERT_SELECTION or WAITING_ADVICE_EXPERTS");
        return orderSystemRepository.findBySub(subServices, orderStatus, orderStatus1);
    }
}
