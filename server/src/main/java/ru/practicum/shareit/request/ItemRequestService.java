package ru.practicum.shareit.request;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.validated.Create;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> findItemRequestAll(Long userId);

    List<ItemRequestResponseDto> findItemRequestOtherAll(Long userId);

    ItemRequestResponseDto findItemRequest(Long userId, Long requestId);

}
