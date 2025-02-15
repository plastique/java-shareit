package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.contracts.ItemRepositoryInterface;
import ru.practicum.shareit.item.contracts.ItemServiceInterface;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.EmptyId;
import ru.practicum.shareit.exception.InvalidOwner;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.contracts.UserRepositoryInterface;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService implements ItemServiceInterface {

    private final ItemRepositoryInterface itemRepository;
    private final UserRepositoryInterface userRepository;

    public ItemDto create(final ItemDto itemDto, final Integer userId) {

        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("User not found");
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);

        Item newItem = itemRepository.create(item);
        itemDto.setId(newItem.getId());

        return itemDto;
    }

    @Override
    public ItemDto update(final ItemDto itemDto, final Integer userId) {
        if (itemDto.getId() == null) {
            throw new EmptyId("id is empty");
        }

        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("User not found");
        }

        Item item = itemRepository.findById(itemDto.getId());

        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        if (!Objects.equals(item.getOwner(), userId)) {
            throw new InvalidOwner("Owner is not the same user");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        item.setAvailable(itemDto.getAvailable());

        itemRepository.update(item);

        return itemDto;
    }

    @Override
    public ItemDto findItemById(Integer itemId) {
        Item item = itemRepository.findById(itemId);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findItemsByUser(final Integer userId) {
        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("User not found");
        }

        return itemRepository
                .findByUserId(userId)
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
                .findByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

}
