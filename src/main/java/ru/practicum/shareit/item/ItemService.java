package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    ItemDto findById(Long itemId, Long userId);

    List<ItemDto> findItemsAll(Long userId);

    List<ItemDto> searchByText(String text);

}
