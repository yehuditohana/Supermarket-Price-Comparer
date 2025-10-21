package com.example.mystore.services.apiServices;

import com.example.mystore.database.entities.Store;
import com.example.mystore.database.repositories.StoreRepository;
import com.example.mystore.dto.api.response.StoreDTO;
import com.example.mystore.services.seederServices.ChainSeederService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    @Value("${chain.image.url.shufersal}")
    private String shufersalImage;

    @Value("${chain.image.url.rami-levi}")
    private String ramiLevyImage;

    @Value("${chain.image.url.victory}")
    private String victoryImage;

    // @Value("${}")
    // private String mahshaneiImage;
    private static final long SHUFERSAL_CHAIN_ID = 7290027600007L;
    private static final long RAMI_LEVI_CHAIN_ID = 7290058140886L;
    private static final long VICTORY_CHAIN_ID = 7290696200003L;
    private static final long MEGA_CHAIN_ID = 7290055700007L;
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository, ChainSeederService chainService) {
        this.storeRepository = storeRepository;
    }

    /**
     * Converts a Store entity to a StoreDTO including image URL based on the chain ID.
     *
     * @param store the Store entity
     * @return the corresponding StoreDTO
     */

    //Returns a list of storeDTO objects for display on the site
    /*public List<StoreDTO> getAllStoreDTOs() {
        List<Store> stores = storeRepository.findAll();

        return stores.stream()
                .filter(store -> {
                    Chain chain = store.getChain();
                    return chain != null && chain.getChainKey().getChainID() != MEGA_CHAIN_ID;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }*/

    private StoreDTO convertToDTO(Store store) {
        String imageUrl = resolveImageUrl(store.getChain().getChainKey().getChainID());
        return new StoreDTO(
                store.getStoreID(),
                store.getChain().getChainName(),
                store.getStoreName(),
                store.getStoreNumber(),
                store.getStoreCity(),
                store.getStoreAddress(),
                imageUrl
        );
    }
    /**
     * Resolves the image URL based on the chain ID.
     *
     * @param chainId the chain ID
     * @return the image URL string, or null if not found
     */
    private String resolveImageUrl(Long chainId) {
        if (chainId == null) return null;

        if (chainId.equals(SHUFERSAL_CHAIN_ID)) return shufersalImage;
        if (chainId.equals(RAMI_LEVI_CHAIN_ID)) return ramiLevyImage;
        if (chainId.equals(VICTORY_CHAIN_ID)) return victoryImage;
        return null;
     }


    /**
     * Retrieves all stores excluding stores from the MEGA chain, with pagination.
     *
     * @param page the page number
     * @param size the number of stores per page
     * @return a list of StoreDTOs
     */

    public List<StoreDTO> getAllStores(int page, int size) {
        Page<Store> storePage;
        Pageable pageable = PageRequest.of(page, size);
        storePage = storeRepository.findByChain_ChainKey_ChainIDNot(MEGA_CHAIN_ID, pageable);
        return storePage.stream()
                .map(this::convertToDTO)
                .filter(dto -> dto.getStoreCity() != null && !dto.getStoreCity().trim().isEmpty() &&
                        !dto.getStoreCity().trim().equals("0"))
                .collect(Collectors.toList());
    }
    /**
     * Retrieves stores by city name, excluding MEGA chain stores, with pagination.
     *
     * @param city the city name
     * @param page the page number
     * @param size the number of stores per page
     * @return a list of StoreDTOs matching the city
     */
    public List<StoreDTO> getStoresByCity(String city , int page, int size) {
        Page<Store> storePage;
        Pageable pageable = PageRequest.of(page, size);
        storePage = storeRepository.findByStoreCityContainingAndChain_ChainKey_ChainIDNot(city , MEGA_CHAIN_ID, pageable);

        return storePage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    /**
     * Retrieves stores by chain name, excluding MEGA chain stores, with pagination.
     *
     * @param chainName the chain name
     * @param page the page number
     * @param size the number of stores per page
     * @return a list of StoreDTOs matching the chain name
     */
    public List<StoreDTO> getStoresByChain(String chainName , int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> storePage = storeRepository.findByChain_ChainNameContainingAndChain_ChainKey_ChainIDNot(chainName, MEGA_CHAIN_ID, pageable);

        return storePage.stream()
                .map(this::convertToDTO)
                .filter(dto -> dto.getStoreCity() != null && !dto.getStoreCity().trim().isEmpty() &&
                        !dto.getStoreCity().trim().equals("0"))
                .collect(Collectors.toList());
    }
    /**
     * Retrieves stores by both city and chain name, excluding MEGA chain stores, with pagination.
     *
     * @param city the city name
     * @param chainName the chain name
     * @param page the page number
     * @param size the number of stores per page
     * @return a list of StoreDTOs matching both city and chain name
     */

    public List<StoreDTO> getStoresByCityAndChain(String city, String chainName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> pageResult = storeRepository.findByStoreCityContainingIgnoreCaseAndChain_ChainNameContainingIgnoreCaseAndChain_ChainKey_ChainIDNot(
                city, chainName, MEGA_CHAIN_ID, pageable
        );
        return pageResult.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}