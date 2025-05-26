package se.backend1.pensionat.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.backend1.pensionat.dto.RoomDto;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.service.RoomService;
import se.backend1.pensionat.exception.RoomHasBookingsException;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RoomControllerTest {

    private MockMvc mockMvc;
    private RoomService roomService;
    private RoomController controller;

    @BeforeEach
    public void setup() {
        roomService = Mockito.mock(RoomService.class);
        controller = new RoomController(roomService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllRooms_NoParams() throws Exception {
        when(roomService.getAllRooms()).thenReturn(List.of(new RoomDto()));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/list"))
                .andExpect(model().attributeExists("rooms"));

        verify(roomService, times(1)).getAllRooms();
    }

    @Test
    public void testGetAllRooms_WithParams() throws Exception {
        RoomDto dto = RoomDto.builder()
                .id(1L)
                .roomNumber("101")
                .roomType(RoomType.DOUBLE)
                .capacity(2)
                .allowExtraBeds(true)
                .maxExtraBeds(1)
                .build();

        when(roomService.findAvailableRoomFromQuery(
                eq(LocalDate.of(2025,5,1)),
                eq(LocalDate.of(2025,5,5)),
                eq(2))
        ).thenReturn(List.of(dto));

        mockMvc.perform(get("/rooms")
                        .param("checkIn", "2025-05-01")
                        .param("checkOut", "2025-05-05")
                        .param("guests", "2")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/list"))
                .andExpect(model().attributeExists("rooms"));

        verify(roomService, never()).getAllRooms();
        verify(roomService, times(1))
                .findAvailableRoomFromQuery(any(LocalDate.class), any(LocalDate.class), anyInt());
    }

    @Test
    public void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().attributeExists("roomDto"))
                .andExpect(model().attributeExists("roomTypes"))
                .andExpect(model().attributeExists("formAction"))
                .andExpect(model().attribute("edit", false));
    }

    @Test
    public void testCreateRoom_Valid() throws Exception {
        // Inga valideringsfel → redirect
        mockMvc.perform(post("/rooms/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roomNumber", "101")
                        .param("roomType", "SINGLE")
                        .param("capacity", "2")
                        .param("allowExtraBeds", "false")
                        .param("maxExtraBeds", "0")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms"));

        verify(roomService, times(1)).createRoom(any(RoomDto.class));
    }

    @Test
    public void testCreateRoom_Invalid() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roomNumber", "")    // triggers @NotBlank
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().attributeHasFieldErrors("roomDto", "roomNumber"))
                .andExpect(model().attribute("edit", false));
    }


    @Test
    public void testShowEditForm() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(1L);

        when(roomService.getRoomById(1L)).thenReturn(dto);

        mockMvc.perform(get("/rooms/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().attributeExists("roomDto"))
                .andExpect(model().attributeExists("roomTypes"))
                .andExpect(model().attribute("roomDto", dto))
                .andExpect(model().attribute("edit", true));
    }



    @Test
    public void testUpdateRoom_Valid() throws Exception {
        mockMvc.perform(post("/rooms/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roomNumber", "202")
                        .param("roomType", "DOUBLE")
                        .param("capacity", "3")
                        .param("allowExtraBeds", "true")
                        .param("maxExtraBeds", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms"));

        verify(roomService, times(1)).updateRoom(eq(1L), any(RoomDto.class));
    }

    @Test
    public void testUpdateRoom_Invalid() throws Exception {
        when(roomService.getRoomById(1L)).thenReturn(new RoomDto());

        mockMvc.perform(post("/rooms/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roomNumber", "")  // invalid
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().attributeHasFieldErrors("roomDto", "roomNumber"))
                .andExpect(model().attribute("edit", true));
    }


    @Test
    public void testDeleteRoom_Success() throws Exception {
        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(post("/rooms/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms"))
                .andExpect(flash().attribute("cancel", "Rum borttaget!"));

        verify(roomService, times(1)).deleteRoom(1L);
    }


    @Test
    public void testDeleteRoom_Failure() throws Exception {
        doThrow(new RoomHasBookingsException("Rum har bokningar och kan inte tas bort"))
                .when(roomService).deleteRoom(1L);

        mockMvc.perform(post("/rooms/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms"))
                .andExpect(flash().attribute("error", "Rum har bokningar och kan inte tas bort"));

        verify(roomService, times(1)).deleteRoom(1L);
    }

}
