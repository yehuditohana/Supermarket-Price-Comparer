import React, { useState, useEffect } from "react";
import {
  getItemsByGeneralCategory,
  getItemsBySubCategory,
  getItemsBySpecificCategory,
  searchItems
} from "../api/itemAPI";
import ItemCard from "../components/ItemCard";
import HoverTopCategoryBar from "../components/HoverTopCategoryBar";
import { FiSearch } from "react-icons/fi";

const ItemsPage = () => {
  // State for category filtering and search
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [debouncedSearchQuery, setDebouncedSearchQuery] = useState(searchQuery);
  const [filteredItems, setFilteredItems] = useState([]);
  // Pagination states for infinite scroll
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [searching, setSearching] = useState(false);
  
  // Initialize selected category when categories are first loaded
  const handleCategoriesLoaded = (categories) => {
    if (categories.length > 0) {
      setSelectedCategory({ generalCategory: categories[0].generalCategory });
    }
  };


  // Debounce input
  //This effect runs every time `searchQuery` changes.
  //It waits 700 ms - if user has stopped typing - it copy the value into `debouncedSearchQuery`.
  //This prevents sading a search request for every single character the user types.
  useEffect(() => {
    const timeout = setTimeout(() => {
      setDebouncedSearchQuery(searchQuery);
    }, 700);
    return () => clearTimeout(timeout);
  }, [searchQuery]);

  //search text
  //This effect runs whenever debouncedSearchQuery changes.
  useEffect(() => {
    //Asynchronous function - to send a request to the server
    const searchItemsFromServer = async () => {
      if (!debouncedSearchQuery.trim()) return;

      setSearching(true);
      setFilteredItems([]);
      setPage(0);
      setHasMore(true);

      try {
        const results = await searchItems(debouncedSearchQuery, 0);//API request to get search results
        const uniqueItems = Array.from(new Map(results.map(item => [item.id, item])).values());
        setFilteredItems(uniqueItems);
      } catch (err) {
        console.error(" Error during search:", err);
        setFilteredItems([]);
        setHasMore(false);
      } finally {
        setSearching(false);
      }
    };

    searchItemsFromServer(); //Calling the function defined above
  }, [debouncedSearchQuery]);

  //category selection
  useEffect(() => {
    const fetchByCategory = async () => {
      if (debouncedSearchQuery.trim()) return; // Don't run if search is active
      if (!selectedCategory?.generalCategory) return;

      setSearching(true);
      setFilteredItems([]);
      setPage(0);
      setHasMore(true);

      try {
        let results = [];
        if (selectedCategory.specificCategory) {
          console.info("selectedCategory.specificCategory:" + selectedCategory.specificCategory);
          results = await getItemsBySpecificCategory(selectedCategory.specificCategory, 0);
          const uniqueItems = Array.from(new Map(results.map(item => [item.id, item])).values());
          setFilteredItems(uniqueItems);
        } else if (selectedCategory.subCategory) {
          results = await getItemsBySubCategory(selectedCategory.subCategory, 0);
          const uniqueItems = Array.from(new Map(results.map(item => [item.id, item])).values());
          setFilteredItems(uniqueItems);
        } else {
          results = await getItemsByGeneralCategory(selectedCategory.generalCategory, 0);
          const uniqueItems = Array.from(new Map(results.map(item => [item.id, item])).values());
          setFilteredItems(uniqueItems);   
        }
        setFilteredItems(results);
      } catch (err) {
        console.error(" Error applying filters:", err);
        setFilteredItems([]);
        setHasMore(false);
      } finally {
        setSearching(false);
      }
    };

    fetchByCategory();
  }, [selectedCategory, debouncedSearchQuery]);

  //  infinite scroll
  useEffect(() => {
    const handleScroll = async () => {
      const bottom =
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 200;

      // skip if no items loaded yet
      if (!filteredItems.length && page === 0) return;

      if (bottom && hasMore && !searching) {
        setSearching(true);
        try {
          const nextPage = page + 1;
          let newItems = [];

          // fetch next page depending on active mode: search or category
          if (debouncedSearchQuery.trim()) {
            newItems = await searchItems(debouncedSearchQuery, nextPage);
          } else if (selectedCategory?.specificCategory) {
            newItems = await getItemsBySpecificCategory(selectedCategory.specificCategory, nextPage);
          } else if (selectedCategory?.subCategory) {
            newItems = await getItemsBySubCategory(selectedCategory.subCategory, nextPage);
          } else if (selectedCategory?.generalCategory) {
            newItems = await getItemsByGeneralCategory(selectedCategory.generalCategory, nextPage);
          }

          if (newItems.length === 0) {
            setHasMore(false);
          } else {
            setFilteredItems((prev) => {
              const allItems = [...prev, ...newItems];
              const uniqueItems = Array.from(new Map(allItems.map(item => [item.id, item])).values());
              return uniqueItems;
            });
            setPage(nextPage);
          }
        } catch (err) {
          console.error("Error loading more items:", err);
          setHasMore(false);
        } finally {
          setSearching(false);
        }
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [page, hasMore, selectedCategory, searching, debouncedSearchQuery, filteredItems.length]);

  // Handle text input change
  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  return (
    <div dir="rtl" className="px-[5%] text-right">
      <div className="flex-1">
        <div className="flex items-center border-2 border-gray-500 rounded-md px-3 py-2 w-full max-w-md mb-6 mx-1 my-1">
          <FiSearch className="text-gray-400 mr-2" />
          <input
            type="text"
            placeholder="חיפוש לפי שם מוצר או ברקוד"
            value={searchQuery}
            onChange={handleSearchChange}
            className="w-full outline-none bg-transparent text-gray-800 placeholder-gray-400"
          />
        </div>

        {/* Category filter bar */}
        <HoverTopCategoryBar
          onSelectCategory={(categoryObj) => {
            setSelectedCategory(categoryObj);
            setSearchQuery(""); // clear search when selecting category
            window.scrollTo({ top: 0, behavior: "smooth" });
          }}
          onCategoriesLoaded={handleCategoriesLoaded}
        />

        <div className="flex flex-col md:flex-row gap-6">
          {filteredItems.length > 0 ? (
             <div className="flex flex-wrap justify-start gap-6">
              {filteredItems.map((item) => (
                <div
                  key={item.id}
                  className="w-full sm:w-[calc(50%-12px)] md:w-[calc(33%-16px)] lg:w-[calc(25%-18px)]"
                >
                  <ItemCard item={item} />
                </div>
              ))}
            </div>
          ) : (
            !searching && (
              <p className="text-gray-500 italic text-center py-10">
                לא נמצאו פריטים שתואמים את החיפוש
              </p>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default ItemsPage;