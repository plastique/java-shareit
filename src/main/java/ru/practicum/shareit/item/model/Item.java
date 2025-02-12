package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
@Builder
public class Item {
    private Integer id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
