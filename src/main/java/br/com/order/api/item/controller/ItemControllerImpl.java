package br.com.order.api.item.controller;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import br.com.order.domain.item.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("item")
public class ItemControllerImpl implements ItemController {

    private ItemService service;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> create(final ItemRequest item) {
        log.info("Received request to create item with payload {}", item);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(item));
    }

    @Override
    @PutMapping(value = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> update(final String itemId, final ItemRequest item) {
        log.info("Received request to update item with itemId {} and payload {}",
            itemId,
            item);
        return ResponseEntity.ok(service.update(itemId, item));
    }

    @Override
    @DeleteMapping(value = "/{itemId}")
    public ResponseEntity<?> delete(final String itemId) {
        log.info("Received request to delete item with itemId {}", itemId);
        service.delete(itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping(value = "/{itemId}")
    public ResponseEntity<ItemResponse> get(final String itemId) {
        log.info("Received request to get item by itemId {}", itemId);
        return ResponseEntity.ok(service.find(itemId));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> list(final Pageable pageable) {
        log.info("Received request to get item list");
        return ResponseEntity.ok(service.findAll(pageable));
    }
}
