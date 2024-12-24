package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestServiceImpl itemRequestServiceImpl;

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
            .owner(user)
            .build();
    private ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("description")
            .build();
    private ItemShortDto itemShortDto = ItemShortDto.builder()
            .itemId(1L)
            .name("name")
            .ownerId(1L)
            .build();
    private ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
            .id(1L)
            .description("description")
            .created(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .items(List.of(itemShortDto))
            .build();
    private ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("description")
            .created(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .items(List.of(itemShortDto))
            .build();

    @Test
    void createTest() throws Exception {
        ItemRequest temp = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        ItemRequestMapper.toItemRequestResponseDto(itemRequest);

        when(itemRequestServiceImpl.create(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestResponseDto);


        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.created", is(String.valueOf(itemRequestResponseDto.getCreated()))));
    }

    @Test
    void findItemRequestTest() throws Exception {
        when(itemRequestServiceImpl.findItemRequest(anyLong(), anyLong())).thenReturn(itemRequestResponseDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.created", is(String.valueOf(itemRequestResponseDto.getCreated()))));
    }

    @Test
    void findItemRequestAllTest() throws Exception {
        List<ItemRequestResponseDto> responses = List.of(itemRequestResponseDto);
        when(itemRequestServiceImpl.findItemRequestAll(anyLong())).thenReturn(responses);

        mvc.perform(get("/requests")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.*.description", is(List.of(itemRequestResponseDto.getDescription()))));
    }

    @Test
    void findItemRequestOtherAllTest() throws Exception {
        List<ItemRequestResponseDto> responses = List.of(itemRequestResponseDto);
        when(itemRequestServiceImpl.findItemRequestOtherAll(anyLong())).thenReturn(responses);

        mvc.perform(get("/requests/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.*.description", is(List.of(itemRequestResponseDto.getDescription()))));
    }

}
