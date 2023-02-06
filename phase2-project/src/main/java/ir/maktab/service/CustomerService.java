package ir.maktab.service;

import ir.maktab.data.enums.OrderStatus;
import ir.maktab.data.model.*;
import ir.maktab.data.repository.CustomerRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFoundUser;
import ir.maktab.util.exception.OrderException;
import ir.maktab.util.exception.SuggestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final MainServicesService mainServicesService;
    private final SubServicesService subServicesService;
    private final SuggestionService suggestionService;
    private final OrderSystemService orderSystemService;

    public void signUp(Customer customer) {
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

    /*public Customer changePassword(String newPassword, String confirmedPassword, Customer customer) {
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        customer.setPassword(newPassword);
        return customerRepository.save(customer);
    }*/

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
    public Suggestion changeOrderStatusToStarted(OrderSystem orderSystem, Suggestion suggestion) {
        if (suggestion.getSuggestionsStartedTime().before(orderSystem.getTimeToDo()))
            throw new OrderException("time of suggestion from expert must be after the time of order system to do");
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.STARTED);
        return suggestionService.updateSuggestion(suggestion);
    }

    public Suggestion changeOrderStatusToDone(Suggestion suggestion) {
        suggestion.getOrderSystem().setOrderStatus(OrderStatus.DONE);
        return suggestionService.updateSuggestion(suggestion);
    }

    public Customer withdraw(Customer customer, Suggestion suggestion) {
        Customer customerByEmail = getCustomerByEmail(customer.getEmail());
        Suggestion suggestionById = suggestionService.getSuggestionById(suggestion.getId());
        if (suggestionById.getPrice() > customerByEmail.getCredit())
            throw new SuggestionException("the amount of customer is should more than suggestion");
        double withdraw = customerByEmail.getCredit() - suggestionById.getPrice();
        customer.setCredit(withdraw);
        return update(customer);
    }
}
