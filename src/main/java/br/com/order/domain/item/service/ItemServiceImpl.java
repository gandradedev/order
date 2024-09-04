package br.com.order.domain.item.service;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import br.com.order.domain.item.model.Item;
import br.com.order.domain.item.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository repository;

    private ObjectMapper mapper;

    @Override
    public ItemResponse create(final ItemRequest itemRequest) {
        Item item = mapper.convertValue(itemRequest, Item.class);
        return mapper.convertValue(repository.save(item), ItemResponse.class);
    }

    @Override
    public ItemResponse update(final String itemId,
                               final ItemRequest itemRequest) {
        return repository.findById(itemId)
            .map(item -> {
                item.setName(itemRequest.getName());
                item.setDescription(itemRequest.getDescription());
                item.setAmountInCents(itemRequest.getAmountInCents());
                return mapper.convertValue(repository.save(item), ItemResponse.class);
            }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(final String itemId) {
        repository.deleteById(itemId);
    }

    @Override
    public ItemResponse find(final String itemId) {
        return repository.findById(itemId)
            .map(item -> mapper.convertValue(item, ItemResponse.class))
            .orElseThrow(RuntimeException::new);
    }

    @Override
    public Page<ItemResponse> findAll(final Pageable pageable) {
        return repository.findAll(pageable)
            .map(item -> ItemResponse.builder()
                .withId(item.getId())
                .withName(item.getName())
                .withDescription(item.getDescription())
                .withAmountInCents(item.getAmountInCents())
                .withCreatedAt(item.getCreatedAt())
                .withUpdatedAt(item.getUpdatedAt())
                .build());
    }
}
