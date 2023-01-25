package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByOrderSystemOrderByPrice(OrderSystem orderSystem);


}
