package se.backend1.pensionat.mapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.entity.Customer;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper(null); // bookingMapper används ej i dessa tester

    @Test
    public void testToDto() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .phoneNumber("0701234567")
                .address("Nacka")
                .build();

        CustomerDto dto = customerMapper.toDto(customer);

        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getName(), dto.getName());
        assertEquals(customer.getEmail(), dto.getEmail());
        assertEquals(customer.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(customer.getAddress(), dto.getAddress());
    }

    @Test
    public void testToEntity() {
        CustomerDto dto = CustomerDto.builder()
                .id(2L)
                .name("Kalle Hamilton")
                .email("kalle@example.com")
                .phoneNumber("0737654321")
                .address("Haparanda")
                .build();

        Customer customer = customerMapper.toEntity(dto);

        assertEquals(dto.getId(), customer.getId());
        assertEquals(dto.getName(), customer.getName());
        assertEquals(dto.getEmail(), customer.getEmail());
        assertEquals(dto.getPhoneNumber(), customer.getPhoneNumber());
        assertEquals(dto.getAddress(), customer.getAddress());
    }
}
