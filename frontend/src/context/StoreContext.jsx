import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
} from "react";
import { getAllStores } from "../api/storeAPI";
import { useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { useCart } from "../context/CartContext";
import { sendComparisonRequest } from "../api/comparisonAPI";

const StoreContext = createContext();
/**
 * Provides store-related state and actions to the application.
 *
 * - Fetches and paginates store list.
 * - Manages selected stores for comparison (max 4).
 * - Sends comparison request and stores results.
 * - Persists comparison results in sessionStorage across reloads.
 */
export const StoreProvider = ({ children }) => {
  const navigate = useNavigate();
  const { currentUser } = useUser();
  const { activeCartId , fetchActiveCartItems } = useCart();
  const PAGE_SIZE = 30;
  const [stores, setStores] = useState([]);
  const [selectedStores, setSelectedStores] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  // Initialize comparison results from sessionStorage
  const [comparisonResults, setComparisonResults] = useState(() => {
    const saved = sessionStorage.getItem("comparisonResults");
    return saved ? JSON.parse(saved) : [];
  });

  // Persist comparison results to sessionStorage on change
  useEffect(() => {
    if (comparisonResults.length > 0) {
      sessionStorage.setItem("comparisonResults", JSON.stringify(comparisonResults));
    }
  }, [comparisonResults]);

  // Clear comparison results when navigating away (but not on reload)
  useEffect(() => {
    const handleVisibilityChange = () => {
      const isReload = performance.navigation.type === 1;
      const isHidden = document.visibilityState === "hidden";
  
      if (isHidden && !isReload) {
        sessionStorage.removeItem("comparisonResults");
      }
    };
  
    document.addEventListener("visibilitychange", handleVisibilityChange);
    return () => {
      document.removeEventListener("visibilitychange", handleVisibilityChange);
    };
  }, []);

   /**
   * Fetches the next page of stores if available.
   */
  const fetchStores = useCallback(async () => {
    if (loading || !hasMore) return;

    setLoading(true);
    try {
      const data = await getAllStores(page, PAGE_SIZE);
      if (data.length === 0) {
        setHasMore(false);
      } else {
        setStores((prev) => [...prev, ...data]);
        setPage((prev) => prev + 1);
      }
    } catch (err) {
      console.error("שגיאה בטעינת הסניפים:", err);
    } finally {
      setLoading(false);
    }
  }, [page, hasMore, loading]);

  useEffect(() => {
    fetchStores();
  }, []);

  // Infinite scroll listener
  useEffect(() => {
    const onScroll = () => {
      const nearBottom =
        window.innerHeight + window.scrollY >=
        document.documentElement.scrollHeight - 200;
      if (nearBottom) fetchStores();
    };
    window.addEventListener("scroll", onScroll);
    return () => window.removeEventListener("scroll", onScroll);
  }, [fetchStores]);
 /**
   * Sends a comparison request for the selected stores.
   * Navigates to results page on success.
   */
  const compareSelectedStores = async () => {
    console.log("User:", currentUser);
    console.log("Cart:", activeCartId);
    console.log("Selected:", selectedStores);

    if (!activeCartId) {
      await fetchActiveCartItems();
    }

    if (!currentUser || !activeCartId || selectedStores.length === 0) {
      console.warn("Missing required info for comparison.");
      return;
    }
    try {
      const response = await sendComparisonRequest({
        userId: currentUser.id,
        cartId: activeCartId,
        storeIds: selectedStores,
      });
      setComparisonResults(response); 
      navigate("/compare-results");
      console.log("Comparison successful:", response);
    } catch (error) {
      console.error("Comparison failed:", error);
    }
  };
/**
   * Toggles selection of a store. Caps selection at 4 stores.
   *
   * @param {string} storeId
   */
  const toggleStore = (storeId) =>
    setSelectedStores((prev) =>
      prev.includes(storeId)
        ? prev.filter((id) => id !== storeId)
        : prev.length < 4
        ? [...prev, storeId]
        : prev
    );

  return (
    <StoreContext.Provider
      value={{
        currentUser,
        activeCartId,
        stores,
        selectedStores,
        toggleStore,
        loading,
        hasMore,
        comparisonResults,
        setComparisonResults,
        compareSelectedStores,
      }}
    >
      {children}
    </StoreContext.Provider>
  );
};
/**
 * Custom hook to access store context.
 *
 * @returns {object} Store context value.
 */
export const useStore = () => useContext(StoreContext);