package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//юнит тесты моки
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private BookingRepository bookingRepositoryImpl;
    @Mock
    private UserRepository userRepositoryImpl;
    @Mock
    private ItemRepository itemRepositoryImpl;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private BookingServiceImpl bookingServiceImpl;
    @InjectMocks
    private ItemServiceImpl itemServiceImpl;
    @Mock
    private CommentRepository commentRepositoryImpl;
    @Mock
    private ItemRequestRepository itemRequestRepositoryImpl;

    private User owner = User.builder()
            .id(1L)
            .name("name")
            .email("name@mail.ru")
            .build();
    private User booker = User.builder()
            .id(2L)
            .name("booker")
            .email("booker@mail.ru")
            .build();
    private UserDto bookerDto = UserMapper.toUserDto(booker);

    private Item item = Item.builder()
            .name("item")
            .description("description")
            .available(true)
            .owner(owner)
            .id(1L)
            .build();
    private ItemDto itemDto = ItemMapper.toItemDto(item);

    private Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .booker(booker)
            .item(item)
            .status(Status.WAITING)
            .build();
    private BookingDto bookingDto = BookingDto.builder()
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusDays(1))
            .itemId(1L)
            .build();

    @Test
    void createTest() {
        when(itemRepositoryImpl.save(any())).thenReturn(item);
        when(userServiceImpl.findById(anyLong())).thenReturn(bookerDto);

        ItemDto response = itemServiceImpl.create(itemDto, 1L);
        assertThat(1L, equalTo(response.getId()));
    }

    @Test
    void updateTest() {
        ItemDto itemDtoTest = ItemDto.builder()
                .name("new name")
                .description("new description")
                .available(true)
                .owner(owner)
                .id(1L)
                .build();

        Item itemTest = ItemMapper.toItem(itemDtoTest);

        when(itemRepositoryImpl.save(any())).thenReturn(itemTest);
        when(userServiceImpl.findById(anyLong())).thenReturn(bookerDto);
        when(itemRepositoryImpl.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto response = itemServiceImpl.update(itemDtoTest, 1L, 1L);

        assertThat("new name", equalTo(response.getName()));
        assertThat("new description", equalTo(response.getDescription()));
    }

    @Test
    void findByIdTest() {
        when(itemRepositoryImpl.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto response = itemServiceImpl.findById(1L, 1L);

        assertThat(1L, equalTo(response.getId()));
        assertThat("item", equalTo(response.getName()));
    }

    @Test
    void findItemsAllTest() {
        when(itemRepositoryImpl.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepositoryImpl.findAllByOwnerId(anyLong())).thenReturn(List.of(item));

        List<ItemDto> response = itemServiceImpl.findItemsAll(1L);

        assertThat(1L, equalTo(response.get(0).getId()));
        assertThat("item", equalTo(response.get(0).getName()));
    }

    @Test
    void searchByTextTest() {
        when(itemRepositoryImpl.searchByText(anyString())).thenReturn(List.of(item));

        List<ItemDto> response = itemServiceImpl.searchByText("item");

        assertThat(1L, equalTo(response.get(0).getId()));
        assertThat("item", equalTo(response.get(0).getName()));
    }

}

