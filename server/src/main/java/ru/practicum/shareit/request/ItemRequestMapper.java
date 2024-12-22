package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems() != null ? itemRequest.getItems() : null)
                .build();
    }
}
