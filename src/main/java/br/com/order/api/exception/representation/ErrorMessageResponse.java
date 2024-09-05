package br.com.order.api.exception.representation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessageResponse {

    private String message;

}
