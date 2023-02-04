package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.util.date.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuggestionServiceTest {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private OrderSystemService orderSystemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ExpertService expertService;

    LocalDateTime localDate1 = LocalDateTime.of(2023, 3, 2, 0, 0);
    Date suggestionTime = DateUtil.localDateTimeToDate(localDate1);
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).
            briefExplanation("KHARID").build();

    Suggestion suggestion = Suggestion.builder().price(450000).suggestionsStartedTime(suggestionTime).
            duration(Duration.ofHours(3)).orderSystem(null).expert(null).build();

    Suggestion suggestion1 = Suggestion.builder().price(500000).suggestionsStartedTime(suggestionTime).
            duration(Duration.ofHours(2)).orderSystem(null).expert(null).build();

    @Test
    @Order(1)
    public void saveNewSuggestion() {
        OrderSystem suggestionById = orderSystemService.getSuggestionById(1L);
        suggestion.setOrderSystem(suggestionById);
        Suggestion fromExpert = suggestionService.sendSuggestionFromExpert(suggestion, subServices);
        suggestion1.setOrderSystem(suggestionById);
        Suggestion fromExpert1 = suggestionService.sendSuggestionFromExpert(suggestion1, subServices);
        Assertions.assertThat(fromExpert.getId()).isGreaterThan(0);
        Assertions.assertThat(fromExpert1.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void changeOrderStatus() {
        OrderSystem suggestionById = orderSystemService.getSuggestionById(1L);
        orderSystemService.changeOrderStatus(suggestionById);
        assertNotNull(suggestionById);
    }

    @Test
    @Order(3)
    public void sortSuggestionByPrice() {
        OrderSystem orderSystem = orderSystemService.getSuggestionById(1L);
        List<Suggestion> suggestions = suggestionService.sortSuggestionByPrice(orderSystem);
        assertEquals(450000, suggestions.get(0).getPrice());
    }

    @Test
    @Order(4)
    public void acceptSuggestionStatus() {
        Suggestion suggestionById = suggestionService.getSuggestionById(1L);
        OrderStatus orderStatus = OrderStatus.WAITING_EXPERT_COME_PLACE;
        Suggestion acceptSuggestion = suggestionService.acceptSuggestion(suggestionById);
        assertEquals(acceptSuggestion.getOrderSystem().getOrderStatus(), orderStatus);
    }

    @Test
    @Order(5)
    public void changeOrderStatusToStarted() {
        OrderStatus orderStatus = OrderStatus.STARTED;
        Suggestion suggestionById = suggestionService.getSuggestionById(1L);
        OrderSystem orderSystem = orderSystemService.getSuggestionById(1L);
        Suggestion status = customerService.changeOrderStatusToStarted(orderSystem, suggestionById);
        assertEquals(status.getOrderSystem().getOrderStatus(), orderStatus);
    }

    @Test
    @Order(6)
    public void withdrawTest() {
        Suggestion suggestionById = suggestionService.getSuggestionById(602L);
        Customer customerByEmail = customerService.getCustomerByEmail("lale.kamali@gmail.com");
        Customer customer = customerService.withdraw(customerByEmail, suggestionById);
        assertEquals(customer.getCredit(), 550000);
    }

    @Test
    @Order(7)
    public void deposit() {
        Suggestion suggestionById = suggestionService.getSuggestionById(502L);
        Expert expertByEmail = expertService.getExpertByEmail("mona.noori@gmail.com");
        Expert expert = expertService.deposit(expertByEmail, suggestionById);
        assertEquals(expert.getCredit(), 450000);
    }

    @Test
    @Order(8)
    public void changeOrderStatusToDone() {
        OrderStatus orderStatus = OrderStatus.DONE;
        Suggestion suggestionById = suggestionService.getSuggestionById(1L);
        Suggestion status = customerService.changeOrderStatusToDone(suggestionById);
        assertEquals(status.getOrderSystem().getOrderStatus(), orderStatus);
    }
}