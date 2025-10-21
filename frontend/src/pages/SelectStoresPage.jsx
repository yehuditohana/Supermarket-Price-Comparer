import React, { useState, useEffect } from "react";
import StoreCard from "../components/StoreCard";
import { useStore } from "../context/StoreContext";
import {
  getAllStores,
  getStoresByCity,
  getStoresByChain,
  getStoresByCityAndChain
} from "../api/storeAPI";
import { useCart } from "../context/CartContext";

const SelectStoresPage = () => {
  // Local state for search inputs
  const [searchCity, setSearchCity] = useState("");
  const [searchChain, setSearchChain] = useState("");
  // State for stores matching search criteria
  const [searchResults, setSearchResults] = useState([]);
  // State for stores matching search criteria
  const { compareSelectedStores, stores, selectedStores, toggleStore, loading, hasMore } = useStore();
  const { activeCartItems, activeCartId, loading: cartLoading } = useCart();
  const [noResults, setNoResults] = useState(false);
  // Pull active cart items from CartContext
  const isCartInvalid = !activeCartId || !activeCartItems || activeCartItems.length === 0;

  // Deduplicate original store list
  const uniqueStores = Array.from(
    new Map(stores.map((s) => [s.storeId, s])).values()
  );

  // Search handler – fetches filtered stores from API
  const handleSearch = async () => {
    try {
      let results = [];

      if (!searchCity && !searchChain) {
        //results == []
      } else if (!searchCity && searchChain) {
        results = await getStoresByChain(searchChain);
      } else if (searchCity && !searchChain) {
        results = await getStoresByCity(searchCity);
      } else {
        results = await getStoresByCityAndChain(searchCity, searchChain);
      }

      if (results.length === 0) {
        console.error("no stores found");
        setSearchResults([]);
        setNoResults(true)
      }
      else {
        const unique = Array.from(new Map(results.map((s) => [s.storeId, s])).values());
        setSearchResults(unique);
        setNoResults(false);
      }
    } catch (err) {
      console.error("Failed to fetch stores:", err);
    }
  };

  // Renders the store cards
  const renderStores = (
    storesToRender,
    selectedStores,
    toggleStore,
    loading
  ) => {
    if (loading) {
      return <p>Loading stores...</p>;
    }

    if (!loading && storesToRender.length === 0) {
      return <p>No stores to display yet.</p>;
    }

    return storesToRender.map((store) => (
      <StoreCard
        key={store.storeId}
        store={store}
        isSelected={selectedStores.includes(store.storeId)}
        toggle={() => toggleStore(store.storeId)}
        disabled={
          !selectedStores.includes(store.storeId) &&
          selectedStores.length >= 4
        }
      />
    ));
  };

  return (
    <div className="mt-12 bg-white border p-6 rounded-xl shadow">
      {/* Header and compare button */}
      <div className="sticky top-[64px] z-10 bg-white pb-4 mb-6 shadow-sm border-b">

        <div className="flex flex-wrap justify-between items-end gap-4">
          <div>
            <h2
              className="text-2xl font-bold text-blue-800 cursor-pointer hover:underline transition"
              onClick={() => {
                // Clear filters and scroll to top when heading clicked
                setSearchCity("");
                setSearchChain("");
                setSearchResults([]);
                setNoResults(false);
                window.scrollTo({ top: 0, behavior: "smooth" });
              }}
            >
              בחירת סניפים להשוואה 🏪
            </h2>
          </div>
          {/* Compare Prices button: disabled if no stores selected or cart empty */}
          {
            <button
              onClick={() => compareSelectedStores()}
              disabled={selectedStores.length === 0 || isCartInvalid}
              className={`transition-all duration-300 flex items-center gap-2 px-6 py-3 text-lg font-bold rounded-xl shadow-xl border-2
    ${selectedStores.length === 0 || isCartInvalid
                  ? "bg-gray-300 text-gray-500 border-gray-300 cursor-not-allowed"
                  : "bg-green-500 hover:bg-green-500 text-white hover:from-green-600 hover:to-green-800 border-green-600"
                }
  `}
            >
              השוואת מחירים
            </button>
          }
          {/*Error message - if there is no active cart or if the cart is empty*/}
          {isCartInvalid && (
            <div className="w-full mt-2 text-sm text-red-500">
              <p>עליך להוסיף מוצרים לעגלה לפני ביצוע השוואה</p>
            </div>
          )}
        </div>

        {/* Search inputs */}
        <div className="flex flex-wrap gap-4 mt-4">
          <div className="flex flex-col w-full md:w-1/3">
            <label className="text-sm font-medium text-gray-700 mb-1">
              חיפוש לפי עיר
            </label>
            <input
              placeholder="לדוגמה: תל אביב"
              value={searchCity}
              onChange={(e) => setSearchCity(e.target.value)}
              className="border p-2 rounded"
            />
          </div>

          <div className="flex flex-col w-full md:w-1/3">
            <label className="text-sm font-medium text-gray-700 mb-1">
              חיפוש לפי רשת
            </label>
            <input
              placeholder="לדוגמה: שופרסל"
              value={searchChain}
              onChange={(e) => setSearchChain(e.target.value)}
              className="border p-2 rounded"
            />
          </div>

          <div className="flex items-end">
            <button
              onClick={handleSearch}
              className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow"
            >
              🔍 חיפוש
            </button>
          </div>
        </div>
      </div>

      {/* Store list */}
      <div className="flex flex-wrap gap-4 mt-4">
        {noResults ? (
          <p className="text-center text-gray-600">לא נמצאו פריטים שתואמים את החיפוש.</p>  // הודעה כשאין תוצאות
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
            {renderStores(
              searchResults.length > 0 ? searchResults : uniqueStores,
              selectedStores,
              toggleStore,
              loading
            )}
          </div>
        )}
      </div>
      {/* Load status */}
      {loading && <p className="text-center mt-4">טוען…</p>}
      {!hasMore && <p className="text-center mt-4">אין סניפים נוספים.</p>}
    </div>
  );
};

export default SelectStoresPage;