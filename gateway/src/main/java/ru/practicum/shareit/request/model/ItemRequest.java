package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.request.dto.ItemShortDto;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemShortDto> items;
}
