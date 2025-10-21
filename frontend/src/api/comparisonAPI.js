import axios from "axios";

const BASE_URL = "http://localhost:8080/api/itemPrice";
/**
 * Send a comparison request to the backend.
 * 
 * @param {Object} payload   Contains comparison parameters
 *                           (e.g. selected stores, items).
 * @returns {Promise<Object>} Returns the comparison results.
 */
export const sendComparisonRequest = async (payload) => {
  try {
    const response = await axios.post(`${BASE_URL}/compare`, payload);
    return response.data;
  } catch (error) {
    console.error(" Error during store comparison:", error);
    throw error;
  }
};
/**
 * Fetch alternative items for a given store and item.
 * 
 * @param {string|number} storeId  ID of the store to search in.
 * @param {string|number} itemId   ID of the item to find alternatives for.
 * @returns {Promise<Array>}       List of alternative items.
 */
export const fetchItemAlternatives = async (storeId, itemId) => {
  try {
    const response = await axios.get("http://localhost:8080/api/item/alternatives", {
      params: { storeId, itemId },
    });
    return response.data;
  } catch (error) {
    console.error(" Error fetching item alternatives:", error);
    throw error;
  }
};