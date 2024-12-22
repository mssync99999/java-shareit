package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    //1.POST /requests — добавить новый запрос вещи.
    //Основная часть запроса — текст запроса, в котором пользователь описывает, какая именно вещь ему нужна.
    //@PostMapping
    public ResponseEntity<Object> create(Long userId,
                                         ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    //2.GET /requests — получить список своих запросов вместе с данными об ответах на них.
    //Для каждого запроса должны быть указаны описание, дата и время создания, а также
    // список ответов в формате: id вещи, название, id владельца.
    // В дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой
    // из них. Запросы должны возвращаться отсортированными от более новых к более старым.
    //@GetMapping
    public ResponseEntity<Object> findItemRequestAll(Long userId) {
        return get("", userId);
    }

    //3. GET /requests/all — получить список запросов, созданных другими пользователями.
    //С помощью этого эндпоинта пользователи смогут просматривать существующие запросы,
    // на которые они могли бы ответить. Запросы сортируются по дате создания от более новых к
    // более старым.
    //@GetMapping("/all")
    public ResponseEntity<Object> findItemRequestOtherAll(Long userId) {
        return get("/all", userId);
    }

    //4. GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об
    // ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном
    // запросе может любой пользователь.
    //@GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequest(Long userId,
                                                  Long requestId) {
        return get("/" + requestId, userId);
    }


}
