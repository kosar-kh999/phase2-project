package ir.maktab.controller;

import ir.maktab.data.dto.CustomerDto;
import ir.maktab.data.model.Customer;
import ir.maktab.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
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
}
