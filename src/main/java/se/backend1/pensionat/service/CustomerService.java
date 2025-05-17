package se.backend1.pensionat.service;

import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.List;

public interface CustomerService {
    //KLAR gör en DTO från frontend till entitet
    CustomerDto createCustomer(CustomerDto customerDto);
    //KLAR
    CustomerDto updateCustomer(Long id, CustomerDto customerDto);
    //KLAR
    void deleteCustomer(Long id);
    //KLAR
    CustomerDto getCustomerById(Long id);
    //KLAR
    List<CustomerDto> getAllCustomers();
    //KLAR
    boolean hasBookings(Long id);

    DetailedCustomerDto getCustomerDetails(Long id);










}