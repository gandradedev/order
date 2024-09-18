package br.com.order.domain.item.service;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import br.com.order.domain.item.exception.ItemNotFoundException;
import br.com.order.domain.item.model.Item;
import br.com.order.domain.item.repository.ItemRepository;
import br.com.order.domain.utils.FileUtils;
import br.com.order.infrastructure.aws.s3.exception.S3IntegrationException;
import br.com.order.infrastructure.aws.s3.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final String FILE_NAME = "/image";

    private ItemRepository repository;

    private ObjectMapper mapper;

    private S3Service s3Service;

    @Override
    public ItemResponse create(final ItemRequest itemRequest) {
        log.info("Starting the item creation flow for request {}", itemRequest);

        Item item = mapper.convertValue(itemRequest, Item.class);
        String imageKey = generateImageKey(itemRequest.getImage());
        item.setImageS3Key(imageKey);

        updateItemImage(item, itemRequest);
        ItemResponse itemResponse = mapper.convertValue(repository.save(item), ItemResponse.class);

        log.info("Item {} created successfully", itemResponse);
        return itemResponse;
    }

    @Override
    public ItemResponse update(final String itemId,
                               final ItemRequest itemRequest) {
        log.info("Starting the item update flow for itemId {} and request {}", itemId, itemRequest);

        return repository.findById(itemId)
            .map(item -> {
                updateItemImage(item, itemRequest);
                item.setName(itemRequest.getName());
                item.setDescription(itemRequest.getDescription());
                item.setAmountInCents(itemRequest.getAmountInCents());
                ItemResponse itemResponse = mapper.convertValue(repository.save(item), ItemResponse.class);
                log.info("Item {} updated successfully", itemResponse);
                return itemResponse;
            }).orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public void delete(final String itemId) {
        log.info("Starting the item delete flow for itemId {}", itemId);

        try {
            repository.findById(itemId)
                .ifPresent(item -> s3Service.deleteFile(item.getImageS3Key()));
        } catch (S3IntegrationException e) {
            log.error("{}, the item image was not removed from S3", e.getMessage());
        }
        repository.deleteById(itemId);

        log.info("ItemId {} deleted successfully", itemId);
    }

    @Override
    public ItemResponse find(final String itemId) {
        return repository.findById(itemId)
            .map(item -> mapper.convertValue(item, ItemResponse.class))
            .orElseThrow(ItemNotFoundException::new);
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

    private void updateItemImage(Item item,
                                 final ItemRequest itemRequest) {
        try {
            uploadImageOnS3(item, itemRequest);
            if (Objects.isNull(item.getImageUrl())) {
                item.setImageUrl(s3Service.getObjectUrl(item.getImageS3Key()));
            }
        } catch (S3IntegrationException e) {
            log.error("{}, the item image has not been updated", e.getMessage());
        }
    }

    private void uploadImageOnS3(final Item item, final ItemRequest itemRequest) {
        String fileName = itemRequest.getImage().getOriginalFilename();
        s3Service.uploadFile(fileName, item.getImageS3Key(), itemRequest.getImage());

    }

    private String generateImageKey(final MultipartFile image) {;
        return UUID.randomUUID().toString()
            .concat(FILE_NAME)
            .concat(FileUtils.getFileExtension(image));
    }

}
