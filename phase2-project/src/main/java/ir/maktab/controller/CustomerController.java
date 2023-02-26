package ir.maktab.controller;

import ir.maktab.captcha.ValidateCaptcha;
import ir.maktab.data.dto.*;
import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.service.CustomerService;
import ir.maktab.service.OrderSystemService;
import ir.maktab.service.SubServicesService;
import ir.maktab.service.SuggestionService;
import ir.maktab.util.exception.ForbiddenException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final OrderSystemService orderSystemService;
    private final SuggestionService suggestionService;

    private final ValidateCaptcha validateCaptcha;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, SubServicesService subServicesService,
                              OrderSystemService orderSystemService, SuggestionService suggestionService, ValidateCaptcha validateCaptcha, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.subServicesService = subServicesService;
        this.orderSystemService = orderSystemService;
        this.suggestionService = suggestionService;
        this.validateCaptcha = validateCaptcha;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add_customer")
    public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerService.signUp(customer);
        return ResponseEntity.ok().body("You sign up successfully");
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<CustomerSignInDto>> getAllExpert() {
        return ResponseEntity.ok().body(customerService.getAll().stream().map(customer -> modelMapper.
                map(customer, CustomerSignInDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/update_customer")
    public ResponseEntity<CustomerSignInDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Customer update = customerService.update(customer);
        CustomerSignInDto dto = modelMapper.map(update, CustomerSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("delete_customer")
    public ResponseEntity<String> deleteCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerService.delete(customer);
        return ResponseEntity.ok().body("This customer delete");
    }

    @GetMapping("find_customer")
    public ResponseEntity<CustomerSignInDto> findCustomerByEmail(@RequestParam("email") String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        CustomerSignInDto dto = modelMapper.map(customer, CustomerSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/change_password")
    public ResponseEntity<CustomerSignInDto> updatePassword(@RequestParam("newPassword") String newPassword,
                                                            @RequestParam("confirmedPassword") String confirmedPassword) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.changePasswordCustomer(newPassword, confirmedPassword, principal.getEmail());
        CustomerSignInDto dto = modelMapper.map(customer, CustomerSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }


    @GetMapping("/all_services")
    public ResponseEntity<List<MainServiceDto>> getAllServices() {
        return ResponseEntity.ok().body(customerService.showAllServices().stream().map(mainService -> modelMapper.
                map(mainService, MainServiceDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/all_sub_services_of_service")
    public ResponseEntity<List<SubServiceDto>> getAllSubServiceOfService(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok().body(subServicesService.getSubServiceByMainService(name).stream().
                map(subServices -> modelMapper.map(subServices, SubServiceDto.class)).collect(Collectors.toList()));
    }

    @Transactional
    @PostMapping("/add_order")
    public ResponseEntity<OrderSystemDto> addOrder(@Valid @RequestBody OrderSystemDto orderSystemDto,
                                                   @RequestParam(value = "subId") Long subId) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrderSystem orderSystem = modelMapper.map(orderSystemDto, OrderSystem.class);
        OrderSystem order = orderSystemService.addOrderWithSubService(subId, orderSystem, principal.getId());
        OrderSystemDto dto = modelMapper.map(order, OrderSystemDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/sort_by_price")
    public ResponseEntity<List<SuggestionDto>> sortByPrice(@RequestParam(value = "orderId") Long orderId) {
        return ResponseEntity.ok().body(suggestionService.sortSuggestionByPrice(orderId).stream().
                map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/sort_by_score")
    public ResponseEntity<List<SuggestionDto>> sortByScore(@RequestParam(value = "orderId") Long orderId) {
        return ResponseEntity.ok().body(suggestionService.sortSuggestionByExpertScore(orderId).stream().
                map(suggestion -> modelMapper.map(suggestion, SuggestionDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/choose_expert")
    public ResponseEntity<SuggestionDto> chooseExpert(@RequestParam(value = "suggestionId") Long suggestionId,
                                                      @RequestParam(value = "expertId") Long expertId,
                                                      @RequestParam(value = "orderId") Long orderId) {
        Suggestion suggestion = suggestionService.acceptSuggestion(suggestionId, expertId, orderId);
        SuggestionDto dto = modelMapper.map(suggestion, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/change_status_to_started")
    public ResponseEntity<SuggestionDto> changeStatusToStarted(@RequestParam(value = "orderId") Long orderId,
                                                               @RequestParam(value = "suggestionId") Long suggestionId) {
        Suggestion suggestion = suggestionService.changeOrderStatusToStarted(orderId, suggestionId);
        SuggestionDto dto = modelMapper.map(suggestion, SuggestionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/update_score")
    public ResponseEntity<ExpertDto> updateScore(@RequestParam(value = "suggestionId") Long suggestionId,
                                                 @RequestParam(value = "expertId") Long expertId,
                                                 @RequestParam(value = "orderId") Long orderId) {
        Expert expert = suggestionService.checkDuration(orderId, suggestionId, expertId);
        ExpertDto dto = modelMapper.map(expert, ExpertDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/add_opinion")
    public ResponseEntity<OpinionDto> addOpinion(@Valid @RequestBody OpinionDto opinionDto,
                                                 @RequestParam(value = "orderId") Long orderId) {
        Opinion opinion = modelMapper.map(opinionDto, Opinion.class);
        Opinion opinionForExpert = orderSystemService.saveOpinionForExpert(opinion, orderId);
        OpinionDto dto = modelMapper.map(opinionForExpert, OpinionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/order_done_date")
    public ResponseEntity<OrderSystemDoneDto> setDoneDate(@RequestParam(value = "suggestionId") Long suggestionId,
                                                          @Valid @RequestBody OrderDoneDateDto orderDoneDateDto,
                                                          @RequestParam(value = "orderId") Long orderId) {
        OrderSystem orderSystem = suggestionService.setDoneDate(orderId, orderDoneDateDto.getDoneDate(), suggestionId);
        OrderSystemDoneDto dto = modelMapper.map(orderSystem, OrderSystemDoneDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/save")
    public String saveCard(@Valid @RequestBody CreditCardDto creditCardDto) throws ForbiddenException {
        final boolean isValidCaptcha = validateCaptcha.validateCaptcha(creditCardDto.getCaptcha());
        if (!isValidCaptcha) {
            throw new ForbiddenException("INVALID_CAPTCHA");
        }
        return ("pay from  " + creditCardDto.getCardNumber());
    }

    @GetMapping("/status_advice")
    public ResponseEntity<List<OrderSystemDto>> findByStatusAdvice(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusAdvice(principal.getId(), orderStatus).stream().
                map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/status_selection")
    public ResponseEntity<List<OrderSystemDto>> findByStatusSelection(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusSelection(principal.getId(), orderStatus).
                stream().map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.
                        toList()));
    }

    @GetMapping("/status_come-place")
    public ResponseEntity<List<OrderSystemDto>> findByStatusComePlace(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusComePlace(principal.getId(), orderStatus).
                stream().map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.
                        toList()));
    }

    @GetMapping("/status_started")
    public ResponseEntity<List<OrderSystemDto>> findByStatusStarted(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusStarted(principal.getId(), orderStatus).stream().
                map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/status_done")
    public ResponseEntity<List<OrderSystemDto>> findByStatusDone(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusDone(principal.getId(), orderStatus).stream().
                map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/status_paid")
    public ResponseEntity<List<OrderSystemDto>> findByStatusPaid(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.findByStatusPaid(principal.getId(), orderStatus).stream().
                map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("all_suggestion_from_expert")
    public ResponseEntity<List<SuggestionDto>> showAllSuggestion(@RequestParam("id") Long id) {
        return ResponseEntity.ok().body(suggestionService.getAllSuggestionFromExpert(id).stream().map(suggestion ->
                modelMapper.map(suggestion, SuggestionDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("view_history_order")
    public ResponseEntity<List<OrderSystemDto>> viewOrderCustomer(@RequestParam("status") OrderStatus orderStatus) {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(orderSystemService.viewOrderCustomer(principal.getEmail(), orderStatus).stream()
                .map(orderSystem -> modelMapper.map(orderSystem, OrderSystemDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("view_credit")
    public ResponseEntity<Double> viewCreditCustomer() {
        Customer principal = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(customerService.viewCredit(principal.getEmail()));
    }
}
