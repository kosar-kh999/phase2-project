package ir.maktab.service;

import ir.maktab.data.dto.CustomerFilterDto;
import ir.maktab.data.dto.CustomerSignUpDto;
import ir.maktab.data.model.Customer;
import ir.maktab.data.model.MainService;

import java.util.List;

public interface CustomerServiceInterface {

    void signUp(Customer customer);

    Customer getCustomerByEmail(String email);

    Customer getById(Long id);

    Customer changePasswordCustomer(String newPassword, String confirmedPassword, String email);

    List<Customer> getAll();

    void delete(Customer customer);

    Customer update(Customer customer);

    List<MainService> showAllServices();

    List<Customer> getCustomers(CustomerFilterDto customer);

    List<Customer> signUpDateFilter(CustomerSignUpDto customer);

    Double viewCredit(String email);
}
