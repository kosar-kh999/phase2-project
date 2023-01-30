package ir.maktab.service;

import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.data.model.Suggestion;
import ir.maktab.util.date.DateUtil;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuggestionServiceTest {
    @Autowired
    private SuggestionService suggestionService;
    LocalDateTime localDate1 = LocalDateTime.of(2023, 3, 2, 0, 0);
    Date suggestionTime = DateUtil.localDateTimeToDate(localDate1);
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).
            briefExplanation("KHARID").build();
    Suggestion suggestion = Suggestion.builder().price(400000).suggestionsStartedTime(suggestionTime).
            duration(Duration.ofHours(3)).build();

    @Test
    @Order(1)
    public void saveNewSuggestion() throws SuggestionException {
        Suggestion fromExpert = suggestionService.sendSuggestionFromExpert(suggestion, subServices);
        Assertions.assertThat(fromExpert.getId()).isGreaterThan(0);
    }
}