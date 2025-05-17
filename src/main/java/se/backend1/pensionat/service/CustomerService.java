package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {

    //CustomerDto customerToCustomerDto (Customer customer);


    //Bygger om customer -> DTO
    DetailedCustomerDto getCustomerDetails(Long id);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    //klar, gör en DTO från frontend till entitet
    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);
}