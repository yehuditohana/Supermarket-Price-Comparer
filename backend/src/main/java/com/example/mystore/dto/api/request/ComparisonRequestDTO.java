package com.example.mystore.dto.api.request;

import java.util.List;

/**
 * ComparisonRequestDTO represents the request body used for comparing cart prices across different stores.
 *
 * Fields:
 * - userId: The ID of the user performing the comparison.
 * - cartId: The ID of the shopping cart whose items are to be compared.
 * - storeIds: A list of store IDs where the cart will be priced and compared.
 */

public class ComparisonRequestDTO{
    private Long userId;
    private Long cartId;
    private List<Long> storeIds;

    public ComparisonRequestDTO(Long userId, Long cartId, List<Long> storeIds) {
        this.userId = userId;
        this.cartId = cartId;
        this.storeIds = storeIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<Long> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
    }
}
