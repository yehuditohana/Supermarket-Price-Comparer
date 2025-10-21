package com.example.mystore.database.repositories;
import com.example.mystore.database.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // Checks if a store exists with the given chain ID, sub-chain ID, and store number.
    boolean existsByChainChainKeyChainIDAndChainChainKeySubChainIDAndStoreNumber(
            Long chainID,
            Long subChainID,
            Long storeNumber
    );

    // Finds a store by its chain ID, sub-chain ID, and store number.
    Optional<Store> findByChainChainKeyChainIDAndChainChainKeySubChainIDAndStoreNumber(
            Long chainId,
            Long subChainId,
            Long storeNumber
    );

    //returns page of all store - excluding the stores belonging to a specific `excludedChainId`.
    Page<Store> findByChain_ChainKey_ChainIDNot(Long excludedChainId , Pageable pageable);

    // Returns a page of stores based on city (substring match), excluding the stores belonging to `excludedChainId`.
    Page<Store> findByStoreCityContainingAndChain_ChainKey_ChainIDNot(String city, Long excludedChainId ,Pageable pageable);

    // Returns a page of stores based on chain name (substring match), excluding the stores belonging to `excludedChainId`.
    Page<Store> findByChain_ChainNameContainingAndChain_ChainKey_ChainIDNot(String chainName, Long excludedChainId, Pageable pageable);


    // Returns a page of stores based on city or chain name (or both), excluding the stores belonging to `excludedChainId`.
Page<Store> findByStoreCityContainingIgnoreCaseAndChain_ChainNameContainingIgnoreCaseAndChain_ChainKey_ChainIDNot(
        String city,
        String chainName,
        Long excludedChainId,
        Pageable pageable
);
}
