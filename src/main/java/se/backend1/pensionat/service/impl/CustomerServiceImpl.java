package se.backend1.pensionat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.backend1.pensionat.dto.CustomerDto;

import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.exception.CustomerNotFoundException;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.CustomerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // TODO : Denna måste mappas om till toDto fyi
//    @Override
//    public List<CustomerDto> getAllCustomers() {
//        return List.of();
//    }

    //Bygger om customer -> DTO
    @Override
    public DetailedCustomerDto getCustomerDetails(Long id) {
        Customer customer = customerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException(CustomerNotFoundException.class.getName()));

        // Här kallar du på mappningsmetod
        return customerMapper.detailedCustomer(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        Customer entity = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);  // inkluderar ID, sparad i backend
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return List.of();
    }

    // TODO : Kasta CustomerNotFoundException om kund inte hittas
    @Override
    public CustomerDto getCustomerById(Long id) {
        return null;
    }



//    @Override
//    public CustomerDto createCustomer(CustomerDto customerDto) {
//        Customer customer = CustomerMapper.toEntity(customerDto);
//        Customer saved = customerRepository.save(customer);
//        return CustomerMapper.toDto(saved);
//    }

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
