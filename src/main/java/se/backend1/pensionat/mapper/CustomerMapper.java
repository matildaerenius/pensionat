package se.backend1.pensionat.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final BookingMapper bookingMapper;

    public CustomerMapper(@Lazy BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    //Customer > DTO
    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    // DTO > Customer
    public Customer toEntity(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
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
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .bookings(
                        customer.getBookings().stream()
                                .map(bookingMapper::toDto)
                                .collect(Collectors.toList())
                )
                .build();

    }

}
