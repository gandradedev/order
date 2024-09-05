package br.com.order.domain.item.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException() {
        super("Item not found");
    }
}
