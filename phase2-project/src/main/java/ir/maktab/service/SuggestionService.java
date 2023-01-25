package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.model.Suggestion;
import ir.maktab.data.repository.SuggestionRepository;
import ir.maktab.util.exception.SuggestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void updateSuggestion(Suggestion suggestion) {
        suggestionRepository.save(suggestion);
    }

    public void deleteSuggestion(Suggestion suggestion) {
        suggestionRepository.delete(suggestion);
    }

    public void sendSuggestionFromExpert(Suggestion suggestion, SubServices subServices) throws SuggestionException {
        if (suggestion.getPrice() < subServices.getPrice())
            throw new SuggestionException("Price must be more than original");
        if (suggestion.getSuggestionsStartedTime().before(suggestion.getRegistrationTime()))
            throw new SuggestionException("time that suggested must be after the register time");
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.WAITING_EXPERT_SELECTION);
        saveSuggestion(suggestion);
    }
}
