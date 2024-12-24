package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
//import org.mockito.MockitoAnnotations;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = BookingController.class)
//или @ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    //Реализовать тесты для REST-эндпоинтов вашего приложения с использованием MockMVC.
    // Вам нужно покрыть тестами все существующие эндпоинты.
    // При этом для слоя сервисов используйте моки.
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingServiceImpl bookingServiceImpl;

    private User user = User.builder()
            .id(1L)
            .name("userName")
            .email("user@mail.ru")
            .build();
    private Item item = Item.builder()
            .id(1L)
            .name("name")
            .description("description")
            .available(true)
            .build();
    private BookingResponseDto response = BookingResponseDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .end(LocalDateTime.of(2024, 2, 3, 4, 5, 6))
            //.state(State.CURRENT)
            .status(Status.APPROVED)
            .item(item)
            .booker(user)
            .build();
    private BookingDto responseDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .end(LocalDateTime.of(2024, 2, 3, 4, 5, 6))
            //.state(State.CURRENT)
            .status(Status.APPROVED)
            .itemId(1L)
            .booker(user)
            .build();

    @Test
    void createTest() throws Exception {
        when(bookingServiceImpl.create(any(), any(BookingDto.class))).thenReturn(response);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookingDto.builder()
                                .start(LocalDateTime
                                        .of(2024, 1, 2, 3, 4, 5))
                                .end(LocalDateTime
                                        .of(2024, 2, 3, 4, 5, 6))
                                .itemId(1L)
                                .build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.valueOf(response.getStart()))))
                .andExpect(jsonPath("$.end", is(String.valueOf(response.getEnd()))))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }


    @Test
    void updateApprovedTest() throws Exception {
        when(bookingServiceImpl.updateApproved(anyLong(), anyLong(), anyBoolean())).thenReturn(response);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.valueOf(response.getStart()))))
                .andExpect(jsonPath("$.end", is(String.valueOf(response.getEnd()))))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }


    @Test
    void findBookingTest() throws Exception {
        when(bookingServiceImpl.findBooking(anyLong(), anyLong())).thenReturn(response);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.valueOf(response.getStart()))))
                .andExpect(jsonPath("$.end", is(String.valueOf(response.getEnd()))))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(user.getName())));
    }


    @Test
    void findBookingAllTest() throws Exception {
        List<BookingResponseDto> responses = List.of(response);

        when(bookingServiceImpl.findBookingAll(anyLong(), any())).thenReturn(responses);

        mvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.*.booker.name", is(List.of(user.getName()))))
                .andExpect(jsonPath("$.*.item.name", is(List.of(item.getName()))));
    }

    @Test
    void findBookingOwnerAllTest() throws Exception {
        List<BookingDto> responses = List.of(responseDto);

        when(bookingServiceImpl.findBookingOwnerAll(anyLong(), any())).thenReturn(responses);

        mvc.perform(get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.*.booker.name", is(List.of(user.getName()))));
                //.andExpect(jsonPath("$.*.item.name", is(List.of(item.getName()))));
    }


}
