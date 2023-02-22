package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpertServiceTest {
    @Autowired
    private ExpertService expertService;
    @Autowired
    private AdminService adminService;


   /* @Test
    @Order(1)
    public void saveNewExpert() {
        Expert expert = Expert.builder().firstName("mona").lastName("noori").email("mona.noori@gmail.com").
                password("123qqqWW").entryDate(new Date()).expertStatus(ExpertStatus.NEW).role(Role.ROLE_EXPERT).build();
        Expert expert1 = Expert.builder().firstName("nader").lastName("babaee").email("nader.babaee@gmail.com").
                password("456eeeRR").entryDate(new Date()).expertStatus(ExpertStatus.NEW).role(Role.ROLE_EXPERT).build();
        expertService.signUp(expert1);
        expertService.signUp(expert);
        Assertions.assertThat(expert.getId()).isGreaterThan(0);
        Assertions.assertThat(expert1.getId()).isGreaterThan(0);
    }
*/
    /*@Test
    public void saveImage() {
        Expert expertByEmail = expertService.getExpertByEmail("mona.noori@gmail.com");
        expertService.saveImage(expertByEmail);
        assertNotNull(expertByEmail);
    }*/

    /*@Test
    public void getImage() {
        Expert expertByEmail = expertService.getExpertByEmail("mona.noori@gmail.com");
        expertService.getImage("mona.noori@gmail.com");
    }*/

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
        Expert expert = expertService.getExpertByEmail("mona.noori@gmail.com");
        expert.setFirstName("sara");
        Expert updateExpert = expertService.update(expert);
        Assertions.assertThat(updateExpert.getFirstName()).isEqualTo("sara");
    }

    /*@Test
    @Order(5)
    public void changePasswordTest() {
        Expert expert = expertService.getExpertByEmail("mona.noori@gmail.com");
        Expert expert1 = expertService.changePassword("111qqqWW", "111qqqWW", expert);
        expertService.update(expert1);
        Assertions.assertThat(expert1.getPassword().equals("111qqqWW"));
    }*/

    @Test
    @Order(6)
    public void editExpertStatusByAdmin() {
        Expert expert = adminService.editStatus("mona.noori@gmail.com");
        Assertions.assertThat(expert.getExpertStatus()).isEqualTo(ExpertStatus.CONFIRMED);
    }

    @Test
    @Order(7)
    public void deleteExpertTest() {
        try {
            Expert expert = expertService.getExpertByEmail("mona.noori@gmail.com");
            expertService.delete(expert);
            Expert expert1 = expertService.getExpertByEmail("mona.noori@gmail.com");
            assertNull(expert1);
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }
}