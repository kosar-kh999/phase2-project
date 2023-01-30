package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.data.repository.SuggestionRepository;
import ir.maktab.util.exception.NotFound;
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

    public Suggestion getSuggestionById(Long id) throws NotFound {
        return suggestionRepository.findSuggestionById(id).orElseThrow(() -> new NotFound("Not found this suggestion"));
    }

    @Transactional
    public Suggestion sendSuggestionFromExpert(Suggestion suggestion, SubServices subServices) throws SuggestionException {
        if (suggestion.getPrice() < subServices.getPrice())
            throw new SuggestionException("Price must be more than original");
        if (suggestion.getSuggestionsStartedTime().before(new Date()))
            throw new SuggestionException("time that suggested must be after now");
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

}
