package ru.practicum.shareit.request.dto;

//-import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

//@AllArgsConstructor
//@NoArgsConstructor
@Data
@Builder
public class ItemRequestResponseDto {
    //ответ исход из контроллера

    private long id;

    //-@NotNull
    private String description;

    private LocalDateTime created;

    private List<ItemShortDto> items;
}
