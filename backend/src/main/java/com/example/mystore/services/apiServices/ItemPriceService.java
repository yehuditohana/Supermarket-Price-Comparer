package com.example.mystore.services.apiServices;

import com.example.mystore.database.entities.*;
import com.example.mystore.database.repositories.*;
import com.example.mystore.dto.api.request.ComparisonRequestDTO;
import com.example.mystore.dto.api.response.ComparisonResultDTO;
import com.example.mystore.dto.api.response.ItemWithPriceDTO;
import com.example.mystore.dto.api.response.StoreDTO;
import com.example.mystore.services.CartStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemPriceService {

    private final ItemPriceRepository itemPriceRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemService itemService;

    public ItemPriceService(ItemPriceRepository itemPriceRepository, UserRepository userRepository, StoreRepository storeRepository, ShoppingCartRepository shoppingCartRepository, ItemService itemService) {
        this.itemPriceRepository = itemPriceRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.itemService = itemService;
    }
    /**
     * Saves a single ItemPrice entity to the database.
     *
     * @param itemPrice the ItemPrice entity to save
     * @return the saved ItemPrice entity
     */

    public ItemPrice save(ItemPrice itemPrice) {
        return itemPriceRepository.save(itemPrice);
    }

    /**
     * Saves a list of ItemPrice entities to the database.
     *
     * @param itemPrices the list of ItemPrice entities to save
     */
    public void saveAll(List<ItemPrice> itemPrices) {
        itemPriceRepository.saveAll(itemPrices);
    }

    /**
     * Finds an ItemPrice by its composite key.
     *
     * @param itemPriceKey the composite key (item_id + store_id)
     * @return an Optional containing the found ItemPrice, or empty if not found
     */
    public Optional<ItemPrice> findById(ItemPriceKey itemPriceKey) {
        return itemPriceRepository.findById(itemPriceKey);
    }

    /**
     * Compares the total price of the user's cart across multiple selected stores.
     *
     * @param requestDTO the comparison request containing userId and storeIds
     * @return a list of ComparisonResultDTO with price details per store
     */
    @Transactional
    public List<ComparisonResultDTO> compareCartAcrossStores(ComparisonRequestDTO requestDTO){
        List<ComparisonResultDTO> comparisonResults  = new ArrayList<>();

        Optional<User> optionalUser = userRepository.findById(requestDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser_UserIDAndStatus(optionalUser.get().getUserID(), CartStatus.ACTIVE);
        if(optionalShoppingCart.isEmpty()){
            throw new IllegalArgumentException("No active cart found for user.");
        }
        List<Store> stores = storeRepository.findAllById(requestDTO.getStoreIds());

        if(stores.isEmpty()){
            throw new IllegalArgumentException("Price comparison is not possible.");
        }

        ShoppingCart shoppingCart = optionalShoppingCart.get();
        List<CartItem> cartItems = shoppingCart.getCartItems();

        for(Store store : stores){
            Long storeId = store.getStoreID();
            List<ItemWithPriceDTO> itemWithPriceDTOS = new ArrayList<>();
            double totalPrice = 0.0;

            for(CartItem cartItem : cartItems){
                Item item = cartItem.getItem();
                ItemPriceKey itemPriceKey = new ItemPriceKey(item.getItemID(), storeId);
                Optional<ItemPrice> optionalItemPrice = itemPriceRepository.findByItemPriceKey(itemPriceKey);


                if(optionalItemPrice.isPresent()) {
                    ItemPrice itemPrice = optionalItemPrice.get();
                    // Calculate the total price based on item price and quantity
                    double price = itemPrice.getPrice() * cartItem.getQuantity();
                    totalPrice += price;
                    itemWithPriceDTOS.add(new ItemWithPriceDTO(
                            item.getItemID(),
                            item.getItemName(),
                            item.getImageUrl(),
                            itemPrice.getPrice(),
                            cartItem.getQuantity(),
                            true));
                } else {

                    itemWithPriceDTOS.add(new ItemWithPriceDTO(
                            item.getItemID(),
                            item.getItemName(),
                            item.getImageUrl(),
                            null,
                            cartItem.getQuantity(),
                            false));
                }
            }

            StoreDTO storeDTO = new StoreDTO(
                    store.getStoreID(),
                    store.getChain().getChainName(),
                    store.getStoreName(),
                    store.getStoreNumber(),
                    store.getStoreCity(),
                    store.getStoreAddress(),
                    null);

            comparisonResults.add(new ComparisonResultDTO(storeDTO, itemWithPriceDTOS, totalPrice));
        }

        return comparisonResults;
    }






}