package ir.maktab.service;

import ir.maktab.data.dto.ExpertFilterDto;
import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.ExpertRepository;
import ir.maktab.util.exception.ExistException;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExpertService {
    private final ExpertRepository expertRepository;
    private final SubServicesService subServicesService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ExpertService(ExpertRepository expertRepository, SubServicesService subServicesService,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.expertRepository = expertRepository;
        this.subServicesService = subServicesService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void signUp(Expert expert) {
        if (expertRepository.findExpertByEmail(expert.getEmail()).isPresent())
            throw new ExistException("The email is exist");
        expert.setRole(Role.ROLE_EXPERT);
        expert.setScore(0);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setCredit(0);
        expert.setPassword(bCryptPasswordEncoder.encode(expert.getPassword()));
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

    @Transactional
    public List<Expert> getExperts(ExpertFilterDto expert) {
        SubServices service = subServicesService.findByName(expert.getSubName());
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
            if (expert.getScore() < 6)
                cq.orderBy(cb.desc(root.get("score")));
            if (service.getSubName() != null && service.getSubName().length() != 0) {
                Join<Expert, SubServices> expertSub = root.join("subServices");
                predicates.add(cb.equal(expertSub.get("subName"), expert.getSubName()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
