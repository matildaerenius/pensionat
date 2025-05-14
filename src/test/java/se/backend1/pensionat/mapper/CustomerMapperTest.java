package se.backend1.pensionat.mapper;

import org.junit.jupiter.api.Test;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.entity.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    @Test
    void shouldMapDtoToEntityCorrectly() {
        CustomerDto dto = new CustomerDto();
        dto.setId(1L);
        dto.setName("Jane Doe");
        dto.setEmail("janedoe@example.com");
        dto.setPhoneNumber("0701234567");
        dto.setAddress("Testgatan 1");

        Customer entity = CustomerMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(dto.getAddress(), entity.getAddress());
    }

    @Test
    void shouldMapEntityToDtoCorrectly() {
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setName("John Doe");
        customer.setEmail("jane@example.com");
        customer.setPhoneNumber("0737654321");
        customer.setAddress("Exempelgatan 2");

        CustomerDto dto = CustomerMapper.toDto(customer);

        assertNotNull(dto);
        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getName(), dto.getName());
        assertEquals(customer.getEmail(), dto.getEmail());
        assertEquals(customer.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(customer.getAddress(), dto.getAddress());
    }

    @Test
    void shouldReturnNullIfInputIsNull() {
        assertNull(CustomerMapper.toDto(null));
        assertNull(CustomerMapper.toEntity(null));
    }
}
