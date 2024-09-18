package br.com.order.domain.item.service;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemResponse create(ItemRequest itemRequest);

    ItemResponse update(String itemId, ItemRequest item);

    void delete(String itemId);

    ItemResponse find(String itemId);

    Page<ItemResponse> findAll(Pageable pageable);
}
