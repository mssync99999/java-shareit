package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? item.getOwner() : null)
                .requestId(item.getRequest())
                .lastBooking(item.getLastBooking() != null ? item.getLastBooking() : null)
                .nextBooking(item.getNextBooking() != null ? item.getNextBooking() : null)
                .comments(item.getComments() != null ? item.getComments() : null)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner() != null ? itemDto.getOwner() : null)
                .request(itemDto.getRequestId())
                .lastBooking(itemDto.getLastBooking() != null ? itemDto.getLastBooking() : null)
                .nextBooking(itemDto.getNextBooking() != null ? itemDto.getNextBooking() : null)
                .comments(itemDto.getComments() != null ? itemDto.getComments() : null)
                .build();
    }

    public static ItemShortDto toItemShortDto(Item item) {
        return ItemShortDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .ownerId(item.getId())
                .build();
    }
}
