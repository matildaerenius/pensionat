package se.backend1.pensionat.service;

import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(int id);
    void saveCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int id);
}