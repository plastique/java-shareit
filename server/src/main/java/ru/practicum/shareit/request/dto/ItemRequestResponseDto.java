package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDto {

    private Long id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;

    public record ItemDto(Long id, String name) {
    }
}
