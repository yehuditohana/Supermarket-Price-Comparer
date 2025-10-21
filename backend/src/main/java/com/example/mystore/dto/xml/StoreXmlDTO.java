package com.example.mystore.dto.xml;
/**
 * StoreXmlDTO is used for parsing and processing store details from Store XML files.
 */
public class StoreXmlDTO {
    private Long    chainID;
    private Long    subChainID;
    private Long    storeNumber;
    private String  storeName;
    private String  storeCity;
    private String  storeAddress;
    private Long    storeZipCode;

    public Long getChainID() {
        return chainID;
    }

    public void setChainID(Long chainID) {
        this.chainID = chainID;
    }

    public Long getSubChainID() {
        return subChainID;
    }

    public void setSubChainID(Long subChainID) {
        this.subChainID = subChainID;
    }

    public Long getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Long getStoreZipCode() {
        return storeZipCode;
    }

    public void setStoreZipCode(Long storeZipCode) {
        this.storeZipCode = storeZipCode;
    }
}
