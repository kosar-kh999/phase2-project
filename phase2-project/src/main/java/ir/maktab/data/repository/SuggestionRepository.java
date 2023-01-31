package ir.maktab.data.repository;

import ir.maktab.data.model.Expert;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findSuggestionByOrderSystemOrderByPriceAsc(OrderSystem orderSystem);

    Optional<Suggestion> findSuggestionById(Long id);

    @Modifying
    @Transactional
    @Query("SELECT s FROM Suggestion s ORDER BY s.expert.score ASC ")
    List<Suggestion> findAllOrderByScore(Expert expert);
}
