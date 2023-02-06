package ir.maktab.controller;

import ir.maktab.data.dto.CustomerDto;
import ir.maktab.data.dto.OrderSystemDto;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.MainService;
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
    public ResponseEntity<Customer> getByEmail(@RequestParam("email") String email,
                                               @RequestParam("password") String password) {
        return ResponseEntity.ok().body(customerService.signIn(email, password));
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<Customer>> getAllExpert() {
        return ResponseEntity.ok().body(customerService.getAll());
    }

    @PutMapping("/update_customer")
    public ResponseEntity<Customer> updateCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        return ResponseEntity.ok().body(customerService.update(customer));
    }

    @DeleteMapping("delete_customer")
    public ResponseEntity<String> deleteCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerService.delete(customer);
        return ResponseEntity.ok().body("This customer delete");
    }

    @GetMapping("find_customer")
    public ResponseEntity<Customer> findCustomerByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok().body(customerService.getCustomerByEmail(email));
    }

    @PutMapping("/change_password")
    public ResponseEntity<Customer> updatePassword(@RequestParam("newPassword") String newPassword,
                                                   @RequestParam("confirmedPassword") String confirmedPassword,
                                                   @RequestParam("email") String email) {
        return ResponseEntity.ok().body(customerService.changePasswordCustomer(newPassword, confirmedPassword, email));
    }

    @GetMapping("/all_services")
    public ResponseEntity<List<MainService>> getAllServices() {
        return ResponseEntity.ok().body(customerService.showAllServices());
    }

    @GetMapping("/all_sub_services_of_service")
    public ResponseEntity<List<SubServices>> getAllSubServiceOfService(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok().body(subServicesService.getSubServiceByMainService(name));
    }

    @Transactional
    @PostMapping("/add_order")
    public ResponseEntity<OrderSystem> addOrder(@RequestBody OrderSystemDto orderSystemDto,
                                                @RequestParam(value = "id") Long id) {
        OrderSystem orderSystem = modelMapper.map(orderSystemDto, OrderSystem.class);
        return ResponseEntity.ok().body(orderSystemService.addOrderWithSubService(id, orderSystem));
    }
}
