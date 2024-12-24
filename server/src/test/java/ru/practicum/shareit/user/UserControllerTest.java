package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserServiceImpl userServiceImpl;

    private User user = User.builder()
            .id(1L)
            .name("userName")
            .email("user@mail.ru")
            .build();
    private UserDto userDto = UserDto.builder()
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

    @Test
    void createTest() throws Exception {
        when(userServiceImpl.create(any(UserDto.class))).thenReturn(userDto);

        mvc.perform(post("/users")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }


    @Test
    void updateTest() throws Exception {
        UserDto userDtoTest = UserDto.builder()
                .id(1L)
                .name("new name")
                .email("new@mail.ru")
                .build();
        when(userServiceImpl.update(any(UserDto.class), anyLong())).thenReturn(userDtoTest);

        mvc.perform(patch("/users/{userId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDtoTest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoTest.getName())))
                .andExpect(jsonPath("$.email", is(userDtoTest.getEmail())));
    }


    @Test
    void findByIdTest() throws Exception {
        when(userServiceImpl.findById(anyLong())).thenReturn(userDto);

        mvc.perform(get("/users/{userId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }


    @Test
    void findUsersAllTest() throws Exception {
        List<UserDto> responses = List.of(userDto);
        when(userServiceImpl.findUsersAll()).thenReturn(responses);

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.*.email", is(List.of(userDto.getEmail()))));

    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userServiceImpl, times(1)).delete(anyLong());
    }

}