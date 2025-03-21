package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemCreateDto {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

}
