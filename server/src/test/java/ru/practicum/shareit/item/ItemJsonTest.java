package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    private User user = User.builder()
            .id(1L)
            .name("userName")
            .email("user@mail.ru")
            .build();
    private User author = User.builder()
            .id(3L)
            .name("authorName")
            .email("author@mail.ru")
            .build();
    private Item item = Item.builder()
            .id(1L)
            .name("name")
            .description("description")
            .available(true)
            .owner(user)
            .build();
    private Comment comment = Comment.builder()
            .id(1L)
            .text("name")
            .item(item)
            .author(author)
            .created(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .build();
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
            .comments(List.of(CommentMapper.toCommentDto(comment)))
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
    void itemDtoTest() throws Exception {

        JsonContent<ItemDto> result = this.json.write(this.itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

    }

}
