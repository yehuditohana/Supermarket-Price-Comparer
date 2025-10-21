import axios from 'axios';

const BASE_URL = "http://localhost:8080/api/categories";

// Cache categories in memory to prevent redundant API calls.
let cachedCategories = null;

/**
 * Retrieve the list of categories.
 * First checks in-memory cache, then localStorage, 
 * finally falls back to fetching from the backend API.
 * Caches the result for subsequent calls.
 *
 * @returns {Promise<Array>} List of category objects
 */
export const fetchCategories = async () => {
  // 1. Return from in-memory cache if available
  if (cachedCategories) {
    return cachedCategories;
  }

  // 2. Try to load from localStorage
  const local = localStorage.getItem("categories");
  if (local) {
    cachedCategories = JSON.parse(local);
    return cachedCategories;
  }

  // 3. Fetch from backend API if not cached
  try {
    const response = await axios.get(BASE_URL);
    cachedCategories = response.data;
    localStorage.setItem("categories", JSON.stringify(response.data));
    return response.data;
  } catch (error) {
    console.error(' Failed to fetch categories:', error);
    throw error;
  }
};

/**
 * Force-refresh categories from API and update cache/localStorage.
 * @returns {Promise<Array>} Updated category list
 */
export const refreshCategories = async () => {
  try {
    const response = await axios.get(BASE_URL);
    cachedCategories = response.data;
    localStorage.setItem("categories", JSON.stringify(response.data));
    return response.data;
  } catch (error) {
    console.error(' Failed to refresh categories:', error);
    throw error;
  }
};