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

    // KLAR
    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        Customer entity = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);  // inkluderar ID, sparad i backend
    }


    // TODO : Kasta CustomerNotFoundException om kund inte hittas
    //KLAR
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

    // TODO : Checka om kund redan har aktiv bokning -> om ja, kasta CustomerHasBookingsException -> om nej, delete kund.
    // TODO : Kasta även CustomerNotFoundException om kund inte hittas
    // KLAR
    @Override
    public void deleteCustomer(Long id) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("There is no existing customer"));

        if (existing.getBookings() != null && !existing.getBookings().isEmpty()) {
            throw new CustomerHasBookingsException("Kan ej ta bort, kund har bokning");
        }else
            customerRepository.delete(existing);
    }



    // TODO : Kasta CustomerNotFoundException om kund inte hittas
    //KLAR
    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        CustomerDto dto = customerMapper.toDto(existing);
        return dto;
    }

    //KLAR
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
    //KLAR
    @Override
    public boolean hasBookings(Long id) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        //om kund har bokning
        return existing.getBookings() != null && !existing.getBookings().isEmpty();

    }


    //Bygger om customer -> DTO
    //KLAR
    @Override
    public DetailedCustomerDto getCustomerDetails(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        // Här kallar du på mappningsmetod
        return customerMapper.detailedCustomer(customer);
    }

}
