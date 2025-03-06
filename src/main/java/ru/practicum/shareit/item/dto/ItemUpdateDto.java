package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemUpdateDto {

    private Long id;

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private Boolean available;

}
