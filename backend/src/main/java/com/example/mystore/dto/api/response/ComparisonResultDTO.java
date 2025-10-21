package com.example.mystore.dto.api.response;

import java.util.List;

/**
 * ComparisonResultDTO represents the result of comparing a shopping cart's prices in a specific store.
 *
 * Fields:
 * - store: A StoreDTO containing information about the store.
 * - items: A list of ItemWithPriceDTO representing each item and its price in the store.
 * - cartPrice: The total price of the shopping cart in the store.
 */

public class ComparisonResultDTO {
    private StoreDTO store;
    private List<ItemWithPriceDTO> items;
    private Double  cartPrice;

    public ComparisonResultDTO(StoreDTO store, List<ItemWithPriceDTO> items, Double  cartPrice) {
        this.store = store;
        this.items = items;
        this.cartPrice = cartPrice;
    }

    public StoreDTO getStore() {
        return store;
    }

    public void setStore(StoreDTO store) {
        this.store = store;
    }

    public List<ItemWithPriceDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemWithPriceDTO> items) {
        this.items = items;
    }
    public Double getCartPrice() {
        return cartPrice;
    }
    public void setCartPrice(Double cartPrice) {
        this.cartPrice = cartPrice;
    }

}