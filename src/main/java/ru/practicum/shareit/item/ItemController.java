package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    //Добавление новой вещи. Будет происходить по эндпоинту POST /items.
    // На вход поступает объект ItemDto. userId в заголовке X-Sharer-User-Id — это идентификатор пользователя,
    // который добавляет вещь. Именно этот пользователь — владелец вещи.
    // Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.
    @PostMapping
    public ItemDto create(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.create(itemDto, userId);
    }

    //Редактирование вещи. Эндпоинт PATCH /items/{itemId}.
    // Изменить можно название, описание и статус доступа к аренде.
    // Редактировать вещь может только её владелец.
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.update(itemDto, itemId, userId);
    }

    //Просмотр информации о конкретной вещи по её идентификатору.
    // Эндпоинт GET /items/{itemId}. Информацию о вещи может просмотреть любой пользователь.
    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.findById(itemId, userId);
    }

    //Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них.
    // Эндпоинт GET /items.
    @GetMapping
    public List<ItemDto> findItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.findItemsAll(userId);
    }

    //Комментарий можно добавить по эндпоинту POST /items/{itemId}/comment,
    //создайте в контроллере метод для него.
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Validated({Create.class})
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto) {

        return itemServiceImpl.createComment(userId, itemId, commentDto);
    }

    //Поиск вещи потенциальным арендатором.
    // Пользователь передаёт в строке запроса текст,
    // и система ищет вещи, содержащие этот текст в названии или описании.
    // Происходит по эндпоинту /items/search?text={text}, в text передаётся текст для поиска.
    // Проверьте, что поиск возвращает только доступные для аренды вещи.
    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text) {
        return itemServiceImpl.searchByText(text);
    }
}