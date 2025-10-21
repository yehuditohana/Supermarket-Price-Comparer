package com.example.mystore.dto.api.response;

/**
 * ItemWithPriceDTO represents an item in the cart along with its price information per store.
 *
 * Fields:
 * - itemId: The unique identifier of the item.
 * - itemName: The name of the item.
 * - imageUrl: The URL of the item's image.
 * - price: The price of the item in the specific store (null if not available).
 * - quantityOfItem: The quantity of the item in the cart.
 * - foundInStore: A boolean flag indicating whether the item was found in the specific store.
 *                 If false, the item is not included in the total cart price calculation.
 */
public class ItemWithPriceDTO {
    private String itemId;
    private String itemName;
    private String imageUrl;
    private Double price;
    private Integer quantityOfItem;
    private boolean foundInStore;

    public ItemWithPriceDTO(String itemId, String itemName, String imageUrl, Double price, Integer quantityOfItem, boolean foundInStore) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantityOfItem = quantityOfItem;
        this.foundInStore = foundInStore;
    }

    public Integer getQuantityOfItem() {
        return quantityOfItem;
    }

    public void setQuantityOfItem(Integer quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }

    public ItemWithPriceDTO(boolean foundInStore) {
        this.foundInStore = foundInStore;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isFoundInStore() {
        return foundInStore;
    }

    public void setFoundInStore(boolean foundInStore) {
        this.foundInStore = foundInStore;
    }
}
