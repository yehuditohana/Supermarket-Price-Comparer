import axios from "axios";

const BASE_URL = "http://localhost:8080/api/item";
// Item-related API calls

/**
 * Search for items by name or barcode, with optional pagination.
 *
 * @param {string} query   Text or barcode to search for.
 * @param {number} page    Page number .
 * @param {number} size    Items per page .
 * @returns {Promise<Array>}  Array of matching items.
 */

// Search by name/barcode
export const searchItems = async (query, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/search`, {
    params: { query, page, size }
  });
  // Some endpoints wrap results in `content`
  return response.data.content ?? response.data;
};

//Products by general category
export const getItemsByGeneralCategory = async (category, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-general-category`, {
    params: { category, page, size }
  });
  return response.data;
};

//Products by subcategory 
export const getItemsBySubCategory = async (category, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-sub-category`, {
    params: { category, page, size }
  });
  return response.data;
};

//Products by specific category 
export const getItemsBySpecificCategory = async (category, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-specific-category`, {
    params: { category, page, size }
  });
  return response.data;
};