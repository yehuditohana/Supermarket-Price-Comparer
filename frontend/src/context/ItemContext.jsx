import React, { createContext, useContext, useState, useEffect } from "react";
import {
  getItemsByGeneralCategory,
} from "../api/itemAPI";
import { fetchCategories as fetchCategoriesAPI } from "../api/categoryAPI";

const ItemContext = createContext();

/**
 * ItemProvider manages the state and data operations related to items and categories.
 */
export const ItemProvider = ({ children }) => {
  const [items, setItems] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Load categories on mount
  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const result = await fetchCategoriesAPI();
      setCategories(result);
    } catch (err) {
      setError("Failed to load categories");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Fetch items by general and sub category.
   */
  const fetchItemsByCategory = async (generalCategory, subCategory) => {
    try {
      setLoading(true);
      const results = await getItemsByCategory(generalCategory, subCategory);
      setItems(results);
      return results;
    } catch (err) {
      setError("Failed to fetch items by category");
      console.error(err);
      return [];
    } finally {
      setLoading(false);
    }
  };

  /**
   * Fetch all items under a general category (across all its sub and specific categories).
   */

  const fetchItemsByGeneralOnly = async (generalCategory, page = 0, size = 30) => {
    try {
      setLoading(true);
      const results = await getItemsByGeneralCategory(generalCategory, page, size);
      setItems(results);
      return results;
    } catch (err) {
      setError("Failed to fetch items by general category");
      console.error(err);
      return [];
    } finally {
      setLoading(false);
    }
  };

  

  /**
   * Search items by name or barcode.
   */
  const searchItems = async (query) => {
    try {
      setLoading(true);
      const results = await searchItemsByName(query);
      setItems(results);
      return results;
    } catch (err) {
      setError("Search error");
      console.error(err);
      return [];
    } finally {
      setLoading(false);
    }
  };

  return (
    <ItemContext.Provider
      value={{
        items,
        categories,
        loading,
        error,
        fetchItemsByCategory,
        fetchItemsByGeneralOnly,
        searchItems,
      }}
    >
      {children}
    </ItemContext.Provider>
  );
};

/**
 * Custom hook to access item context.
 */
export const useItems = () => useContext(ItemContext);
