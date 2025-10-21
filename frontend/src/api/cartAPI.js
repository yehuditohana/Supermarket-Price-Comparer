const BASE_URL = "http://localhost:8080/api/shopping-carts";

//maybe delete - I create automatically  activate cart (if not exists one) through getActiveCartId
// export const createCart = async (user) => {
//   const response = await fetch(`${BASE_URL}/new`, {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify(user),

//   });
//   if (!response.ok) throw new Error("Failed to create cart");
//   return await response.json();
// };

/**
 * Retrieve the ID of the user’s active cart.
 *
 * @param {string|number} userId  Identifier of the user.
 * @returns {Promise<string|number>}  Resolves with the active cart’s ID.
 * @throws {Error}                   If the request fails.
 */
export const getActiveCartId = async (userId) => {
  const response = await fetch(`${BASE_URL}/active/${userId}`);
  if (!response.ok) throw new Error("Failed to fetch active cart ID");
  return await response.json();
};
/**
 * Archive a completed cart under the given name.
 *
 * @param {string|number} cartId    Identifier of the cart to archive.
 * @param {string}        cartName  Name to assign to the archived cart (optional).
 * @returns {Promise<Object>}       Resolves with the updated cart object.
 * @throws {Error}                  If the archive operation fails.
 */
export const archiveCart = async (cartId, cartName) => {
  const params = new URLSearchParams({
    cartId: cartId,
    cartName: cartName ?? ""
  });
  const response = await fetch(`${BASE_URL}/archive?${params.toString()}`, {
    method: "PUT",
  });
  if (!response.ok) throw new Error("Failed to archive cart");
  return await response.json();
};
/**
 * Re-activate a previously archived cart.
 *
 * @param {string|number} cartId  Identifier of the cart to activate.
 * @returns {Promise<void>}       Resolves when the cart is active again.
 * @throws {Error}                If activation fails.
 */

export const activateCart = async (cartId) => {
  const response = await fetch(`${BASE_URL}/${cartId}/activate`, {
    method: "PUT",
  });
  if (!response.ok) throw new Error("Failed to activate cart");
};
/**
 * Delete a cart and all its items.
 *
 * @param {string|number} cartId  Identifier of the cart to delete.
 * @returns {Promise<void>}       Resolves when the cart is deleted.
 * @throws {Error}                If deletion fails.
 */
export const deleteCart = async (cartId) => {
  const response = await fetch(`${BASE_URL}/${cartId}`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error("Failed to delete cart");
};
/**
 * Retrieve the user’s past carts (history).
 *
 * @param {string|number} userId  Identifier of the user.
 * @returns {Promise<Array>}      Resolves with an array of archived carts.
 * @throws {Error}                If fetching the history fails.
 */
export const getCartHistory = async (userId) => {
  const response = await fetch(`${BASE_URL}/history/${userId}`);
  if (!response.ok) throw new Error("Failed to fetch cart history");
  return await response.json();
};