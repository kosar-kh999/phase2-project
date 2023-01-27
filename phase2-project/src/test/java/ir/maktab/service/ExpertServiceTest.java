package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpertServiceTest {
    @Autowired
    private ExpertService expertService;

    @Order(1)
    @Test
    public void saveNewExpert() {
        Expert expert = Expert.builder().firstName("mona").lastName("noori").email("mona.noori@gmail.com").password("123qqqWW")
                .entryDate(new Date()).credit(100000).expertStatus(ExpertStatus.NEW).role(Role.EXPORT).build();
        expertService.signUp(expert);
        Assertions.assertThat(expert.getId()).isGreaterThan(0);
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

    @Order(3)
    @Test
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
    public void getExpertByStatus() {
        Expert expert = expertService.getStatus(ExpertStatus.NEW);
        assertEquals("NEW", expert.getExpertStatus().name());
    }

    @Test
    @Order(6)
    public void changePasswordTest(){

    }
}