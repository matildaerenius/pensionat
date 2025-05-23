package se.backend1.pensionat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.dto.DetailedCustomerDto;
import se.backend1.pensionat.entity.Booking;
import se.backend1.pensionat.entity.Customer;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.CustomerNotFoundException;
import se.backend1.pensionat.mapper.CustomerMapper;
import se.backend1.pensionat.repository.CustomerRepository;
import se.backend1.pensionat.service.impl.CustomerServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceimpl;

    private Customer customer;
    private CustomerDto customerDto;
    private DetailedCustomerDto detailedCustomerDto;

    @BeforeEach
    void setUp(){
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("För");
        customer.setLastName("Efter");
        customer.setEmail("För.efternamn@Gmail.com");
        customer.setPhoneNumber("123123");
        customer.setAddress("Nackademin 1");

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("För");
        customerDto.setLastName("Efter");
        customerDto.setEmail("För.efternamn@Gmail.com");
        customerDto.setPhoneNumber("123123");
        customerDto.setAddress("Nackademin 1");


        detailedCustomerDto = new DetailedCustomerDto();
        detailedCustomerDto.setId(1L);
        detailedCustomerDto.setFirstName("Test");
        detailedCustomerDto.setLastName("Person");
    }

    @Test
    void createCustomerTest(){
        // Förberedelse, mockar dom 3 raderna i metoden
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // Utför metoden
        CustomerDto result = customerServiceimpl.createCustomer(customerDto);

        // Kontroll av svar
        assertEquals(customerDto.getId(), result.getId());
        assertEquals(customerDto.getFirstName(), result.getFirstName());
        verify(customerRepository).save(customer);
        verify(customerMapper).toEntity(customerDto);
        verify(customerMapper).toDto(customer);

    }
    @Test
    void updateCustomerTest(){
        Long id = customer.getId();
        //Kontroll att kunden finns i databas
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        //Kontroll att den uppdaterade kunden returneras när vi sparar
        when(customerRepository.save(customer)).thenReturn(customer);
        //Kontroll av mappning till DTO
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerServiceimpl.updateCustomer(id, customerDto);

        // Verifiera resultat
        assertEquals(customerDto.getId(), result.getId());
        assertEquals(customerDto.getFirstName(), result.getFirstName());
        assertEquals(customerDto.getEmail(), result.getEmail());

        // Kontrollera att metoder anropades
        verify(customerRepository).findById(id);
        verify(customerRepository).save(customer);
        verify(customerMapper).toDto(customer);

    }

    @Test
    void deleteCustomerTest(){
        Long id = customer.getId();


        // Mocka att kunden inte har några bokningar
        customer.setBookings(Collections.emptyList());

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Kör metoden
        customerServiceimpl.deleteCustomer(id);

        // Kontrollera att rätt metoder anropades
        verify(customerRepository).findById(id);
        verify(customerRepository).delete(customer);
    }
    @Test
    void deleteCustomerShouldThrowExceptionWhenCustomerHasBookings() {
        Long id = customer.getId();

        // Mocka att kunden har en bokning
        customer.setBookings(List.of(new Booking())); // Du kan mocka Booking om det behövs

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Kontrollera att rätt undantag kastas
        assertThrows(CustomerHasBookingsException.class, () -> customerServiceimpl.deleteCustomer(id));
    }
    @Test
    void getCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerServiceimpl.getCustomerById(1L);

        assertEquals(customerDto.getId(), result.getId());
        verify(customerRepository).findById(1L);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void getCustomerById_whenCustomerNotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerServiceimpl.getCustomerById(2L));
    }

    @Test
    void getAllCustomers() {
        List<Customer> customers = List.of(customer);
        List<CustomerDto> dtos = List.of(customerDto);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        List<CustomerDto> result = customerServiceimpl.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals(customerDto.getId(), result.get(0).getId());
        verify(customerRepository).findAll();
        verify(customerMapper).toDto(customer);
    }

    @Test
    void hasBookings_true() {
        customer.setBookings(List.of(new Booking())); // eller mockad Booking
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertTrue(customerServiceimpl.hasBookings(1L));
        verify(customerRepository).findById(1L);
    }
    @Test
    void hasBookings_false() {
        customer.setBookings(Collections.emptyList());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertFalse(customerServiceimpl.hasBookings(1L));
    }

    @Test
    void getCustomerDetails() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.detailedCustomer(customer)).thenReturn(detailedCustomerDto);

        DetailedCustomerDto result = customerServiceimpl.getCustomerDetails(1L);

        assertEquals(detailedCustomerDto.getId(), result.getId());
        verify(customerRepository).findById(1L);
        verify(customerMapper).detailedCustomer(customer);
    }

    @Test
    void save() {
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);

        customerServiceimpl.createCustomer(customerDto);

        verify(customerMapper).toEntity(customerDto);
        verify(customerRepository).save(customer);
    }

}
