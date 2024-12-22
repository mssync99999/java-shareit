package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validated.Create;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    //1.POST /requests — добавить новый запрос вещи.
    //Основная часть запроса — текст запроса, в котором пользователь описывает, какая именно вещь ему нужна.
    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class})  @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(userId, itemRequestDto);
    }

    //2.GET /requests — получить список своих запросов вместе с данными об ответах на них.
    //Для каждого запроса должны быть указаны описание, дата и время создания, а также
    // список ответов в формате: id вещи, название, id владельца.
    // В дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой
    // из них. Запросы должны возвращаться отсортированными от более новых к более старым.
    @GetMapping
    public ResponseEntity<Object> findItemRequestAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.findItemRequestAll(userId);
    }

    //3. GET /requests/all — получить список запросов, созданных другими пользователями.
    //С помощью этого эндпоинта пользователи смогут просматривать существующие запросы,
    // на которые они могли бы ответить. Запросы сортируются по дате создания от более новых к
    // более старым.
    @GetMapping("/all")
    public ResponseEntity<Object> findItemRequestOtherAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.findItemRequestOtherAll(userId);
    }

    //4. GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об
    // ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном
    // запросе может любой пользователь.
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable Long requestId) {
        return itemRequestClient.findItemRequest(userId, requestId);
    }

}
