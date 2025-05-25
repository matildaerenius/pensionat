package se.backend1.pensionat.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.backend1.pensionat.dto.CustomerDto;
import se.backend1.pensionat.exception.CustomerHasBookingsException;
import se.backend1.pensionat.exception.CustomerNotFoundException;
import se.backend1.pensionat.service.CustomerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;
    private CustomerService customerService;
    private CustomerController controller;

    @BeforeEach
    public void setup() {
        customerService = Mockito.mock(CustomerService.class);
        controller = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(new CustomerDto()));
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers/list"))
                .andExpect(model().attributeExists("customers"));
        verify(customerService).getAllCustomers();
    }

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/customers/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers/form"))
                .andExpect(model().attributeExists("customerDto"))
                .andExpect(model().attribute("edit", false));
    }

    @Test
    public void testCreateCustomer_Valid() throws Exception {
        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Anna")
                        .param("lastName", "Andersson")
                        .param("email", "anna@test.se")
                        .param("phoneNumber", "0123456789")
                        .param("address", "Gata 1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
        verify(customerService).createCustomer(any(CustomerDto.class));
    }

    @Test
    public void testCreateCustomer_Invalid() throws Exception {
        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")       // triggers @NotBlank
                        .param("lastName", "")
                        .param("email", "invalid")
                        .param("phoneNumber", "")
                        .param("address", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("customers/form"))
                .andExpect(model().attributeHasFieldErrors(
                        "customerDto",
                        "firstName", "lastName", "email", "phoneNumber", "address"
                ));
        verify(customerService, never()).createCustomer(any());
    }

    @Test
    public void testShowEditForm() throws Exception {
        CustomerDto dto = CustomerDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Testsson")
                .email("t@test.se")
                .phoneNumber("0123")
                .address("Gata 1")
                .build();
        when(customerService.getCustomerById(1L)).thenReturn(dto);

        mockMvc.perform(get("/customers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers/form"))
                .andExpect(model().attributeExists("customerDto"))
                .andExpect(model().attribute("edit", true));
        verify(customerService).getCustomerById(1L);
    }

    @Test
    public void testUpdateCustomer_Valid() throws Exception {
        mockMvc.perform(post("/customers/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Eva")
                        .param("lastName", "Ekvall")
                        .param("email", "eva@test.se")
                        .param("phoneNumber", "0987654321")
                        .param("address", "Väg 2")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
        verify(customerService).updateCustomer(eq(1L), any(CustomerDto.class));
    }

    @Test
    public void testUpdateCustomer_Invalid() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(new CustomerDto());

        mockMvc.perform(post("/customers/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")  // invalid
                )
                .andExpect(status().isOk())
                .andExpect(view().name("customers/edit"))
                .andExpect(model().attributeHasFieldErrors("customerDto", "firstName"));
        verify(customerService, never()).updateCustomer(anyLong(), any());
    }

    @Test
    public void testDeleteCustomer_Success() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(post("/customers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
        verify(customerService).deleteCustomer(1L);
    }

    @Test
    public void testDeleteCustomer_HasBookings() throws Exception {
        doThrow(new CustomerHasBookingsException("")).when(customerService).deleteCustomer(1L);

        mockMvc.perform(post("/customers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"))
                .andExpect(flash().attribute("error", "Kunden har aktiva bokningar och kan inte tas bort"));
        verify(customerService).deleteCustomer(1L);
    }

    @Test
    public void testDeleteCustomer_NotFound() throws Exception {
        doThrow(new CustomerNotFoundException("")).when(customerService).deleteCustomer(1L);

        mockMvc.perform(post("/customers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"))
                .andExpect(flash().attribute("error", "Kunden kunde inte hittas"));
        verify(customerService).deleteCustomer(1L);
    }
}