package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.EmptyIdException;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private ItemServiceInterface itemService;

    @PostMapping
    public ItemDto create(
            final @RequestBody ItemCreateDto dto,
            final @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        return itemService.create(dto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            final @RequestBody ItemUpdateDto dto,
            final @PathVariable Long itemId,
            final @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        if (itemId == null) {
            throw new EmptyIdException("Id required");
        }

        dto.setId(itemId);

        return itemService.update(dto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItem(
            final @PathVariable Long itemId,
            final @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsByOwner(final @RequestHeader(name = HEADER_USER_ID) Long ownerId) {
        return itemService.findItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(final @RequestParam String text) {
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            final @PathVariable Long itemId,
            final @RequestHeader(name = HEADER_USER_ID) Long authorId,
            final @RequestBody CommentCreateDto commentDto
    ) {
        return itemService.addComment(itemId, authorId, commentDto);
    }
}
