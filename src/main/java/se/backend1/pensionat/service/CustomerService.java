package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {

    //CustomerDto customerToCustomerDto (Customer customer);


    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);
}