package ir.maktab.service;

import ir.maktab.data.dto.ExpertFilterDto;
import ir.maktab.data.dto.ExpertSignUpDto;
import ir.maktab.data.model.Expert;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ExpertServiceInterface {

    void signUp(Expert expert, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);

    Expert changePasswordExpert(String newPassword, String confirmedPassword, String email);

    List<Expert> getAll();

    void delete(Expert expert);

    Expert update(Expert expert);

    Expert getExpertByEmail(String email);

    Expert getExpertById(Long id);

    List<Expert> getExperts(ExpertFilterDto expert);

    List<Expert> signUpDateFilter(ExpertSignUpDto expert);

    Double viewCredit(String email);
}
