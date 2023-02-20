package ir.maktab.service;

import ir.maktab.data.dto.CustomerFilterDto;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.MainService;
import ir.maktab.data.repository.CustomerRepository;
import ir.maktab.util.exception.ExistException;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MainServicesService mainServicesService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerService(CustomerRepository customerRepository, MainServicesService mainServicesService,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.mainServicesService = mainServicesService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void signUp(Customer customer) {
        if (customerRepository.findCustomerByEmail(customer.getEmail()).isPresent())
            throw new ExistException("The email is exist");
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
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

    @Transactional
    public List<Customer> getCustomers(CustomerFilterDto customer) {

        return customerRepository.findAll((Specification<Customer>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (customer.getRole() != null)
                predicates.add(cb.equal(root.get("role"), customer.getRole()));
            if (customer.getFirstName() != null && customer.getFirstName().length() != 0)
                predicates.add(cb.equal(root.get("firstName"), customer.getFirstName()));
            if (customer.getLastName() != null && customer.getLastName().length() != 0)
                predicates.add(cb.equal(root.get("lastName"), customer.getLastName()));
            if (customer.getEmail() != null && customer.getEmail().length() != 0)
                predicates.add(cb.equal(root.get("email"), customer.getEmail()));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

}
