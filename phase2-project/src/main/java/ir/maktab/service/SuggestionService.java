package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.model.Suggestion;
import ir.maktab.data.repository.SuggestionRepository;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.OrderException;
import ir.maktab.util.exception.SuggestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final SubServicesService subServicesService;
    private final ExpertService expertService;
    private final OrderSystemService orderSystemService;

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
    public Suggestion sendSuggestionFromExpert(Suggestion suggestion, Long subId) {
        SubServices subServices = subServicesService.findById(subId);
        if (suggestion.getPrice() < subServices.getPrice())
            throw new SuggestionException("Price must be more than original");
        if (suggestion.getSuggestionsStartedTime().before(new Date()))
            throw new SuggestionException("time that suggested must be after now");
        suggestion.setExpert(expertService.getExpertById(suggestion.getExpert().getId()));
        suggestion.setOrderSystem(orderSystemService.getOrderById(suggestion.getOrderSystem().getId()));
        return suggestionRepository.save(suggestion);
    }


    public List<Suggestion> sortSuggestionByPrice(OrderSystem orderSystem) {
        List<Suggestion> suggestions = suggestionRepository.findSuggestionByOrderSystemOrderByPriceAsc(orderSystem);
        return suggestionRepository.saveAll(suggestions);
    }

    public Suggestion acceptSuggestion(Suggestion suggestion) {
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.WAITING_EXPERT_COME_PLACE);
        return suggestionRepository.save(suggestion);
    }

    /*@Transactional
    public List<Suggestion> sortSuggestionByExpertScore(Expert expert) {
        return suggestionRepository.findAllOrderByScore(expert);
    }*/

    public Suggestion changeOrderStatusToDone(Suggestion suggestion) {
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.DONE);
        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public Suggestion changeOrderStatusToStarted(OrderSystem orderSystem, Suggestion suggestion) {
        if (suggestion.getSuggestionsStartedTime().before(orderSystem.getTimeToDo()))
            throw new OrderException("time of suggestion from expert must be after the time of order system to do");
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.STARTED);
        return suggestionRepository.save(suggestion);
    }
}
