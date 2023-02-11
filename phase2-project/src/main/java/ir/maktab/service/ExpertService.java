package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.data.repository.ExpertRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;

    public void signUp(Expert expert) {
        expert.setRole(Role.EXPORT);
        expert.setScore(0);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setCredit(0);
        expertRepository.save(expert);
    }

    public Expert signIn(String email, String password) {
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
        if (!(expert.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return expert;
    }


    public Expert changePasswordExpert(String newPassword, String confirmedPassword, String email) {
        Expert expertByEmail = getExpertByEmail(email);
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        expertByEmail.setPassword(newPassword);
        return expertRepository.save(expertByEmail);
    }

    public List<Expert> getAll() {
        return expertRepository.findAll();
    }

    public void delete(Expert expert) {
        Expert expert1 = getExpertByEmail(expert.getEmail());
        expert.setId(expert1.getId());
        expertRepository.delete(expert);
    }

    public Expert update(Expert expert) {
        Expert expert1 = getExpertByEmail(expert.getEmail());
        expert.setId(expert1.getId());
        return expertRepository.save(expert);
    }

    public Expert getExpertByEmail(String email) {
        return expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
    }

    public Expert getExpertById(Long id) {
        return expertRepository.findExpertById(id).orElseThrow(() -> new NotFoundUser("This user is not found"));
    }

    public List<Expert> getExperts(Expert expert) {
        return expertRepository.findAll((Specification<Expert>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (expert.getRole() != null)
                predicates.add(cb.equal(root.get("role"), expert.getRole()));
            if (expert.getFirstName() != null && expert.getFirstName().length() != 0)
                predicates.add(cb.equal(root.get("firstName"), expert.getFirstName()));
            if (expert.getLastName() != null && expert.getLastName().length() != 0)
                predicates.add(cb.equal(root.get("lastName"), expert.getLastName()));
            if (expert.getEmail() != null && expert.getEmail().length() != 0)
                predicates.add(cb.equal(root.get("email"), expert.getEmail()));
            if (expert.getSubServices() != null && expert.getSubServices().size() != 0)
                predicates.add(cb.equal(root.get("subServices"), expert.getSubServices()));
            if (expert.getScore() < 6)
                cq.orderBy(cb.desc(root.get("score")));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
