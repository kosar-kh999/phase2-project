package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.model.*;
import ir.maktab.service.CustomerService;
import ir.maktab.service.OrderSystemService;
import ir.maktab.service.SubServicesService;
import ir.maktab.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final SubServicesService subServicesService;
    private final OrderSystemService orderSystemService;
    private final SuggestionService suggestionService;
    private final ModelMapper modelMapper;

    @PostMapping("/add_customer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerService.signUp(customer);
        return ResponseEntity.ok().body("You sign up successfully");
    }

    @GetMapping("/sign_In_customer")
    public ResponseEntity<CustomerSignInDto> getByEmail(@RequestParam("email") String email,
                                                        @RequestParam("password") String password) {
        Customer customer = customerService.signIn(email, password);
        CustomerSignInDto dto = modelMapper.map(customer, CustomerSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<CustomerSignInDto>> getAllExpert() {
        return ResponseEntity.ok().body(customerService.getAll().stream().map(customer -> modelMapper.
                map(customer, CustomerSignInDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/update_customer")
    public ResponseEntity<CustomerSignInDto> updateCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Customer update = customerService.update(customer);
        CustomerSignInDto dto = modelMapper.map(update, CustomerSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("delete_customer")
    public ResponseEntity<String> deleteCustomer(@RequestBody CustomerDto customerDto) {
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
                                                            @RequestParam("confirmedPassword") String confirmedPassword,
                                                            @RequestParam("email") String email) {
        Customer customer = customerService.changePasswordCustomer(newPassword, confirmedPassword, email);
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
    public ResponseEntity<OrderSystemDto> addOrder(@RequestBody OrderSystemDto orderSystemDto,
                                                   @RequestParam(value = "subId") Long subId,
                                                   @RequestParam(value = "customerId") Long customerId) {
        OrderSystem orderSystem = modelMapper.map(orderSystemDto, OrderSystem.class);
        OrderSystem order = orderSystemService.addOrderWithSubService(subId, orderSystem, customerId);
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

    @PutMapping("/change_status_to_done")
    public ResponseEntity<SuggestionDto> changeStatusToDone(@RequestParam(value = "suggestionId") Long suggestionId) {
        Suggestion suggestion = suggestionService.changeOrderStatusToDone(suggestionId);
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
    public ResponseEntity<OpinionDto> addOpinion(@RequestBody OpinionDto opinionDto,
                                                 @RequestParam(value = "orderId") Long orderId) {
        Opinion opinion = modelMapper.map(opinionDto, Opinion.class);
        Opinion opinionForExpert = orderSystemService.saveOpinionForExpert(opinion, orderId);
        OpinionDto dto = modelMapper.map(opinionForExpert, OpinionDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/filter_customer")
    public ResponseEntity<List<CustomerFilterDto>> filterCustomer(@RequestBody CustomerFilterDto customerFilterDto){
        return ResponseEntity.ok().body(customerService.getCustomers(customerFilterDto).stream().map(customer ->
                modelMapper.map(customer,CustomerFilterDto.class)).collect(Collectors.toList()));
    }
}
