import axios from "axios";

const BASE_URL = "http://localhost:8080/api/stores";

/**
 * Retrieve all stores with pagination support.
 *
 * @param {number}  Page number (zero-based).
 * @param {number}  Number of stores per page.
 * @returns {Promise<Object>} Resolves with the paginated list of stores.
 */

// Retrieve all stores
export const getAllStores = async (page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/all`, {
    params: { page, size }
  });
  return response.data;
};

// Retrieve stores by city
export const getStoresByCity = async (city, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-city`, {
    params: { city, page, size }
  });
  return response.data;
};

// Retrieve stores by chain
export const getStoresByChain = async (chainName, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-chain`, {
    params: { chainName, page, size }
  });
  return response.data;
};

// Retrieve stores in a specific city and chain
export const getStoresByCityAndChain = async (city, chainName, page = 0, size = 30) => {
  const response = await axios.get(`${BASE_URL}/by-city-and-chain`, {
    params: { city, chainName, page, size },
  });
  return response.data;
};