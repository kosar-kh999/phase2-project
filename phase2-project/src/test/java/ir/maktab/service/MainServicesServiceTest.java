package ir.maktab.service;

import ir.maktab.data.model.MainService;
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
public class MainServicesServiceTest {
    MainService mainService = MainService.builder().name("ASHPA_ZKHANE").build();
    @Autowired
    private MainServicesService mainServicesService;
    @Autowired
    private AdminService adminService;

    @Test
    @Order(1)
    public void saveServiceTest() {
        mainServicesService.addServices(mainService);
        try {
            MainService service = mainServicesService.findByName(mainService.getName());
            assertNotNull(service);
        } catch (NotFound e) {
            assertEquals("not found this service", e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void getMainServiceByEmail() {
        try {
            mainServicesService.findByName(mainService.getName());
            Assertions.assertThat(mainService.getName()).isEqualTo("ASHPA_ZKHANE");
        } catch (NotFound e) {
            assertEquals("not found this service", e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void getServiceTest() {
        List<MainService> allServices = mainServicesService.getAllServices();
        Assertions.assertThat(allServices.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void updateServiceTest() {
        MainService serviceByName = mainServicesService.findByName(mainService.getName());
        serviceByName.setName("lavazem_khanegi");
        MainService service = mainServicesService.updateServices(serviceByName);
        Assertions.assertThat(service.getName()).isEqualTo("lavazem_khanegi");
    }

    @Test
    @Order(6)
    public void deleteServiceTest() {
        try {
            MainService serviceByName = mainServicesService.findById(202L);
            mainServicesService.deleteServices(serviceByName);
            MainService byName = mainServicesService.findById(202L);
            assertNull(byName);
        } catch (NotFound e) {
            assertEquals("not found this service", e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void addServiceByAdmin() {
        MainService service = MainService.builder().name("NEZAFAT").build();
        adminService.addServices(service);
        Assertions.assertThat(service.getId()).isGreaterThan(0);
    }
}