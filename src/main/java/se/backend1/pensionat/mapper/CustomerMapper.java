package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final BookingMapper bookingMapper;

    public CustomerMapper(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    //Customer > DTO
    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    // DTO > Customer
    public Customer toEntity(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .address(customerDto.getAddress())
                .build();
    }



    //får ut alla bokningar som en kund har samt deras
    //Detailed customer till Dto då frontend kanske vill kika på data från databasen
    public DetailedCustomerDto detailedCustomer(Customer customer) {
        return DetailedCustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getName())
                .email(customer.getEmail())
                .bookings(
                        customer.getBookings().stream()
                                .map(bookingMapper::bookingToBookingDto)
                                .collect(Collectors.toList())
                )
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
