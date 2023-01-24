package ir.maktab.data.repository;

import ir.maktab.data.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubServicesRepository extends JpaRepository<SubServices, Long> {
    Optional<SubServices> findBySubName(String name);

    @Modifying
    @Query("update SubServices s set s.briefExplanation=:brief where s.subName=:sub")
    void updateBrief(@Param("brief") String brief, @Param("sub") String sub);

    @Modifying
    @Query("update SubServices s set s.price=:price where s.subName=:sub")
    void updatePrice(@Param("price") double price, @Param("sub") String sub);
}
