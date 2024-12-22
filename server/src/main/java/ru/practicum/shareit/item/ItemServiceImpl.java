package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepositoryImpl;
    private final UserRepository userRepositoryImpl;
    private final UserServiceImpl userServiceImpl;
    private final BookingRepository bookingRepositoryImpl;
    private final CommentRepository commentRepositoryImpl;
    private final ItemRequestRepository itemRequestRepositoryImpl;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User user = UserMapper.toUser(userServiceImpl.findById(userId));
        item.setOwner(user);
/*--*/
        if (itemDto.getRequestId() != null) {
            itemRequestRepositoryImpl.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("ItemRequest не найден"));
        }
        //System.out.println("!!! itemDto=" + itemDto);
        //System.out.println("!!! item=" + item);

        return ItemMapper.toItemDto(itemRepositoryImpl.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {

        User user = UserMapper.toUser(userServiceImpl.findById(userId));
        Item item = ItemMapper.toItem(this.findById(itemId, userId));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не владелец вещи");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepositoryImpl.save(item));
    }

    @Override
    public ItemDto findById(Long itemId, Long userId) {
        Item item = itemRepositoryImpl.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
        if (item.getOwner().getId().equals(userId)) {
            this.lastDateBooking(item);
            this.nextDateBooking(item);
        }

        this.getComments(item);

        return ItemMapper.toItemDto(itemRepositoryImpl.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден")));
    }

    public void lastDateBooking(Item item) {
        item.setLastBooking(bookingRepositoryImpl.findFirst1ByItemAndStartBeforeOrderByStartDesc(item, LocalDateTime.now()));
    }

    public void nextDateBooking(Item item) {
        item.setLastBooking(bookingRepositoryImpl.findFirst1ByItemAndStartAfterOrderByStartAsc(item, LocalDateTime.now()));
    }


    public void getComments(Item item) {

        item.setComments(commentRepositoryImpl.findAllByItem(item).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

    }

    @Override
    public List<ItemDto> findItemsAll(Long userId) {
        return itemRepositoryImpl.findAllByOwnerId(userId).stream()
                .map(i -> this.findById(i.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepositoryImpl.searchByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(Long userId,
                                    Long itemId,
                                    CommentDto commentDto) {

        Comment comment = CommentMapper.toComment(commentDto);

        User user = userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));
        Item item = itemRepositoryImpl.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));

        if (bookingRepositoryImpl.findAllByBookerAndEndIsBeforeAndItemAndStatusEquals(user, LocalDateTime.now(),
                item, Status.APPROVED).isEmpty()) {
            throw new ValidationException("Пользователь должен оформить бронирование");
        }

        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.toCommentDto(commentRepositoryImpl.save(comment));
    }




}
