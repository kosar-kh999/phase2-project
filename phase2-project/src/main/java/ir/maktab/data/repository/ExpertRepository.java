package ir.maktab.data.repository;

import ir.maktab.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findExpertByEmail(String email);

    Optional<Expert> findExpertById(Long id);

    /*static Specification<Expert> filterUsers(Expert expert) {
        return (users, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (expert.getRole() != null)
                predicates.add(cb.equal(users.get("role"), expert.getRole()));
            if (expert.getFirstName() != null && expert.getFirstName().length() != 0)
                predicates.add(cb.equal(users.get("firstName"), expert.getFirstName()));
            if (expert.getLastName() != null && expert.getLastName().length() != 0)
                predicates.add(cb.equal(users.get("lastName"), expert.getFirstName()));
            if (expert.getEmail() != null && expert.getEmail().length() != 0)
                predicates.add(cb.equal(users.get("email"), expert.getEmail()));
            if (expert.getSubServices() != null && expert.getSubServices().size() != 0)
                predicates.add(cb.equal(users.get("subServices"), expert.getSubServices()));
            if (expert.getScore() < 6)
                cq.orderBy(cb.desc(users.get("score")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }*/
}
