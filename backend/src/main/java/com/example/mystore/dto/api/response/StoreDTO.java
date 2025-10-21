package com.example.mystore.dto.api.response;
/**
 * StoreDTO represents a store available for product price comparison on the frontend.
 *
 * Fields:
 * - storeId: Unique identifier of the store.
 * - chainName: Name of the supermarket chain the store belongs to (e.g., Shufersal, Rami Levi).
 * - storeName: Display name of the specific store.
 * - storeNumber: Internal store number provided by the chain.
 * - storeCity: City where the store is located.
 * - storeAddress: Full address of the store.
 * - chainImageUrl: URL of the chain's logo.
 *
 * Used for displaying available stores to users, allowing them to select stores for price comparison.
 */

public class StoreDTO {
     private  Long storeId;
     private String chainName;
     private String storeName;
     private Long storeNumber;
     private String storeCity;
     private String storeAddress;
     private String chainImageUrl;

    public StoreDTO(Long storeId, String chainName, String storeName, Long storeNumber, String storeCity, String storeAddress, String chainImageUrl) {
        this.storeId = storeId;
        this.chainName = chainName;
        this.storeName = storeName;
        this.storeNumber = storeNumber;
        this.storeCity = storeCity;
        this.storeAddress = storeAddress;
        this.chainImageUrl = chainImageUrl;
    }


    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getChainImageUrl() {
        return chainImageUrl;
    }

    public void setChainImageUrl(String chainImageUrl) {
        this.chainImageUrl = chainImageUrl;
    }
}
