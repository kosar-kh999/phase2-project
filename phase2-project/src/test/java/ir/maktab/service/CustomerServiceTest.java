package ir.maktab.service;

import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Customer;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTest {
    Customer customer = Customer.builder().firstName("lale").lastName("kamali").email("lale.kamali@gmail.com").credit(100000).
            entryDate(new Date()).password("lale1KKK").role(Role.CUSTOMER).build();
    @Autowired
    private CustomerService customerService;

    @Test
    @Order(1)
    public void saveCustomerTest() {
        customerService.signUp(customer);
        try {
            Customer customer1 = customerService.getCustomerByEmail(customer.getEmail());
            assertNotNull(customer1);
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void getCustomerByEmail() {
        try {
            Customer customer1 = customerService.getCustomerByEmail(customer.getEmail());
            Assertions.assertThat(customer1.getEmail()).isEqualTo("lale.kamali@gmail.com");
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void getCustomersTest() {
        List<Customer> customers = customerService.getAll();
        Assertions.assertThat(customers.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void updateCustomerTest() {
        Customer customerByEmail = customerService.getCustomerByEmail(customer.getEmail());
        customerByEmail.setLastName("jarare");
        Customer update = customerService.update(customerByEmail);
        Assertions.assertThat(update.getLastName()).isEqualTo("jarare");
    }

    /*@Test
    @Order(5)
    public void changePasswordTest() {
        Customer customerByEmail = customerService.getCustomerByEmail(customer.getEmail());
        Customer changePassword = customerService.changePassword("lale1JJJ", "lale1JJJ",
                customerByEmail);
        customerService.update(changePassword);
        Assertions.assertThat(changePassword.getPassword().equals("lale1JJJ"));
    }*/

    @Test
    @Order(6)
    public void deleteCustomerTest() {
        try {
            Customer customerByEmail = customerService.getCustomerByEmail(customer.getEmail());
            customerService.delete(customerByEmail);
            Customer byEmail = customerService.getCustomerByEmail("lale.kamali@gmail.com");
            assertNull(byEmail);
        } catch (NotFoundUser e) {
            assertEquals("This email is not exist", e.getMessage());
        }
    }
}