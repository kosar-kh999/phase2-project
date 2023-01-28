package ir.maktab.service;

import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.util.exception.NotFound;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
}