package ru.practicum.shareit.request.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepositoryInterface extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(Long id);

    List<ItemRequest> allOrderByCreatedDesc();

    List<ItemRequest> findAllByRequestor_IdIsNotOrderByCreatedDesc(Long id);

}
