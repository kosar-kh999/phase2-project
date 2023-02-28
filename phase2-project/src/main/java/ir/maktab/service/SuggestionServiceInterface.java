package ir.maktab.service;

import ir.maktab.data.model.Customer;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Suggestion;

import java.util.List;

public interface SuggestionServiceInterface {

    void saveSuggestion(Suggestion suggestion);

    List<Suggestion> getAllSuggestion();

    Suggestion updateSuggestion(Suggestion suggestion);

    void deleteSuggestion(Suggestion suggestion);

    Suggestion getSuggestionById(Long id);

    Suggestion sendSuggestionFromExpert(Suggestion suggestion, Long orderId, Long expertId);

    List<Suggestion> sortSuggestionByPrice(Long id);

    Suggestion acceptSuggestion(Long suggestionId, Long expertId, Long orderId);

    List<Suggestion> sortSuggestionByExpertScore(Long id);

    Suggestion changeOrderStatusToStarted(Long orderId, Long suggestionId);

    Expert checkDuration(Long orderId, Long suggestionId, Long expertId);

    Customer withdraw(Long customerId, Long suggestionId);

    Expert deposit(Long expertId, Long suggestionId);

    List<Suggestion> getAllSuggestionFromExpert(Long expertId);
}
