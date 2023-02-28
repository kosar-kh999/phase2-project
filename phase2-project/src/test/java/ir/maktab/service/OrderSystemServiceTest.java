package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.impl.OrderSystemService;
import ir.maktab.util.date.DateUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderSystemServiceTest {
    SubServices subServices = SubServices.builder().subName("LAVAZEM_ASHPAZKHANE").price(300000).
            briefExplanation("KHARID").build();
    LocalDateTime localDate1 = LocalDateTime.of(2023, 3, 1, 0, 0);
    Date timeToDo = DateUtil.localDateTimeToDate(localDate1);
    OrderSystem orderSystem = OrderSystem.builder().price(400000).description("JA_BE_JAEE").timeToDo(timeToDo).
            address("qeshm").orderStatus(OrderStatus.WAITING_ADVICE_EXPERTS).expert(null).build();
    @Autowired
    private OrderSystemService orderSystemService;

    /*@Test
    @Order(1)
    public void saveNewOrder() {
        OrderSystem order = orderSystemService.addOrderWithSubService(subServices, orderSystem);
        Assertions.assertThat(order.getId()).isGreaterThan(0);
    }*/

    /*@Test
    @Order(2)
    public void showOrderToExpert() {
        assertDoesNotThrow(() -> orderSystemService.showOrderToExpert(orderSystem));
    }*/
}