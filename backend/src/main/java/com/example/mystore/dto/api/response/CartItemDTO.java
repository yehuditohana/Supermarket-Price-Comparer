package com.example.mystore.dto.api.response;

/**
 * CartItemDTO represents an item displayed inside a user's shopping cart on the frontend.
 *
 * Fields:
 * - itemId: Unique identifier of the item.
 * - itemName: Display name of the item.
 * - imageUrl: URL of the item's image.
 * - quantity: Quantity of the item in the cart.
 * - minPrice: Minimum price found for a single unit.
 * - maxPrice: Maximum price found for a single unit.
 * - totalMinPrice: Total minimum price based on quantity (minPrice * quantity).
 * - totalMaxPrice: Total maximum price based on quantity (maxPrice * quantity).
 *
 * Used to present cart items with price range.
 */
public class CartItemDTO {
    private String itemId;
    private String itemName;
    private String imageUrl;
    private Integer quantity = 0;
    private  Double minPrice;
    private Double maxPrice;
    private Double totalMinPrice;
    private Double totalMaxPrice;

    public CartItemDTO(String itemId, String itemName, String imageUrl,
                       Integer quantity, Double totalMinPrice, Double totalMaxPrice ,Double minPrice , Double maxPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalMinPrice = totalMinPrice;
        this.totalMaxPrice = totalMaxPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getTotalMinPrice() {
        return totalMinPrice;
    }

    public void setTotalMinPrice(Double totalMinPrice) {
        this.totalMinPrice = totalMinPrice;
    }

    public Double getTotalMaxPrice() {
        return totalMaxPrice;
    }
    public void setTotalMaxPrice(Double totalMaxPrice) {
        this.totalMaxPrice = totalMaxPrice;
    }
}
