package ir.maktab.data.repository;

import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    @Query("select o from Opinion o where o.expert=:expert")
    List<Opinion> showByScore(Expert expert);
}
