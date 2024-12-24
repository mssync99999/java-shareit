package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.*;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//Transactional  каждый тест будет запускаться в отдельной транзакции. Spring откатывает каждую транзакцию в конце теста
//@SpringBootTest(properties = "jdbc.url=jdbc:postgresql://localhost:6541/test",
//или properties = "spring.profiles.active=test",
//        webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@RequiredArgsConstructor(onConstructor_ = @Autowired)

@Transactional
@SpringBootTest /*(properties = "spring.datasource.url=jdbc:postgresql://localhost:6432/shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)*/
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {

    //Реализовать интеграционные тесты, которые проверяют взаимодействие с базой данных.
    // Как вы помните, интеграционные тесты представляют собой более высокий уровень тестирования:
    // их обычно требуется меньше, но покрытие каждого — больше.
    // Мы предлагаем вам создать по одному интеграционному тесту для каждого крупного метода в ваших сервисах.
    // Например, для метода getUserItems в классе ItemServiceImpl
    @Autowired
    private BookingServiceImpl bookingServiceImpl;
    @Autowired
    private UserRepository userStorage;

    @Autowired
    private ItemRepository itemStorage;
    @Autowired
    private BookingRepository bookingStorage;


    private User owner;

    private Item item;
    private User booker;

    @BeforeEach
    void start() {
        owner = userStorage.save(User.builder()
                .id(1L)
                .name("ownerName")
                .email("owner@mail.ru")
                .build());

        item = itemStorage.save(Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build());
        booker = userStorage.save(User.builder()
                .id(2L)
                .name("tenantName")
                .email("tenant@mail.ru")
                .build());
    }

    @Test
    void findBookingAllTest() {
        //testCanGetPastBookingsOfTenant
        Booking booking = bookingStorage.save(Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.now().plusDays(1)) // .minusDays(2)
                .end(LocalDateTime.now().plusDays(2)) // .minusDays(1)
                .booker(booker)
                .item(item)
                .build());
        //System.out.println("!!! booking=" + booking);
        List<BookingResponseDto> bookings = bookingServiceImpl.findBookingAll(booker.getId(), State.PAST);
//System.out.println("!!! bookings=" + bookings);
        assertThat(1, equalTo(bookings.size()));
        assertThat(booking.getId(), equalTo(bookings.getFirst().getId()));
    }


    @Test
    void findBookingOwnerAllTest() {
        // testCanGetAllBookingsOfAllItemsOfOwner
        Booking booking = bookingStorage.save(Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(booker)
                .item(item)
                .build());

        List<BookingDto> bookings = bookingServiceImpl.findBookingOwnerAll(owner.getId(), State.ALL);

        assertThat(1, equalTo(bookings.size()));
        assertThat(booking.getId(), equalTo(bookings.getFirst().getId()));
    }




}
