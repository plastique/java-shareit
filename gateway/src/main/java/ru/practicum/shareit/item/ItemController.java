package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody ItemCreateDto dto,
            @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        return itemClient.create(dto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @Valid @RequestBody ItemUpdateDto dto,
            @PathVariable Long itemId,
            @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {

        return itemClient.update(dto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable Long itemId,
            @RequestHeader(name = HEADER_USER_ID) Long userId
    ) {
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(name = HEADER_USER_ID) Long ownerId) {
        return itemClient.findItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestParam String text) {
        return itemClient.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @PathVariable Long itemId,
            @RequestHeader(name = HEADER_USER_ID) Long authorId,
            @RequestBody @Valid CommentCreateDto commentDto
    ) {
        return itemClient.addComment(itemId, authorId, commentDto);
    }

}
