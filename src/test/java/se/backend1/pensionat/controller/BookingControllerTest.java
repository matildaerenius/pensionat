package se.backend1.pensionat.controller;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.service.BookingService;
import se.backend1.pensionat.service.CustomerService;
import se.backend1.pensionat.service.RoomService;

import java.time.LocalDate;
import java.util.List;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class BookingControllerTest {

    private MockMvc mockMvc;

    private BookingService bookingService;
    private CustomerService customerService;
    private RoomService roomService;

    private BookingController bookingController;

    @BeforeEach
    public void setup() {
        bookingService = Mockito.mock(BookingService.class);
        customerService = Mockito.mock(CustomerService.class);
        roomService = Mockito.mock(RoomService.class);

        bookingController = new BookingController(bookingService, customerService, roomService);

        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    public void testGetAllBookings() throws Exception {
        // Mocka respons från bookingService
        when(bookingService.getAllBookings()).thenReturn(List.of(new BookingDto()));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/list"))
                .andExpect(model().attributeExists("bookings"));
    }

    @Test
    public void testShowCreateForm() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of());
        when(roomService.getAllRooms()).thenReturn(List.of());

        mockMvc.perform(get("/bookings/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/form"))
                .andExpect(model().attributeExists("bookingDto"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attribute("edit", false))
                .andExpect(model().attribute("formAction", "/bookings/create"));
    }


    @Test
    public void testCreateBooking_Success() throws Exception {
        RoomDto mockRoom = new RoomDto();
        mockRoom.setCapacity(2);
        mockRoom.setAllowExtraBeds(false);
        mockRoom.setMaxExtraBeds(0);

        when(roomService.getRoomById(1L)).thenReturn(mockRoom);

        mockMvc.perform(post("/bookings/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("checkIn", "2025-05-01")
                        .param("checkOut", "2025-05-05")
                        .param("numberOfGuests", "2")
                        .param("customerId", "1")
                        .param("roomId", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings"));
    }


    @Test
    public void testCreateBooking_ValidationError() throws Exception {
        // Skicka ogiltiga data som ger valideringsfel, t.ex. utan checkIn

        when(customerService.getAllCustomers()).thenReturn(List.of());
        when(roomService.getAllRooms()).thenReturn(List.of());

        mockMvc.perform(post("/bookings/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("checkOut", "2025-05-05")
                        .param("numberOfGuests", "2")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/form"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attribute("edit", false));
    }
    @Test
    public void testShowEditForm() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        when(bookingService.getBookingById(1L)).thenReturn(dto);
        when(customerService.getAllCustomers()).thenReturn(List.of());
        when(roomService.getAllRooms()).thenReturn(List.of());

        mockMvc.perform(get("/bookings/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings/form"))
                .andExpect(model().attributeExists("bookingDto"))
                .andExpect(model().attribute("edit", true));
    }
    @Test
    public void testUpdateBooking_Success() throws Exception {
        RoomDto mockRoom = new RoomDto();
        mockRoom.setCapacity(2);
        mockRoom.setAllowExtraBeds(false);
        mockRoom.setMaxExtraBeds(0);

        when(roomService.getRoomById(1L)).thenReturn(mockRoom);

        mockMvc.perform(post("/bookings/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("checkIn", "2025-05-01")
                        .param("checkOut", "2025-05-05")
                        .param("numberOfGuests", "2")
                        .param("customerId", "1")
                        .param("roomId", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings"));
    }


    @Test
    public void testDeleteBooking_Success() throws Exception {
        mockMvc.perform((RequestBuilder) post("/bookings/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings"));
    }

    @Test
    public void testDeleteBooking_Failure() throws Exception {
        doThrow(new RuntimeException("fail")).when(bookingService).deleteBooking(1L);

        mockMvc.perform((RequestBuilder) post("/bookings/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings"))
                .andExpect(flash().attributeExists("error"));
    }

}
