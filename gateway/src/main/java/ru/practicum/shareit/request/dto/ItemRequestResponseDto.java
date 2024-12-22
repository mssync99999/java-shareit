package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestResponseDto {
    //ответ исход из контроллера

    private long id;

    @NotNull
    private String description;
    private LocalDateTime created;
    private List<ItemShortDto> items;
}
