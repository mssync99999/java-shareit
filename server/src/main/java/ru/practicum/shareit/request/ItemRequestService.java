package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> findItemRequestAll(Long userId);

    List<ItemRequestResponseDto> findItemRequestOtherAll(Long userId);

    ItemRequestResponseDto findItemRequest(Long userId, Long requestId);

}
