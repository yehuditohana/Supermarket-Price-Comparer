package com.example.mystore.dto.common;
/**
 * A simple data holder representing an image result for an item,
 * containing the item's ID, name, and a URL pointing to its image.
 */
public class ImageResultDTO {
    private final String itemId;
    private final String itemName;
    private final String imageUrl;

    public ImageResultDTO(String itemId, String itemName, String imageUrl) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}