package com.example.mystore.services.seederServices;

import com.example.mystore.database.entities.Chain;
import com.example.mystore.database.entities.ChainKey;
import com.example.mystore.database.repositories.ChainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChainSeederService {

    private final ChainRepository chainRepository;

    public ChainSeederService(ChainRepository chainRepository) {
        this.chainRepository = chainRepository;
    }

    /**
     * Saves a list of Chain entities to the database.
     *
     * @param chains the list of Chain entities to save
     */
    public void saveAll(List<Chain> chains) {
        chainRepository.saveAll(chains);
    }
    // Save a chain
    /**
     * Saves a single Chain entity to the database.
     *
     * @param chain the Chain entity to save
     * @return the saved Chain entity
     */
    public Chain save(Chain chain) {
        return chainRepository.save(chain);
    }

    // Find a chain by its key
    /**
     * Finds an existing Chain entity by its composite key.
     *
     * @param chainKey the composite key (chainID and subChainID)
     * @return an Optional containing the found Chain, or empty if not found
     */
    public Optional<Chain> findExistingChain(ChainKey chainKey) {
        return chainRepository.findByChainKey(chainKey);
    }

}