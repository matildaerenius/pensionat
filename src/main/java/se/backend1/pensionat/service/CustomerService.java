package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(long id);
    void saveCustomer(Customer customer);
    void updateCustomer(Customer customer); //Ska vi ha CostumerDto som inparameter istället för Costumer
    void deleteCustomer(long id);
}
