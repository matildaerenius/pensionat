package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.entity.Customer;

@Component
public class CustomerMapper {

    //Customer > DTO
    public CustomerDto customerToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    // DTO > Customer
    public Customer customerDtoToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .address(customerDto.getAddress())
                .build();
    }

//    public static Customer toEntity(CustomerDto dto) {
//        if (dto == null) return null;
//        Customer customer = new Customer();
//        customer.setId(dto.getId());
//        customer.setName(dto.getName());
//        customer.setEmail(dto.getEmail());
//        customer.setAddress(dto.getAddress());
//        customer.setPhoneNumber(dto.getPhoneNumber());
//        return customer;
//    }
//
//    public static CustomerDto toDto(Customer customer) {
//        if (customer == null) return null;
//        CustomerDto dto = new CustomerDto();
//        dto.setId(customer.getId());
//        dto.setName(customer.getName());
//        dto.setEmail(customer.getEmail());
//        dto.setPhoneNumber(customer.getPhoneNumber());
//        dto.setAddress(customer.getAddress());
//        return dto;
//    }
}
