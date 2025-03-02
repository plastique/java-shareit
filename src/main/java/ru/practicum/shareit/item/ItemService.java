package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.InvalidOwnerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService implements ItemServiceInterface {

    private static final String USER_NOT_FOUND = "User with id='%d' not found";

    private final ItemRepositoryInterface itemRepository;
    private final UserRepositoryInterface userRepository;

    @Override
    public ItemDto create(final ItemDto itemDto, final Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND.formatted(userId));
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user.get());

        Item newItem = itemRepository.save(item);
        itemDto.setId(newItem.getId());

        return itemDto;
    }

    @Override
    public ItemDto update(final ItemDto itemDto, final Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND.formatted(userId));
        }

        Optional<Item> itemOptional = itemRepository.findById(itemDto.getId());

        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Item with id='%d' not found".formatted(itemDto.getId()));
        }

        Item item = itemOptional.get();

        if (!Objects.equals(item.getOwner(), user.get())) {
            throw new InvalidOwnerException("Owner is not the same user");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        item.setAvailable(itemDto.getAvailable());

        itemRepository.save(item);

        return itemDto;
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        return item.map(ItemMapper::toItemDto).orElse(null);
    }

    @Override
    public List<ItemDto> findItemsByUser(final Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND.formatted(userId));
        }

        return itemRepository
                .findAllByOwnerId(user.get().getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> findItemsByText(final String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository
                .findAllByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

}
