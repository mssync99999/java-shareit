package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    private UserDto ownerDto = UserDto.builder()
            .id(1L)
            .name("name")
            .email("name@mail.ru")
            .build();
    private UserDto bookerDto = UserDto.builder()
            .id(2L)
            .name("b")
            .email("b@mail.ru")
            .build();
    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .owner(UserMapper.toUser(ownerDto))
            .requestId(1L)
            .lastBooking(null)
            .nextBooking(null)
            .build();
    private BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .end(LocalDateTime.of(2024, 2, 3, 4, 5, 6))
            .booker(UserMapper.toUser(bookerDto))
            .itemId(itemDto.getId())
            .status(Status.WAITING)
            .build();

    @Test
    void ItemDtoTest() throws Exception {

        JsonContent<ItemDto> result = this.json.write(this.itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

    }

}
