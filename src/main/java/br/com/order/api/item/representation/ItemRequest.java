package br.com.order.api.item.representation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long amountInCents;

//    private MultipartFile image;
}
