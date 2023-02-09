package ir.maktab.service;

import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.MainService;
import ir.maktab.data.repository.CustomerRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MainServicesService mainServicesService;

    public void signUp(Customer customer) {
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
    }

    public Customer signIn(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist ! "));
        if (!(customer.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return customer;
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundUser("This user is not found"));
    }

    public Customer changePasswordCustomer(String newPassword, String confirmedPassword, String email) {
        Customer customerByEmail = getCustomerByEmail(email);
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        customerByEmail.setPassword(newPassword);
        return customerRepository.save(customerByEmail);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void delete(Customer customer) {
        Customer customer1 = getCustomerByEmail(customer.getEmail());
        customer.setId(customer1.getId());
        customerRepository.delete(customer);
    }

    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<MainService> showAllServices() {
        return mainServicesService.getAllServices();
    }

}
