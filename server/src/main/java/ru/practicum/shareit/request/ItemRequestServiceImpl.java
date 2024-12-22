package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepositoryImpl;
    private final ItemRepository itemRepositoryImpl;
    private final UserRepository userRepositoryImpl;
    private final BookingRepository bookingRepositoryImpl;
    private final CommentRepository commentRepositoryImpl;

    @Override
    public ItemRequestResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        User requestor = userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requestor);

        return ItemRequestMapper.toItemRequestResponseDto(itemRequestRepositoryImpl.save(itemRequest));
    }

    @Override
    public List<ItemRequestResponseDto> findItemRequestAll(Long userId) {
        //проверка существования пользователя
        User requestor = userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));

        //вместе с данными об ответах на него
        List<ItemRequestResponseDto> res = itemRequestRepositoryImpl.findAllByRequestorOrderByCreatedDesc(requestor).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());

        res.forEach(x -> x.setItems(this.findAnswersItemRequest(x.getId())));

        return res;
    }

    @Override
    public List<ItemRequestResponseDto> findItemRequestOtherAll(Long userId) {

        //проверка существования пользователя
        User requestor = userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));

        return itemRequestRepositoryImpl.findAllByRequestorNotOrderByCreatedDesc(requestor).stream()
                .map(ItemRequestMapper::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto findItemRequest(Long userId, Long requestId) {
        //проверка существования пользователя
        User requestor = userRepositoryImpl.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));

        ItemRequest itemRequest = itemRequestRepositoryImpl.findAllById(requestId);
        //вместе с данными об ответах на него
        itemRequest.setItems(this.findAnswersItemRequest(itemRequest.getId()));

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest);
    }

    public List<ItemShortDto> findAnswersItemRequest(Long requestId) {
        return itemRepositoryImpl.findAllByRequest(requestId).stream()
                .map(ItemMapper::toItemShortDto)
                .collect(Collectors.toList());
    }

}
