package br.com.order.api.item.controller;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface ItemController {

    ResponseEntity<ItemResponse> create(@ModelAttribute @Valid ItemRequest item);

    ResponseEntity<ItemResponse> update(@PathVariable String itemId, @ModelAttribute @Valid ItemRequest item);

    ResponseEntity<?> delete(@PathVariable String itemId);

    ResponseEntity<ItemResponse> get(@PathVariable String itemId);

    ResponseEntity<Page<ItemResponse>> list(@ParameterObject @PageableDefault(
        page = 0,
        size = 20,
        sort = "id",
        direction = Sort.Direction.DESC) Pageable pageable);
}
