package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private CommentRepository commentRepository;
    private UserServiceImpl userServiceImpl;
    private BookingServiceImpl bookingServiceImpl;
    private User owner;
    private Item item;
    private ItemRequest request;
    private Comment comment;


    @BeforeEach
    void start() {
        owner = userRepository.save(User.builder()
                .id(1L)
                .name("ownerName")
                .email("owner@mail.ru")
                .build());
        request = itemRequestRepository.save(ItemRequest.builder()
                .requestor(owner)
                .created(LocalDateTime.now())
                .description("description request")
                .build());
        item = itemRepository.save(Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .request(request.getId())
                .build());
        comment = commentRepository.save(Comment.builder()
                .id(1L)
                .text("texttext")
                .item(item)
                .author(owner)
                .created(LocalDateTime.now())
                .build());
    }

    @Test
    void findAllByOwnerIdTest() {
        assertThat(item.getId(), equalTo(itemRepository.findAllByOwnerId(owner.getId()).getFirst().getId()));
    }

    @Test
    void searchByTextTest() {
        assertThat(item.getId(), equalTo(itemRepository.searchByText("descr").getFirst().getId()));
    }

    @Test
    void findAllByRequestTest() {
        assertThat(item.getId(), equalTo(itemRepository.findAllByRequest(item.getRequest()).getFirst().getId()));
    }

    @Test
    void findAllByItemTest() {
        assertThat(comment.getId(), equalTo(commentRepository.findAllByItem(item).getFirst().getId()));
    }

}