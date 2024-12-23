package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
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
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;
    @Mock
    private ItemServiceImpl itemServiceImpl;

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

    private Comment comment = Comment.builder()
            .id(1L)
            .text("comment")
            .item(new Item())
            .author(null)
            .created(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
            .build();
    private CommentDto commentDto = CommentMapper.toCommentDto(comment);

    private Item item = Item.builder()
            .name("item")
            .description("description")
            .available(true)
            .owner(owner)
            .id(1L)
            .comments(List.of(commentDto))
            .build();
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
        when(userServiceImpl.findById(anyLong())).thenReturn(bookerDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponseDto response = BookingMapper.toBookingResponseDto(booking);
        BookingResponseDto response1 = bookingServiceImpl.create(2L, bookingDto);

        assertThat(response.getId(), equalTo(response1.getId()));
    }


    @Test
    void updateApprovedTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponseDto response = BookingMapper.toBookingResponseDto(booking);
        BookingResponseDto response1 = bookingServiceImpl.updateApproved(booking.getId(), 1L, true);

        assertThat(response.getId(), equalTo(response1.getId()));
        assertThat(Status.APPROVED, equalTo(response1.getStatus()));
    }

    @Test
    void findBookingTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingResponseDto response = BookingMapper.toBookingResponseDto(booking);
        BookingResponseDto response1 = bookingServiceImpl.findBooking(booking.getId(), 1L);

        assertThat(response.getId(), equalTo(response1.getId()));
        assertThat(response.getStart(), equalTo(response1.getStart()));
        assertThat(response.getEnd(), equalTo(response1.getEnd()));
    }

    @Test
    void findBookingAllTest() {
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = List.of(booking);

        when(bookingRepository.findAllByBookerId(booker.getId(), sort)).thenReturn(bookings);
        when(userServiceImpl.findById(anyLong())).thenReturn(bookerDto);

        assertThat(bookings.stream().map(BookingMapper::toBookingResponseDto).toList().getFirst().getId(),
                equalTo(bookingServiceImpl.findBookingAll(booker.getId(), State.ALL).getFirst().getId()));

        verify(bookingRepository, times(1)).findAllByBookerId(booker.getId(), sort);
    }


    @Test
    void createCommentTest() {
        //Sort sort = Sort.by(Sort.Direction.DESC, "start");
        //List<Booking> bookings = List.of(booking);

        //when(userRepository.findById(any())).thenReturn(null);
        //when(itemRepository.findById(any())).thenReturn(null);
        //when(bookingRepository.findAllByBookerAndEndIsBeforeAndItemAndStatusEquals(any(), any(), any(), any())).thenReturn(null);
        when(itemServiceImpl.createComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        CommentDto response = itemServiceImpl.createComment(1L, 1L, commentDto);

        assertThat(response.getId(), equalTo(commentDto.getId()));
    }
}