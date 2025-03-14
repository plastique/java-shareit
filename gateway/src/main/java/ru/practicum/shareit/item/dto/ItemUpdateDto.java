package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemUpdateDto {

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private Boolean available;

}
