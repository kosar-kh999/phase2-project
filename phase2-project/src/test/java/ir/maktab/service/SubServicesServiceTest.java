package ir.maktab.service;

import ir.maktab.data.model.SubServices;
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

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubServicesServiceTest {
    @Autowired
    private SubServicesService subServicesService;
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).briefExplanation("KHARDI")
            .build();

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
    public void updateSubServiceTest() throws NotFound {
        SubServices name = subServicesService.findByName(subServices.getSubName());
        name.setBriefExplanation("TAMIR");
        SubServices services = subServicesService.updateSubServices(name);
        Assertions.assertThat(services.getBriefExplanation()).isEqualTo("TAMIR");
    }

    @Test
    @Order(6)
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
}