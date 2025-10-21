const BASE_URL = "http://localhost:8080/api/cart-items";
/**
 * Fetch active cart items for the specified user.
 *
 * @param {string|number} userId  Identifier of the user.
 * @returns {Promise<Array>}      Resolves with an array of active cart items.
 * @throws {Error}                If fetching the active cart items fails.
 */
export const getActiveCartItems = async (userId) => {
  const response = await fetch(`${BASE_URL}/user/${userId}`);
  if (!response.ok) throw new Error("Failed to fetch active cart items");
  return await response.json();
};

/**
 * Fetch archived cart items for the specified cart.
 *
 * @param {string|number} cartId  Identifier of the archived cart.
 * @returns {Promise<Array>}      Resolves with an array of archived cart items.
 * @throws {Error}                If fetching the archived cart items fails.
 */
export const getArchivedCartItems = async (cartId) => {
  const response = await fetch(`${BASE_URL}/cart/${cartId}`);
  if (!response.ok) throw new Error("Failed to fetch active cart items");
  return await response.json();
};
/**
 * Add an item to the user’s active cart.
 *
 * @param {string|number} cartId   Identifier of the cart.
 * @param {string|number} itemId   Identifier of the item to add.
 * @param {number}        quantity Quantity of the item to add.
 * @returns {Promise<void>}        Resolves when the item has been added.
 * @throws {Error}                 If adding the item to the cart fails.
 */

export const addItemToCart = async (cartId, itemId, quantity) => {
  const url = `${BASE_URL}/${cartId}/items/${itemId}?quantity=${quantity}`;
  const response = await fetch(url, { method: "POST" });
  if (!response.ok) throw new Error("Failed to add item to cart");
  return ;
};
/**
 * Remove an item from the user’s active cart.
 *
 * @param {string|number} cartId   Identifier of the cart.
 * @param {string|number} itemId   Identifier of the item to remove.
 * @param {number}        quantity Quantity of the item to remove.
 * @returns {Promise<void>}        Resolves when the item has been removed.
 * @throws {Error}                 If removing the item from the cart fails.
 */
export const removeItemFromCart = async (cartId, itemId, quantity) => {
  const url = `${BASE_URL}/${cartId}/items/${itemId}?quantity=${quantity}`;
  const response = await fetch(url, { method: "DELETE" });
  if (!response.ok) throw new Error("Failed to remove item from cart");
};