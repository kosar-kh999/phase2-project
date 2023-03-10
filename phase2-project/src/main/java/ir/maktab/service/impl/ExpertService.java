package ir.maktab.service.impl;

import ir.maktab.data.dto.ExpertFilterDto;
import ir.maktab.data.dto.ExpertSignUpDto;
import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.repository.ExpertRepository;
import ir.maktab.service.ExpertServiceInterface;
import ir.maktab.util.exception.ExistException;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExpertService implements ExpertServiceInterface {
    private final ExpertRepository expertRepository;
    private final SubServicesService subServicesService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JavaMailSender mailSender;

    public ExpertService(ExpertRepository expertRepository, SubServicesService subServicesService,
                         BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender mailSender) {
        this.expertRepository = expertRepository;
        this.subServicesService = subServicesService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSender = mailSender;
    }

    public void signUp(Expert expert, String siteURL) throws MessagingException, UnsupportedEncodingException {
        if (expertRepository.findExpertByEmail(expert.getEmail()).isPresent())
            throw new ExistException("The email is exist");
        expert.setRole(Role.ROLE_EXPERT);
        expert.setScore(0);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setCredit(0);
        expert.setPassword(bCryptPasswordEncoder.encode(expert.getPassword()));
        String randomCode = RandomString.make(64);
        expert.setVerificationCode(randomCode);
        expert.setEnabled(false);
        sendVerificationEmail(expert, siteURL);
        expertRepository.save(expert);
    }

    private void sendVerificationEmail(Expert expert, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = expert.getEmail();
        String fromAddress = "spring.mail.username";
        String senderName = "maktab";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", expert.getFirstName());
        String verifyURL = siteURL + "/expert/verify?code=" + expert.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        Expert user = expertRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            user.setExpertStatus(ExpertStatus.AWAITING_CONFIRMATION);
            expertRepository.save(user);

            return true;
        }

    }

    public Expert changePasswordExpert(String newPassword, String confirmedPassword, String email) {
        Expert expertByEmail = getExpertByEmail(email);
        if (!(newPassword.equals(confirmedPassword)))
            throw new NotCorrect("The new password and confirmed password must be match");
        expertByEmail.setPassword(bCryptPasswordEncoder.encode(newPassword));
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
            if (expert.getMinScore() == 0 && expert.getMaxScore() != 0)
                predicates.add(cb.lt(root.get("score"), expert.getMaxScore()));
            if (expert.getMinScore() != 0 && expert.getMaxScore() == 0)
                predicates.add(cb.gt(root.get("score"), expert.getMinScore()));
            if (expert.getSubName() != null && expert.getSubName().length() != 0) {
                SubServices service = subServicesService.findByName(expert.getSubName());
                Join<Expert, SubServices> expertSub = root.join("subServices");
                predicates.add(cb.equal(expertSub.get("subName"), service.getSubName()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Transactional
    public List<Expert> signUpDateFilter(ExpertSignUpDto expert) {
        return expertRepository.findAll((Specification<Expert>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (expert.getTimeAfter() != null && expert.getTimeBefore() != null)
                predicates.add(cb.between(root.get("entryDate"), expert.getTimeAfter(), expert.getTimeBefore()));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Double viewCredit(String email) {
        Expert expert = getExpertByEmail(email);
        return expert.getCredit();
    }
}
