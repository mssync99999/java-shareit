package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item, Long itemId, Long userId);

    Optional<Item> findById(Long itemId);

    List<Item> findItemsAll(Long userId);

    List<Item> searchByText(String text);
}
