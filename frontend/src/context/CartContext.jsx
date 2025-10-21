import React, { createContext, useState, useContext, useEffect } from "react";
import { useUser } from "./UserContext";
import {
  deleteCart,
  activateCart,
  archiveCart,
  getCartHistory,
  getActiveCartId,
} from "../api/cartAPI";
import {
  addItemToCart,
  removeItemFromCart,
  getActiveCartItems,
  getArchivedCartItems
} from "../api/cartItemsAPI";


const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const {currentUser} = useUser();
  const [cartItems, setCartItems] = useState([]);
  const [activeCartId, setActiveCartId] = useState(null);
  const [activeCartItems, setActiveCartItems] = useState([]);
  const [savedCarts, setSavedCarts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (currentUser) {
      fetchCartHistory();
      fetchActiveCartItems();

    }
  }, [currentUser]);

  //This function returns the active cart number. 
  // If the local variable is not initialized, it sends an API request to get the active cart number.
  //Note: The getActiveCartId function always returns the active cart - if it doesn't exist, it creates a new one
  const getOrFetchActiveCartId = async () => {
    if (activeCartId) return activeCartId;

    try {
      const cartId = await getActiveCartId(currentUser.userId);
      setActiveCartId(cartId);
      return cartId;
    } catch (err) {
      console.error(" Error retrieving id of active cart", err);
      setError("Error retrieving active cart");
      return null;
    }
  };
  const refreshActiveCart = async () => {
    await cleanActiveCart();
    await fetchActiveCartItems();
  };

  //This function reset activeCartId varible - that save the number of active cart. 
  const cleanActiveCart = async () => {
    setActiveCartId(null);
    setCartItems([]);
    setActiveCartItems([])
  };


  //This function returns the cart items of cart_id (archived cart)
  const loadArchivedCartItems = async (cartId) => {
    const items = await getArchivedCartItems(cartId);
    return items;
  };

  //This function returns the items in the active cart.
  const fetchActiveCartItems = async () => {
    try {
      setLoading(true);
      let cartId = await getOrFetchActiveCartId(currentUser.userId); //get active cart by user id
      const items = await getActiveCartItems(currentUser.userId);//Getting existing items in an active cart - by api request
      //Sorting - to maintain a consistent order when adding and removing items from the cart
      const sortedItems = items.sort((a, b) => a.itemId.localeCompare(b.itemId));
      //Update the cartItems state with the list of items returned from the backend.
      setCartItems(sortedItems);
      setActiveCartItems(sortedItems); 
    } catch (err) {
      console.error("Error loading active cart", err);
      setError("Error loading active cart");
    } finally {
      setLoading(false);
    }
  };
  //Fetches the list of archived (saved) carts for the current user.
  const fetchCartHistory = async () => {
    try {
      setLoading(true);
      const carts = await getCartHistory(currentUser.id); //API request for getting user's saved carts
      setSavedCarts(carts);
    } catch (err) {
      setError("Error loading cart history");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  //Adding items to cart
  //If there is no active cart, a new one will be created
  const addToCart = async (itemId, quantity) => {
    try {
      let cartId = await getOrFetchActiveCartId(currentUser.userId); //get active cart by user id - if exists - else create new.
      await addItemToCart(cartId, itemId, quantity); // add items to the active cart
      await fetchActiveCartItems();
      console.log("Add item to cart");
    } catch (err) {
      setError("Error adding item to cart");
      console.error("Error adding item to cart", err);
    }
  };

  //Removing items from active cart
  //Note : Active cart definitely exists - because removing items is only done through the cart page.
  const removeFromCart = async (itemId, quantity) => {
    let cartId = await getOrFetchActiveCartId(currentUser.userId); //get active cart by user id - if exists - else create new.
    try {
      await removeItemFromCart(cartId, itemId, quantity); //API request to remove an item to the cart in the DB
      await fetchActiveCartItems();
    } catch (err) {
      setError("Error removing item from cart");
    }
  };


  //This function delete an existing active cart (and delete all items in it).
  const clearCart = async () => {
    const cartId = await getOrFetchActiveCartId(currentUser.userId);
    if (!cartId) return; // If there isn't active cart - do nothing.
    try {
      await deleteCart(cartId);
      await refreshActiveCart();
    } catch (err) {
      setError("error clearing cart");
    }
  };

  //This function delete an existing archived cart (and delete all items in it). 
  const deleteCartFromArchived = async (cartId) => {
    try {
      await deleteCart(cartId);
      await fetchCartHistory();
    } catch (err) {
      setError("Error deleting cart from archive");
    }
  };

  //This function load an existing archived cart - to active cart (If there is an active cart - it is deleted).
  const loadArchivedCart = async (cartId) => {
    try {
      await activateCart(cartId); // API request to change cart status from archived to active
      await refreshActiveCart();
    } catch (err) {
      setError("Error loading saved cart");
    }
  };

  //Moving an active cart to the archive
  const archiveActiveCart = async (cartName) => {
    const cartId = await getOrFetchActiveCartId(currentUser.userId);
    if (!cartId) return;

    try {
      if (!cartName || cartName.trim() === "") {
        // const now = new Date();
        // const formattedDate = now.toLocaleDateString("he-IL");
        cartName = `עגלה ${Date.now()}`;//Default name for cart in archive
       
      }
      await archiveCart(cartId, cartName);//API request to change cart status from active to archived.
      await refreshActiveCart();
    } catch (err) {
      console.error("error loading archived cart", err);
      setError("error loading archived cart");
    }
  };

  return (
    <CartContext.Provider
      value={{
        cart: cartItems,
        activeCartItems,
        fetchActiveCartItems,
        savedCarts,
        activeCartId,
        archiveActiveCart,
        loading,
        error,
        addToCart,
        removeFromCart,
        clearCart,
        loadArchivedCartItems,
        loadArchivedCart,
        deleteCartFromArchived,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);