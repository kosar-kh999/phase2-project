package ir.maktab.controller;

import ir.maktab.data.dto.*;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.SubServices;
import ir.maktab.service.CustomerService;
import ir.maktab.service.OrderSystemService;
import ir.maktab.service.SubServicesService;
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

    /*@Transactional
    @GetMapping("/find_order_by_sub")
    public ResponseEntity<List<OrderSystemDto>> findBySub(@RequestBody SubServiceDto subServiceDto) {
        SubServices subServices = modelMapper.map(subServiceDto, SubServices.class);
        return ResponseEntity.ok().body(orderSystemService.findBySub());
        *//*List<OrderSystem> orderSystems = orderSystemService.findBySub(subServices);
        OrderSystemDto dto = modelMapper.map(orderSystems, OrderSystemDto.class);*//*

    }*/

    @Transactional
    @PostMapping("add_expert_and_customer")
    public ResponseEntity<String> addExpertAndCustomer(@RequestParam(value = "orderId") Long orderId,
                                                       @RequestParam(value = "customerId") Long customerId) {
        orderSystemService.setExpertAndCustomer(orderId, customerId);
        return ResponseEntity.ok().body("add expert and customer");
    }

}
