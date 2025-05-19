package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.CustomerDto;

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


    private CustomerRepository customerRepository;

    // TODO : Denna måste mappas om till toDto fyi
//    @Override
//    public List<CustomerDto> getAllCustomers() {
//        return List.of();
//    }

    //Bygger om customer -> DTO


    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDto dto = CustomerMapper.toDto(customer);
            customerDtos.add(dto);
        }

        return customerDtos;
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id.intValue())
                .orElseThrow(() -> new CustomerNotFoundException("Kund med id "+ id + " hittades inte"));
        return CustomerMapper.toDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.toEntity(customerDto);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toDto(saved);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existing = customerRepository.findById(id.intValue())
                .orElseThrow(() -> new CustomerNotFoundException("Kund med ID " + id + " hittades inte"));

        existing.setName(customerDto.getName());
        existing.setEmail(customerDto.getEmail());
        existing.setPhoneNumber(customerDto.getPhoneNumber());
        existing.setAddress(customerDto.getAddress());

        Customer updated = customerRepository.save(existing);
        return CustomerMapper.toDto(updated);
    }

    // TODO : Checka om kund redan har aktiv bokning -> om ja, kasta CustomerHasBookingsException -> om nej, delete kund.
    // TODO : Kasta även CustomerNotFoundException om kund inte hittas
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id.intValue())
                .orElseThrow(() -> new CustomerNotFoundException("Kund med ID " + id + " hittades inte"));
        if (customer.getBookings() != null && !customer.getBookings().isEmpty()) {
            throw new CustomerHasBookingsException("Kunden har aktiva bokningar och kan inte tas bort");
        }

        customerRepository.delete(customer);

    }
}
