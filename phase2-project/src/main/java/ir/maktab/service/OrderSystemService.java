package ir.maktab.service;

import ir.maktab.data.dto.OrderSystemFilterDto;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.data.repository.OrderSystemRepository;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.OpinionException;
import ir.maktab.util.exception.OrderException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final OrderSystemRepository orderSystemRepository;
    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final OpinionService opinionService;
    private final ExpertService expertService;
    private final MainServicesService mainServicesService;

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
        OrderSystem system = orderSystemRepository.save(orderSystem);
        return system;
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

    public List<OrderSystem> findByStatusAdvice(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.WAITING_ADVICE_EXPERTS)))
            throw new OrderException("the order status must be waiting advice experts");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> findByStatusSelection(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.WAITING_EXPERT_SELECTION)))
            throw new OrderException("the order status must be waiting expert selection");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> findByStatusComePlace(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.WAITING_EXPERT_COME_PLACE)))
            throw new OrderException("the order status must be waiting expert come place");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> findByStatusStarted(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.STARTED)))
            throw new OrderException("the order status must be started");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> findByStatusDone(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.DONE)))
            throw new OrderException("the order status must be done");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> findByStatusPaid(Long customerId, OrderStatus orderStatus) {
        Customer customer = customerService.getById(customerId);
        if (!(orderStatus.equals(OrderStatus.PAID)))
            throw new OrderException("the order status must be paid");
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }


    public Opinion saveOpinionForExpert(Opinion opinion, Long orderId) {
        OrderSystem order = getOrderById(orderId);
        if (opinion.getScore() < 1 || opinion.getScore() > 5)
            throw new OpinionException("The score must be between 1 and 5");
        Expert expert = order.getExpert();
        if (expert.getScore() == 0)
            expert.setScore(opinion.getScore());
        double score = (expert.getScore() + opinion.getScore()) / 2;
        expert.setScore(score);
        opinion.setExpert(order.getExpert());
        expertService.update(expert);
        return opinionService.saveOpinion(opinion);
    }

    @Transactional
    public List<OrderSystem> filterOrder(OrderSystemFilterDto dto) {
        return orderSystemRepository.findAll((Specification<OrderSystem>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto.getOrderStatus() != null)
                predicates.add(cb.equal(root.get("orderStatus"), dto.getOrderStatus()));
            if (dto.getSubName() != null && dto.getSubName().length() != 0) {
                SubServices service = subServicesService.findByName(dto.getSubName());
                Join<OrderSystem, SubServices> join = root.join("subServices");
                predicates.add(cb.equal(join.get("subName"), service.getSubName()));
            }
            if (dto.getTimeBefore() != null && dto.getTimeAfter() != null) {
                predicates.add(cb.between(root.get("timeToDo"), dto.getTimeAfter(), dto.getTimeBefore()));
            }

            if (dto.getName() != null && dto.getName().length() != 0) {
                MainService service = mainServicesService.findByName(dto.getName());
                Root<OrderSystem> orderSystemRoot = cq.from(OrderSystem.class);
                Root<SubServices> subServicesRoot = cq.from(SubServices.class);
                Root<MainService> mainServiceRoot = cq.from(MainService.class);
                cq.select(orderSystemRoot.get("subServices"));
                cq.where(
                        cb.and(
                                cb.equal(orderSystemRoot.get("subServices"), subServicesRoot.get("subName")),
                                cb.equal(mainServiceRoot.get("name"), subServicesRoot.get("mainService")),
                                cb.equal(mainServiceRoot.get("name"), service.getName())
                        )

                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public int calculateOrders(String email, OrderStatus orderStatus) {
        Customer customer = customerService.getCustomerByEmail(email);
        return orderSystemRepository.calculateOrders(customer, orderStatus);
    }

    public int calculateOrdersExpert(String email, OrderStatus orderStatus) {
        Expert expert = expertService.getExpertByEmail(email);
        return orderSystemRepository.calculateOrdersExpert(expert, orderStatus);
    }

    public List<OrderSystem> viewOrderCustomer(String email, OrderStatus orderStatus) {
        Customer customer = customerService.getCustomerByEmail(email);
        return orderSystemRepository.findByStatus(customer, orderStatus);
    }

    public List<OrderSystem> viewOrderExpert(String email, OrderStatus orderStatus) {
        Expert expert = expertService.getExpertByEmail(email);
        return orderSystemRepository.findByStatusExpert(expert, orderStatus);
    }

}
