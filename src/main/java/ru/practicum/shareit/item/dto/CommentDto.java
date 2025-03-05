package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {

    @Nullable
    private Long id;

    @NotBlank
    private String text;

}
