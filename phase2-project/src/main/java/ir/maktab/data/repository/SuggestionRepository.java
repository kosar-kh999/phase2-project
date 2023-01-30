package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findSuggestionByOrderSystemOrderByPriceAsc(OrderSystem orderSystem);

    Optional<Suggestion> findSuggestionById(Long id);
}
