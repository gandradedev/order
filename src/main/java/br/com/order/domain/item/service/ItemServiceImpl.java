package br.com.order.domain.item.service;

import br.com.order.api.item.representation.ItemRequest;
import br.com.order.api.item.representation.ItemResponse;
import br.com.order.domain.item.exception.ItemNotFoundException;
import br.com.order.domain.item.model.Item;
import br.com.order.domain.item.repository.ItemRepository;
import br.com.order.domain.utils.FileUtils;
import br.com.order.infrastructure.aws.s3.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
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
    public ItemResponse create(final ItemRequest itemRequest) throws IOException {
        Item item = mapper.convertValue(itemRequest, Item.class);

        String fileName = itemRequest.getImage().getOriginalFilename();
        String imageKey = generateImageKey(itemRequest.getImage());

        item.setImageS3Key(imageKey);

        s3Service.uploadFile(fileName, imageKey, itemRequest.getImage().getBytes());
        URL url = s3Service.getObjectUrl(imageKey);
        item.setImageUrl(url);

        return mapper.convertValue(repository.save(item), ItemResponse.class);
    }

    @Override
    public ItemResponse update(final String itemId,
                               final ItemRequest itemRequest) {
        return repository.findById(itemId)
            .map(item -> {
                updateImageOnS3(item, itemRequest);
                item.setName(itemRequest.getName());
                item.setDescription(itemRequest.getDescription());
                item.setAmountInCents(itemRequest.getAmountInCents());
                return mapper.convertValue(repository.save(item), ItemResponse.class);
            }).orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public void delete(final String itemId) {
        repository.findById(itemId)
            .ifPresent(item -> s3Service.deleteFile(item.getImageS3Key()));
        repository.deleteById(itemId);
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

    private void updateImageOnS3(final Item item, final ItemRequest itemRequest) {
        try {
            String fileName = itemRequest.getImage().getOriginalFilename();
            s3Service.uploadFile(fileName, item.getImageS3Key(), itemRequest.getImage().getBytes());
        } catch (IOException e) {
            log.error("Error to update file in S3 bucket for itemId {}", item.getId());
        }
    }

    private String generateImageKey(final MultipartFile image) {
        String extension = FileUtils.getFileExtension(image);
        return UUID.randomUUID().toString()
            .concat(FILE_NAME)
            .concat(extension);
    }

}
