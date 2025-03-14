package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody ItemRequestCreateDto itemRequestCreateDto
    ) {
        return itemRequestClient.create(userId, itemRequestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getListByUser(@RequestHeader(name = HEADER_USER_ID) Long userId) {
        return itemRequestClient.getListByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getList(@RequestHeader(name = HEADER_USER_ID) Long userId) {
        return itemRequestClient.getList(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId) {
        return itemRequestClient.getById(requestId);
    }

}
