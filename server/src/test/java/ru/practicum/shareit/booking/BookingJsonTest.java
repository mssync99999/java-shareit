package ru.practicum.shareit.booking;

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
public class BookingJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

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
    void bookingDtoTest() throws Exception {

        JsonContent<BookingDto> result = this.json.write(this.bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-02-03T04:05:06");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("b");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("b@mail.ru");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

}
