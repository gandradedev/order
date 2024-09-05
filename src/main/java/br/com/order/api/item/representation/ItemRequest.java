package br.com.order.api.item.representation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Size(max = 200)
    private String description;

    @NotNull
    @Positive
    private Long amountInCents;

//    private MultipartFile image;
}
