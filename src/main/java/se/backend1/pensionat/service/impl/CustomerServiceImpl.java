package se.backend1.pensionat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.CustomerDto;

import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.CustomerService;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // TODO : Denna måste mappas om till toDto fyi
    @Override
    public List<CustomerDto> getAllCustomers() {
        return List.of();
    }

    // TODO : Kasta CustomerNotFoundException om kund inte hittas
    @Override
    public CustomerDto getCustomerById(Long id) {
        return null;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.toEntity(customerDto);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toDto(saved);
    }

    // TODO : Kasta CustomerNotFoundException om kund inte hittas
    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        return null;
    }

    // TODO : Checka om kund redan har aktiv bokning -> om ja, kasta CustomerHasBookingsException -> om nej, delete kund.
    // TODO : Kasta även CustomerNotFoundException om kund inte hittas
    @Override
    public void deleteCustomer(Long id) {

    }
}
