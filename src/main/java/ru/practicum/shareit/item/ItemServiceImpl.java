package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepositoryImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryImpl itemRepositoryImpl;
    private final UserServiceImpl userServiceImpl;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        User user = UserMapper.toUser(userServiceImpl.findById(userId));
        item.setOwner(user);

        return ItemMapper.toItemDto(itemRepositoryImpl.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto,
                          Long itemId,
                          Long userId) {
        itemDto.setId(itemId);

        Item item = ItemMapper.toItem(itemDto);
        User user = UserMapper.toUser(userServiceImpl.findById(userId));
        item.setOwner(user);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь вещи не найден или отличается");
        }

        return ItemMapper.toItemDto(itemRepositoryImpl.update(item, itemId, userId));
    }

    @Override
    public ItemDto findById(Long itemId) {
        return ItemMapper.toItemDto(itemRepositoryImpl.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден")));
    }

    @Override
    public List<ItemDto> findItemsAll(Long userId) {
        return itemRepositoryImpl.findItemsAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        return itemRepositoryImpl.searchByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
