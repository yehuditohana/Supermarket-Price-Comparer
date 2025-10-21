package com.example.mystore.services.seederServices;

import com.example.mystore.database.entities.Chain;
import com.example.mystore.database.entities.ChainKey;
import com.example.mystore.database.entities.Store;
import com.example.mystore.database.repositories.ChainRepository;
import com.example.mystore.database.repositories.StoreRepository;
import com.example.mystore.dto.xml.StoreXmlDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//This class is designed to provide services for initializing the store table.
/**
 * Service for initializing and seeding the Store table.
 */
@Service
public class StoreSeederService {

    private final StoreRepository storeRepository;
    private final ChainRepository chainRepository;

    public StoreSeederService(StoreRepository storeRepository, ChainRepository chainRepository) {
        this.storeRepository = storeRepository;
        this.chainRepository = chainRepository;
    }

/*This function converts StoreXmlDTO objects into Store entities
 and saves them to the table — if they are valid.
It returns the number of records that were successfully saved.*/
    /**
     * Converts a list of StoreXmlDTOs into Store entities and saves them
     * only if their associated chain exists.
     *
     * @param dtos the list of StoreXmlDTO objects
     * @return the number of successfully saved stores
     */
    public int saveAllIfChainExists(List<StoreXmlDTO> dtos) {
        int saved = 0;
        for (StoreXmlDTO dto : dtos) {
            if (saveIfChainExists(dto)){
                saved++;
            }
        }
        return saved;
    }
/* This function performs the following checks:
Verifies that the foreign key (chain_id, sub_chain_id) exists in the chain table.
Validates that the combination (chain_id, sub_chain_id, store_number) is valid.
If both checks pass, it adds a new record to the store table.*/
    /**
     * Saves a single Store entity if its chain exists and the store does not already exist.
     *
     * @param dto the StoreXmlDTO object
     * @return true if the store was saved, false otherwise
     */
    private boolean saveIfChainExists(StoreXmlDTO dto) {
        ChainKey chainKey = new ChainKey(dto.getChainID() , dto.getSubChainID());
        Optional<Chain> optionalChain = chainRepository.findById(chainKey);
        if(optionalChain.isEmpty()){
            return false;
        }
        Chain chain = optionalChain.get();

        if(!storeExists(chain.getChainKey().getChainID(), chain.getChainKey().getSubChainID(), dto.getStoreNumber())){
            Store store = new Store(chain, dto.getStoreNumber() , dto.getStoreName() , dto.getStoreCity() , dto.getStoreAddress(), dto.getStoreZipCode());
            storeRepository.save(store);
            return true;
        }
        return false;
    }

/*This function returns true if a triplet of
  (chain_id, sub_chain_id, store_number) — which must be unique —
 already exists in the store table.*/
    /**
     * Checks if a store with the given (chain_id, sub_chain_id, store_number) combination already exists.
     *
     * @param chainId the chain ID
     * @param subChainId the sub-chain ID
     * @param storeNumber the store number
     * @return true if the store exists, false otherwise
     */
    public boolean storeExists(Long chainId , Long subChainId  , Long storeNumber) {
        return storeRepository.existsByChainChainKeyChainIDAndChainChainKeySubChainIDAndStoreNumber(
                chainId,
                subChainId,
                storeNumber
        );
    }

    /**
     * Finds a store by its (chain_id, sub_chain_id, store_number) combination.
     *
     * @param chainId the chain ID
     * @param subChainId the sub-chain ID
     * @param storeNumber the store number
     * @return an Optional containing the Store if found
     */
    public Optional<Store> findStoreId(Long chainId,Long subChainId,Long storeNumber)
    {
        return storeRepository.findByChainChainKeyChainIDAndChainChainKeySubChainIDAndStoreNumber(chainId, subChainId, storeNumber);
    }
    /**
     * Retrieves all stores from the database.
     *
     * @return a list of Store entities
     */
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
    /**
     * Saves a list of stores to the database.
     *
     * @param stores the list of Store entities
     * @return the list of saved Store entities
     */
    public List<Store> saveAllStores(List<Store> stores) {
        return storeRepository.saveAll(stores);
    }


}
