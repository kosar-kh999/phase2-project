package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.OrderSystemRepository;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final OrderSystemRepository orderSystemRepository;

    public void addOrder(OrderSystem orderSystem) {
        orderSystemRepository.save(orderSystem);
    }

    public OrderSystem getSuggestionById(Long id) {
        return orderSystemRepository.findOrderSystemById(id).orElseThrow(() -> new NotFound("Not found this order"));
    }

    @Transactional
    public OrderSystem addOrderWithSubService(SubServices subServices, OrderSystem orderSystem) {
        if (orderSystem.getPrice() < subServices.getPrice())
            throw new OrderException("Price must be more than original");
        if (new Date().after(orderSystem.getTimeToDo()))
            throw new OrderException("time and date must be after than today");
        orderSystem.setOrderStatus(OrderStatus.WAITING_ADVICE_EXPERTS);
        return orderSystemRepository.save(orderSystem);
    }

    public void showOrderToExpert(OrderSystem orderSystem)  {
        if (!(orderSystem.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_SELECTION) || orderSystem.getOrderStatus().
                equals(OrderStatus.WAITING_ADVICE_EXPERTS)))
            throw new OrderException("the order status must be WAITING_EXPERT_SELECTION or WAITING_ADVICE_EXPERTS");
    }

    public void changeOrderStatus(OrderSystem orderSystem) {
        orderSystem.setOrderStatus(OrderStatus.WAITING_EXPERT_SELECTION);
        orderSystemRepository.save(orderSystem);
    }

}
