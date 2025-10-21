import React, { useState, useEffect } from "react";
import { useCart } from "../context/CartContext";
import { FiX } from "react-icons/fi";

const ItemModal = ({ item, onClose }) => {
    // Access the cart context to read current cart and add items
  const { cart, addToCart } = useCart();
    // Track whether we are currently adding to cart
  const [isAdding, setIsAdding] = useState(false);

  // Find if this item is already in the cart
  const existingItem = cart.find((i) => i.id === item.id);
    // Initialize quantity in modal based on existing cart quantity or default to 1
  const [quantity, setQuantity] = useState(existingItem?.quantity || 1);

  console.log(" item in modal:", item);
    // Whenever the item or its presence in cart changes, reset the quantity field
  useEffect(() => {
    setQuantity(existingItem?.quantity || 1);
  }, [existingItem, item]);
  const closeModal = () => {
    if (onClose) {
      onClose();
    }
  };
  // Determine price bounds, falling back to a single price if needed
  const minPrice = item.lowestPrice ?? item.price ?? 0;
  const maxPrice = item.highestPrice ?? item.price ?? 0;
  return (
    <div dir="rtl" className="fixed inset-0 bg-black/40 backdrop-blur-sm z-50 flex justify-center items-center p-4 text-right">
      <div className="bg-white w-full max-w-xl rounded-2xl shadow-lg p-6 relative animate-fade-in">
                {/* Close button in top-left corner */}
        <button
          onClick={closeModal}
          className="absolute top-4 left-4 text-gray-500 hover:text-red-500 text-xl"
        >
          <FiX />
        </button>

        {/* Image  */}
        <div className="w-full h-40 bg-gray-100 rounded-md mb-4 flex items-center justify-center overflow-hidden">
          {item.imageUrl ? (
            <img src={item.imageUrl} alt={item.name} className="object-contain h-full" />
          ) : (
            <span className="text-gray-400">אין תמונה</span>
          )}
        </div>

       {/* Product name / title */}
        <h2 className="text-3xl font-bold text-blue-700 mb-2 text-center">
          {item.name || item.itemName}
        </h2>
        {/* Price range*/}
        {minPrice === maxPrice ? (
          <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
            <span className="text-gray-600">מחיר:</span>
            <span className="text-gray-800 font-semibold">₪{minPrice.toFixed(2)}</span>
          </div>
        ) : (
          <>
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">המחיר הנמוך ביותר:</span>
              <span className="text-gray-800 font-semibold">₪{minPrice.toFixed(2)}</span>
            </div>
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">המחיר הגבוה ביותר:</span>
              <span className="text-gray-800 font-semibold">₪{maxPrice.toFixed(2)}</span>
            </div>
          </>
        )}

        {/* Additional details list */}
        {item.itemId && (
          <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
            <span className="text-gray-600">ברקוד:</span>
            <span className="text-gray-800 font-semibold">{item.itemId}</span>
          </div>
        )}
        <div className="mb-6 space-y-2">
          {(item.unitQty || item.quantity != null) && (
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">כמות באריזה:</span>
              <span className="text-gray-800 font-semibold">
                {item.quantity && item.unitQty
                  ? `${item.quantity}  ${item.unitQty}`
                  : item.unitQty || item.quantity}
              </span>
            </div>
          )}

          {/* Manufacturer name, if known */}
          {item.manufacturerName && item.manufacturerName !== "לא ידוע" && (
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">יצרן:</span>
              <span className="text-gray-800 font-semibold">{item.manufacturerName}</span>
            </div>
          )}

          {item.manufactureCountry === "ישראל" && (
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">ארץ ייצור:</span>
              <span className="text-gray-800 font-semibold">ישראל</span>
            </div>
          )}
  {/* Show if item is sold by weight */}
          {item.weighted != null && (
            <div className="flex justify-between px-4 py-2 bg-gray-50 border border-gray-200 rounded-md">
              <span className="text-gray-600">נמכר לפי משקל:</span>
              <span className="text-gray-800 font-semibold">{item.weighted ? "כן" : "לא"}</span>
            </div>
          )}
        </div>

      </div>
    </div>
  );
};
export default ItemModal;