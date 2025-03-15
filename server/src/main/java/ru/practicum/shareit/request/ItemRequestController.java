package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.contracts.ItemRequestServiceInterface;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestServiceInterface itemRequestService;

    @PostMapping
    public ItemRequestResponseDto createItem(
            final @RequestHeader(HEADER_USER_ID) Long userId,
            final @RequestBody ItemRequestCreateDto itemRequestCreateDto
    ) {
        itemRequestCreateDto.setUserId(userId);

        return itemRequestService.create(itemRequestCreateDto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getListByUser(final @RequestHeader(name = HEADER_USER_ID) Long userId) {
        return itemRequestService.getListByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getList(final @RequestHeader(name = HEADER_USER_ID) Long userId) {
        return itemRequestService.getList(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(final @PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }

}
