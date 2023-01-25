package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.OrderSystem;
import ir.maktab.data.model.Suggestion;
import ir.maktab.data.repository.CustomerRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import ir.maktab.util.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ServicesService servicesService;
    private final SubServicesService subServicesService;

    private final SuggestionService suggestionService;

    private final OrderSystemService orderSystemService;

    public void signUp(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer signIn(String email, String password) throws NotFoundUser {
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist ! "));
        if (!(customer.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return customer;
    }

    public void changePassword(String newPassword, String confirmedPassword, Customer customer) throws NotCorrect {
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        customer.setPassword(newPassword);
        customerRepository.save(customer);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    public void update(Customer customer) {
        customerRepository.save(customer);
    }

    public void showAllServices() {
        servicesService.getAllServices();
    }

    public void showAllSubServices() {
        subServicesService.getAllSubServices();
    }

    public void changeOrderStatusToStarted(OrderSystem orderSystem, Suggestion suggestion) throws OrderException {
        if (suggestion.getSuggestionsStartedTime().before(orderSystem.getTimeToDo()))
            throw new OrderException("time of suggestion from expert must be after the time of order system to do");
        orderSystem.setOrderStatus(OrderStatus.STARTED);
        orderSystemService.addOrder(orderSystem);
    }

    public void changeOrderStatusToDone(OrderSystem orderSystem) {
        orderSystem.setOrderStatus(OrderStatus.DONE);
        orderSystemService.addOrder(orderSystem);
    }
}
