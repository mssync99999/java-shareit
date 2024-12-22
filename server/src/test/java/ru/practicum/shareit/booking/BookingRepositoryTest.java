package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private UserServiceImpl userServiceImpl;
    private BookingServiceImpl bookingServiceImpl;

    private User owner;
    private Item item;
    private User booker;
    private Booking booking;
    private Sort sort;

    @BeforeEach
    void start() {
        owner = userRepository.save(User.builder()
                .id(1L)
                .name("ownerName")
                .email("owner@mail.ru")
                .build());
        item = itemRepository.save(Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build());
        booker = userRepository.save(User.builder()
                .id(2L)
                .name("tenantName")
                .email("tenant@mail.ru")
                .build());
        booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(1))
                .build());
        sort = Sort.by(Sort.Direction.DESC, "start");
    }


    @Test
    void findAllByBookerIdTest() {
        assertThat(booking.getId(), equalTo(bookingRepository.findAllByBookerId(booker.getId(), sort).getFirst().getId()));
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(1))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                booker.getId(), LocalDateTime.now(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findAllByBookerAndStartAfterAndEndAfterOrderByStartDescTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByBookerAndStartAfterAndEndAfterOrderByStartDesc(
                booker, LocalDateTime.now(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findJpqlQueryFutureTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findJpqlQueryFuture(
                booker.getId(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findJpqlQueryStatusTest() {

        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findJpqlQueryStatus(
                booker.getId(), LocalDateTime.now(), Status.APPROVED).getFirst().getId()));
    }

    @Test
    void findAllByItemOwnerIdTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByItemOwnerId(
                owner.getId(), sort).getFirst().getId()));
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                owner.getId(), LocalDateTime.now(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findAllByItemOwnerAndStartAfterAndEndAfterOrderByStartDescTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByItemOwnerAndStartAfterAndEndAfterOrderByStartDesc(
                owner, LocalDateTime.now(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findJpqlQueryFutureOwnerTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findJpqlQueryFutureOwner(
                owner.getId(), LocalDateTime.now()).getFirst().getId()));
    }

    @Test
    void findJpqlQueryStatusOwnerTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findJpqlQueryStatusOwner(
                owner.getId(), LocalDateTime.now(), Status.APPROVED).getFirst().getId()));
    }

    @Test
    void findAllByBookerAndEndIsBeforeAndItemAndStatusEqualsTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(2))
                .item(item)
                .end(LocalDateTime.now().minusDays(1))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findAllByBookerAndEndIsBeforeAndItemAndStatusEquals(
                booker, LocalDateTime.now(), item, Status.APPROVED).getFirst().getId()));
    }

    @Test
    void findFirst1ByItemAndStartBeforeOrderByStartDescTest() {
        assertThat(booking.getId(), equalTo(bookingRepository.findFirst1ByItemAndStartBeforeOrderByStartDesc(
                item, LocalDateTime.now()).getId()));
    }

    @Test
    void findFirst1ByItemAndStartAfterOrderByStartAscTest() {
        Booking bookingTest = bookingRepository.save(Booking.builder()
                .booker(booker)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1))
                .item(item)
                .end(LocalDateTime.now().plusDays(2))
                .build());

        assertThat(bookingTest.getId(), equalTo(bookingRepository.findFirst1ByItemAndStartAfterOrderByStartAsc(
                item, LocalDateTime.now()).getId()));
    }

}
