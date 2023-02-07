package ir.maktab.data.repository;

import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    @Query("SELECT s from Suggestion s WHERE s.orderSystem=:orderSystem order by s.price desc ")
    List<Suggestion> sortByPrice(OrderSystem orderSystem);

    Optional<Suggestion> findSuggestionById(Long id);


    @Query("SELECT s FROM Suggestion s where s.orderSystem=:orderSystem ORDER BY s.expert.score desc ")
    List<Suggestion> findAllOrderByScore(OrderSystem orderSystem);
}
