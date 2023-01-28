package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.repository.OrderSystemRepository;
import ir.maktab.util.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final OrderSystemRepository orderSystemRepository;
    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;

    public void addOrder(OrderSystem orderSystem) {
        orderSystemRepository.save(orderSystem);
    }

    /*public void choseServiceAndSubService(String serviceName, String subName, OrderSystem orderSystem) throws NotFound, OrderException {
        servicesService.getByName(serviceName);
        SubServices subServiceByName = subServicesService.getByName(subName);
        if (orderSystem.getPrice() < subServiceByName.getPrice())
            throw new OrderException("Price must be more than original");
        if (new Date().after(orderSystem.getTimeToDo()))
            throw new OrderException("time and date must be after than today");
        orderSystem.setOrderStatus(OrderStatus.WAITING_ADVICE_EXPERTS);
        addOrder(orderSystem);
    }*/

    public void showOrderToExpert(OrderSystem orderSystem) throws OrderException {
        if (!(orderSystem.getOrderStatus().equals(OrderStatus.WAITING_EXPERT_SELECTION) || orderSystem.getOrderStatus().
                equals(OrderStatus.WAITING_ADVICE_EXPERTS)))
            throw new OrderException("the order status must be WAITING_EXPERT_SELECTION or WAITING_ADVICE_EXPERTS");
    }

}
