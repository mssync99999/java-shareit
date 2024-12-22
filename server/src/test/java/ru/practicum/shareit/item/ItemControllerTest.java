package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemServiceImpl itemServiceImpl;
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
            .owner(user)
            .build();
    private ItemDto itemDto = ItemMapper.toItemDto(item);

    private BookingResponseDto response = BookingResponseDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .end(LocalDateTime.of(2024, 2, 3, 4, 5, 6))
            .status(Status.APPROVED)
            .item(item)
            .booker(user)
            .build();
    private BookingDto responseDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .end(LocalDateTime.of(2024, 2, 3, 4, 5, 6))
            .status(Status.APPROVED)
            .itemId(1L)
            .booker(user)
            .build();

    @Test
    void createTest() throws Exception {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        when(itemServiceImpl.create(any(ItemDto.class), any())).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }


    @Test
    void updateTest() throws Exception {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1L)
                .name("name2")
                .description("description2")
                .available(false)
                .build();

        when(itemServiceImpl.update(any(ItemDto.class), any(), any())).thenReturn(itemDtoTest);

        mvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTest.getDescription())));
    }

    @Test
    void findByIdTest() throws Exception {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();

        when(itemServiceImpl.findById(any(), any())).thenReturn(itemDtoTest);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTest.getDescription())));
    }

    @Test
    void findItemsAllTest() throws Exception {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();

        List<ItemDto> responses = List.of(itemDtoTest);
        when(itemServiceImpl.findItemsAll(any())).thenReturn(responses);

        mvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.*.name", is(List.of(itemDtoTest.getName()))));
    }

    @Test
    void searchByTextTest() throws Exception {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();

        List<ItemDto> responses = List.of(itemDtoTest);
        when(itemServiceImpl.searchByText(any())).thenReturn(responses);

        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.*.name", is(List.of(itemDtoTest.getName()))));
    }

    @Test
    void createCommentTest() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .created(LocalDateTime.of(2024, 12, 11, 10, 9, 8))
                .authorName("authorName")
                .text("texttexttext")
                .id(1L)
                .build();

        when(itemServiceImpl.createComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(String.valueOf(commentDto.getCreated()))));
    }

}
