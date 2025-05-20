package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.CustomerDto;

import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.CustomerNotFoundException;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        Customer entity = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);  // inkluderar ID, sparad i backend
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID" + id));

        // Uppdatera bara det som kommer in i DTO:n
        existing.setName(customerDto.getName());
        existing.setEmail(customerDto.getEmail());
        existing.setPhoneNumber(customerDto.getPhoneNumber());
        existing.setAddress(customerDto.getAddress());

        Customer saved = customerRepository.save(existing);
        return customerMapper.toDto(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + id));

        if (existing.getBookings() != null && !existing.getBookings().isEmpty()) {
            throw new CustomerHasBookingsException("Kan ej ta bort, kund har bokning");
        }

        customerRepository.delete(existing);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers =customerRepository.findAll();
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer c : customers) {
            CustomerDto dto = customerMapper.toDto(c);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public boolean hasBookings(Long id) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        //om kund har bokning
        return existing.getBookings() != null && !existing.getBookings().isEmpty();

    }

    @Override
    public DetailedCustomerDto getCustomerDetails(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        return customerMapper.detailedCustomer(customer);
    }

    @Override
    public void save(CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        customerRepository.save(customer);
    }

}
