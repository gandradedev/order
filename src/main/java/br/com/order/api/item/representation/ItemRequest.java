package br.com.order.api.item.representation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    @JsonIgnore
    @NotNull
    private MultipartFile image;
}
