package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.model.Admin;
import ir.maktab.data.model.Expert;
import ir.maktab.data.model.MainService;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceInterface adminService;
    private final SubServicesServiceInterface subServicesService;
    private final SuggestionServiceInterface suggestionService;
    private final ExpertServiceInterface expertService;
    private final CustomerServiceInterface customerService;
    private final OrderSystemServiceInterface orderSystemService;
    private final ModelMapper modelMapper;

    @PostMapping("/add_admin")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDto adminDto) {
        Admin admin = modelMapper.map(adminDto, Admin.class);
        adminService.saveAdmin(admin);
        return ResponseEntity.ok().body(admin.getUsername() + " " + "sign up successfully");
    }

    @PostMapping("/add_service")
    public ResponseEntity<String> addService(@Valid @RequestBody MainServiceDto mainServiceDto) {
        MainService mainService = modelMapper.map(mainServiceDto, MainService.class);
        adminService.addServices(mainService);
        return ResponseEntity.ok().body(mainService.getName() + " " + " is added");
    }

    @Transactional
    @PostMapping("/add_sub_service")
    public ResponseEntity<String> addSubService(@Valid @RequestBody SubServiceDto subServiceDto,
                                                @RequestParam(value = "id") Long id) {
        SubServices subServices = modelMapper.map(subServiceDto, SubServices.class);
        adminService.addSubServiceToService(subServices, id);
        return ResponseEntity.ok().body(subServices.getSubName() + " " + " is added");
    }

    @Transactional
    @PostMapping("/add_expert_to_sub_service")
    public ResponseEntity<String> addExpertToSubService(@Valid @RequestBody ExpertSubDto expertSubDto) {
        Expert expert = adminService.addExpertToSubService(expertSubDto.getExpertId(), expertSubDto.getSubId());
        return ResponseEntity.ok().body(expert.getFirstName() + " " + "added to sub service");
    }

    @DeleteMapping("/delete_expert_from_sub_service")
    public ResponseEntity<String> deleteExert(@RequestParam(value = "idSub") Long idSub,
                                              @RequestParam(value = "id") Long id) {
        adminService.deleteExpertFromSubServices(id, idSub);
        return ResponseEntity.ok().body("expert removed from sub service");
    }

    @GetMapping("/get_main_services")
    public ResponseEntity<List<MainServiceDto>> gatAllServices() {
        return ResponseEntity.ok().body(adminService.showAllServices().stream().map(mainService -> modelMapper.
                map(mainService, MainServiceDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("/edit_to_confirmed")
    public ResponseEntity<ExpertSignInDto> editToConfirmed(@RequestParam(value = "email") String email) {
        Expert expert = adminService.editStatus(email);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/update_price")
    public ResponseEntity<String> updatePrice(@RequestParam(value = "price") double price,
                                              @RequestParam(value = "id") Long id) {
        subServicesService.updatePrice(price, id);
        return ResponseEntity.ok().body("The price has been update");
    }

    @PutMapping("/update_brief")
    public ResponseEntity<String> updateBrief(@RequestParam(value = "brief") String brief,
                                              @RequestParam(value = "id") Long id) {
        subServicesService.updateBrief(brief, id);
        return ResponseEntity.ok().body("The brief has been update");
    }

    @PutMapping("/deposit_credit")
    public ResponseEntity<ExpertSignInDto> deposit(@RequestParam(value = "idExpert") Long idExpert,
                                                   @RequestParam(value = "idSuggestion") Long idSuggestion) {
        Expert expert = suggestionService.deposit(idExpert, idSuggestion);
        ExpertSignInDto dto = modelMapper.map(expert, ExpertSignInDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExpertFilterDto>> filter(@Valid @RequestBody ExpertFilterDto expert) {
        return ResponseEntity.ok().body(expertService.getExperts(expert).stream().map(expert1 -> modelMapper.
                map(expert1, ExpertFilterDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/filter_customer")
    public ResponseEntity<List<CustomerFilterDto>> filterCustomer(@Valid @RequestBody CustomerFilterDto
                                                                          customerFilterDto) {
        return ResponseEntity.ok().body(customerService.getCustomers(customerFilterDto).stream().map(customer ->
                modelMapper.map(customer, CustomerFilterDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/filter_order")
    public ResponseEntity<List<OrderSystemFilterDto>> filterOrder(@Valid @RequestBody OrderSystemFilterDto
                                                                          orderSystemFilterDto) {
        return ResponseEntity.ok().body(orderSystemService.filterOrder(orderSystemFilterDto).stream().map(orderSystem ->
                modelMapper.map(orderSystem, OrderSystemFilterDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/filter_customer_entry_date")
    public ResponseEntity<List<CustomerSignUpDto>> filterCustomerByEntry(@Valid @RequestBody CustomerSignUpDto
                                                                                 customerSignUpDto) {
        return ResponseEntity.ok().body(customerService.signUpDateFilter(customerSignUpDto).stream().map(customer ->
                modelMapper.map(customer, CustomerSignUpDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/filter_expert_entry_date")
    public ResponseEntity<List<ExpertSignUpDto>> filterExpertByEntry(@Valid @RequestBody ExpertSignUpDto
                                                                             expertSignUpDto) {
        return ResponseEntity.ok().body(expertService.signUpDateFilter(expertSignUpDto).stream().map(expert ->
                modelMapper.map(expert, ExpertSignUpDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/count_order")
    public int calculateOrders(@RequestBody OrderCountCustomerDto orderCountCustomerDto) {
        return orderSystemService.calculateOrders(orderCountCustomerDto.getEmail(), orderCountCustomerDto.
                getOrderStatus());
    }

    @GetMapping("/count_order_expert")
    public int calculateOrdersExpert(@RequestBody OrderCountExpertDto orderCountExpertDto) {
        return orderSystemService.calculateOrdersExpert(orderCountExpertDto.getEmail(), orderCountExpertDto.
                getOrderStatus());
    }
}
