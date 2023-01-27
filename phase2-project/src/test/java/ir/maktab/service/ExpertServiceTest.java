package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpertServiceTest {
    @Autowired
    private ExpertService expertService;

    @Test
    @Order(1)
    public void saveNewExpert() {
        Expert expert = Expert.builder().firstName("mona").lastName("noori").email("mona.noori@gmail.com").password("123qqqWW")
                .entryDate(new Date()).credit(100000).expertStatus(ExpertStatus.NEW).role(Role.EXPORT).build();
        Expert expert1 = Expert.builder().firstName("babak").lastName("vahedi").email("babak.vahedi@gmail.com").password("222qqqWW")
                .entryDate(new Date()).credit(200000).expertStatus(ExpertStatus.NEW).role(Role.EXPORT).build();
        expertService.signUp(expert);
        expertService.signUp(expert1);
        Assertions.assertThat(expert.getId()).isGreaterThan(0);
        Assertions.assertThat(expert1.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getExpertByEmail() {
        try {
            Expert expert = expertService.getExpertByEmail("mona.noori@gmail.com");
            Assertions.assertThat(expert.getEmail()).isEqualTo("mona.noori@gmail.com");
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }


    @Test
    @Order(3)
    public void getExpertsTest() {
        List<Expert> experts = expertService.getAll();
        Assertions.assertThat(experts.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void updateExpertTest() {
        try {
            Expert expert = expertService.getExpertByEmail("mona.noori@gmail.com");
            expert.setEmail("mona.noori111@gmail.com");
            Expert updateExpert = expertService.update(expert);
            Assertions.assertThat(updateExpert.getEmail()).isEqualTo("mona.noori111@gmail.com");
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void changePasswordTest() {
        try {
            Expert expert = expertService.getExpertByEmail("mona.noori111@gmail.com");
            try {
                Expert expert1 = expertService.changePassword("111qqqWW", "111qqqWW", expert);
                expertService.update(expert1);
                Assertions.assertThat(expert1.getPassword().equals("111qqqWW"));
            } catch (NotCorrect e) {
                assertEquals("The new password and confirmed password must be match", e.getMessage());
            }
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }

    @Test
    @Order(6)
    public void deleteExpertTest() {
        try {
            Expert expert = expertService.getExpertByEmail("babak.vahedi@gmail.com");
            expertService.delete(expert);
            Expert expert1 = expertService.getExpertByEmail("babak.vahedi@gmail.com");
            assertNull(expert1);
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }
}