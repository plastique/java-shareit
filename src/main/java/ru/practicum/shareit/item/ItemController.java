package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemServiceInterface itemService;

    @PostMapping
    public ItemDto create(final ItemDto dto, @RequestHeader(name = "X-Sharer-User-Id") String userId) {
        return itemService.create(dto, userId);
    }

}
