package ir.maktab.service;

import ir.maktab.data.enums.ActiveExpert;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.data.repository.SuggestionRepository;
import ir.maktab.util.date.DateUtil;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.OrderException;
import ir.maktab.util.exception.SuggestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final SubServicesService subServicesService;
    private final ExpertService expertService;
    private final OrderSystemService orderSystemService;
    private final CustomerService customerService;

    public void saveSuggestion(Suggestion suggestion) {
        suggestionRepository.save(suggestion);
    }

    public List<Suggestion> getAllSuggestion() {
        return suggestionRepository.findAll();
    }

    public Suggestion updateSuggestion(Suggestion suggestion) {
        return suggestionRepository.save(suggestion);
    }

    public void deleteSuggestion(Suggestion suggestion) {
        suggestionRepository.delete(suggestion);
    }

    public Suggestion getSuggestionById(Long id) {
        return suggestionRepository.findSuggestionById(id).orElseThrow(() -> new NotFound("Not found this suggestion"));
    }

    @Transactional
    public Suggestion sendSuggestionFromExpert(Suggestion suggestion, Long subId, Long expertId) {
        SubServices subServices = subServicesService.findById(subId);
        Expert expert = expertService.getExpertById(expertId);
        if (suggestion.getPrice() < subServices.getPrice())
            throw new SuggestionException("Price must be more than original");
        if (suggestion.getSuggestionsStartedTime().before(new Date()))
            throw new SuggestionException("time that suggested must be after now");
        suggestion.setExpert(expert);
        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public Suggestion setOrder(Long suggestionId, Long orderId) {
        Suggestion suggestion = getSuggestionById(suggestionId);
        OrderSystem order = orderSystemService.getOrderById(orderId);
        suggestion.setOrderSystem(order);
        return suggestionRepository.save(suggestion);
    }

    public List<Suggestion> sortSuggestionByPrice(Long id) {
        OrderSystem orderSystem = orderSystemService.getOrderById(id);
        return suggestionRepository.sortByPrice(orderSystem);
    }

    public Suggestion acceptSuggestion(Long suggestionId, Long expertId, Long orderId) {
        Suggestion suggestion = getSuggestionById(suggestionId);
        Expert expert = expertService.getExpertById(expertId);
        OrderSystem order = orderSystemService.getOrderById(orderId);
        order.setExpert(expert);
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.WAITING_EXPERT_COME_PLACE);
        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public List<Suggestion> sortSuggestionByExpertScore(Long id) {
        OrderSystem orderSystem = orderSystemService.getOrderById(id);
        return suggestionRepository.findAllOrderByScore(orderSystem);
    }

    public Suggestion changeOrderStatusToDone(Long id) {
        Suggestion suggestion = getSuggestionById(id);
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.DONE);
        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public Suggestion changeOrderStatusToStarted(Long orderId, Long suggestionId) {
        OrderSystem orderSystem = orderSystemService.getOrderById(orderId);
        Suggestion suggestion = getSuggestionById(suggestionId);
        if (suggestion.getSuggestionsStartedTime().before(orderSystem.getTimeToDo()))
            throw new OrderException("time of suggestion from expert must be after the time of order system to do");
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.STARTED);
        return suggestionRepository.save(suggestion);
    }

    public Expert checkDuration(Long orderId, Long suggestionId, Long expertId) {
        Expert expert = expertService.getExpertById(expertId);
        OrderSystem orderSystem = orderSystemService.getOrderById(orderId);
        Suggestion suggestion = getSuggestionById(suggestionId);
        LocalDateTime doneOrder = DateUtil.changeDateToLocalDateTime(orderSystem.getDoneDate());
        LocalDateTime doneSuggestion = DateUtil.changeDateToLocalDateTime(suggestion.getDoneDateExpert());
        long hours = Duration.between(doneSuggestion, doneOrder).toHours();
        double score = expert.getScore() - hours;
        expert.setScore(score);
        if (score < 0)
            expert.setActiveExpert(ActiveExpert.NOT_ACTIVE);
        expertService.update(expert);
        return expert;
    }

    public Customer withdraw(Long customerId, Long suggestionId) {
        Customer customer = customerService.getById(customerId);
        Suggestion suggestion = getSuggestionById(suggestionId);
        Customer customerByEmail = customerService.getCustomerByEmail(customer.getEmail());
        ;
        if (suggestion.getPrice() > customerByEmail.getCredit())
            throw new SuggestionException("the amount of customer is should more than suggestion");
        double withdraw = customerByEmail.getCredit() - suggestion.getPrice();
        customer.setCredit(withdraw);
        return customerService.update(customer);
    }

    public Expert deposit(Long expertId, Long suggestionId) {
        Expert expert = expertService.getExpertById(expertId);
        Suggestion suggestion = getSuggestionById(suggestionId);
        double deposit = expert.getCredit() + (0.7 * suggestion.getPrice());
        expert.setCredit(deposit);
        return expertService.update(expert);
    }
}
