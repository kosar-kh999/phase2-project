package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.impl.AdminService;
import ir.maktab.service.impl.ExpertService;
import ir.maktab.service.impl.SubServicesService;
import ir.maktab.util.exception.NotFound;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubServicesServiceTest {
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).briefExplanation("KHARDI")
            .build();
    Expert expert = Expert.builder().firstName("mona").lastName("noori").email("mona.noori@gmail.com").
            password("123qqqWW").entryDate(new Date()).expertStatus(ExpertStatus.NEW).role(Role.ROLE_EXPERT).build();
    @Autowired
    private SubServicesService subServicesService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ExpertService expertService;

    @Test
    @Order(1)
    public void saveSubServiceTest() {
        subServicesService.saveSubService(subServices);
        try {
            SubServices services = subServicesService.findByName(subServices.getSubName());
            assertNotNull(services);
        } catch (NotFound e) {
            assertEquals("not found this sub service", e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void getSubServiceByEmail() {
        try {
            subServicesService.findByName(subServices.getSubName());
            Assertions.assertThat(subServices.getSubName()).isEqualTo("LAVAZEM_ASHPAZKHANE");
        } catch (NotFound e) {
            assertEquals("not found this sub service", e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void getSubServiceTest() {
        List<SubServices> allSubServices = subServicesService.getAllSubServices();
        Assertions.assertThat(allSubServices.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void updateSubServiceTest() {
        SubServices name = subServicesService.findByName(subServices.getSubName());
        name.setBriefExplanation("TAMIR");
        SubServices services = subServicesService.updateSubServices(name);
        Assertions.assertThat(services.getBriefExplanation()).isEqualTo("TAMIR");
    }

    /*@Test
    @Order(5)
    public void updatePrice() {
        SubServices service = subServicesService.findByName("RAKHT_SHOOIE");
        subServicesService.updatePrice(200000, "RAKHT_SHOOIE");
        assertEquals(200000, service.getPrice(), 200000);
    }*/

    /*@Test
    @Order(6)
    public void updateBrief() {
        SubServices service = subServicesService.findByName(subServices.getSubName());
        subServicesService.updateBrief("taviz", "LAVAZEM_ASHPAZKHANE");
        assertNotNull(service);
    }*/

    @Test
    @Order(7)
    public void deleteSubServiceTest() {
        try {
            SubServices service = subServicesService.findByName(subServices.getSubName());
            subServicesService.deleteSubServices(service);
            subServicesService.findByName("LAVAZEM_ASHPAZKHANE");
            assertNull(service);
        } catch (NotFound e) {
            assertEquals("not found this sub service", e.getMessage());
        }
    }

    /*@Test
    @Order(8)
    public void addSubServiceByAdmin() {
        SubServices services = SubServices.builder().subName("RAKHT_SHOOIE").price(400000).
                briefExplanation("Based on weight").build();
        adminService.addSubService(services);
        Assertions.assertThat(services.getId()).isGreaterThan(0);
    }*/

    /*@Test
    @Order(9)
    public void addExpertToSubServiceByAdmin() {
        Expert expertByEmail = expertService.getExpertByEmail(expert.getEmail());
        SubServices service = subServicesService.findByName("RAKHT_SHOOIE");
        Expert expertToSubService = adminService.addExpertToSubService(expertByEmail, service);
        Assertions.assertThat(expertToSubService.getId()).isGreaterThan(0);
    }*/

    /*@Test
    @Order(10)
    public void deleteExpertFromSubServicesByAdmin() {
        Expert expertByEmail = expertService.getExpertByEmail(expert.getEmail());
        SubServices service = subServicesService.findByName("RAKHT_SHOOIE");
        boolean deleteExpertFromSubServices = adminService.deleteExpertFromSubServices(expertByEmail, service);
        assertTrue(deleteExpertFromSubServices);
    }*/
}