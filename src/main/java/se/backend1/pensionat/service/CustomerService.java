package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);

    CustomerDto getCustomerById(Long id);

    List<CustomerDto> getAllCustomers();

    boolean hasBookings(Long id);

    DetailedCustomerDto getCustomerDetails(Long id);

}

