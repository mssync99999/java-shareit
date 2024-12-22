package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemIntegrationTest {
    @Autowired
    private BookingServiceImpl bookingServiceImpl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemServiceImpl itemServiceImpl;

    private User owner;
    private Item item;
    private User booker;

    @BeforeEach
    void start() {
        owner = userRepository.save(User.builder()
                .id(1L)
                .name("ownerName")
                .email("owner@mail.ru")
                .build());
        item = itemRepository.save(Item.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build());
        booker = userRepository.save(User.builder()
                .id(2L)
                .name("otherName")
                .email("other@mail.ru")
                .build());
    }

    @Test
    void findByIdTest() {
        ItemDto itemTest = itemServiceImpl.findById(item.getId(), 1L);
        assertThat(item.getId(), equalTo(itemTest.getId()));
    }
}
