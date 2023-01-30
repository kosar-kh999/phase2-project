package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.model.Suggestion;
import ir.maktab.util.date.DateUtil;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.SuggestionException;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuggestionServiceTest {
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private OrderSystemService orderSystemService;
    LocalDateTime localDate1 = LocalDateTime.of(2023, 3, 2, 0, 0);
    Date suggestionTime = DateUtil.localDateTimeToDate(localDate1);
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).
            briefExplanation("KHARID").build();
    Date timeToDo = DateUtil.localDateTimeToDate(localDate1);
    /*OrderSystem orderSystem = OrderSystem.builder().price(400000).description("JA_BE_JAEE").timeToDo(timeToDo).
            address("qeshm").orderStatus(OrderStatus.WAITING_ADVICE_EXPERTS).build();*/
    Suggestion suggestion = Suggestion.builder().price(450000).suggestionsStartedTime(suggestionTime).
            duration(Duration.ofHours(3)).orderSystem(null).build();

    Suggestion suggestion1 = Suggestion.builder().price(500000).suggestionsStartedTime(suggestionTime).
            duration(Duration.ofHours(2)).orderSystem(null).build();

    @Test
    @Order(1)
    public void saveNewSuggestion() throws SuggestionException, NotFound {
        OrderSystem suggestionById = orderSystemService.getSuggestionById(302L);
        suggestion.setOrderSystem(suggestionById);
        Suggestion fromExpert = suggestionService.sendSuggestionFromExpert(suggestion, subServices);
        suggestion1.setOrderSystem(suggestionById);
        Suggestion fromExpert1 = suggestionService.sendSuggestionFromExpert(suggestion1, subServices);
        Assertions.assertThat(fromExpert.getId()).isGreaterThan(0);
        Assertions.assertThat(fromExpert1.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void changeOrderStatus() throws NotFound {
        OrderSystem suggestionById = orderSystemService.getSuggestionById(302L);
        orderSystemService.changeOrderStatus(suggestionById);
        assertNotNull(suggestionById);
    }

    @Test
    @Order(3)
    public void sortSuggestionByPrice() throws NotFound {
        OrderSystem orderSystem = orderSystemService.getSuggestionById(302L);
        List<Suggestion> suggestions = suggestionService.sortSuggestionByPrice(orderSystem);
        Assertions.assertThat(suggestions.size()).isGreaterThan(0);
    }
}