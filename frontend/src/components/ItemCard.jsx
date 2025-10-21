import React, { useState } from "react";
import { useCart } from "../context/CartContext";
import { useUser } from "../context/UserContext";
import ItemModal from "./ItemModal";

const ItemCard = ({ item }) => {
  const { addToCart } = useCart();
  const [quantity, setQuantity] = useState(1);
  const [isAdding, setIsAdding] = useState(false);
  const [showModal, setShowModal] = useState(false);
   // Increase quantity by 1, up to a max of 99
  const increaseQty = () => setQuantity((q) => Math.min(q + 1, 99));
    // Decrease quantity by 1, down to a min of 1

  const decreaseQty = () => setQuantity((q) => Math.max(q - 1, 1));
  const { currentUser } = useUser();
  
  const isLoggedIn = !!currentUser;
// Handle adding item to cart via context
  const handleAddToCart = async () => {
    setIsAdding(true);
    try {
      await addToCart(item.id, quantity);
      setQuantity(1);
    } catch (error) {
      console.error(" Failed to add item to cart:", error);
    } finally {
      setIsAdding(false);
    }
  };
 //if item data is missing, show placeholder
  if (!item || !item.name) {
    return (
      <div className="bg-white rounded-2xl shadow-md p-6 border border-gray-100 text-right">
        <p className="text-gray-500">מידע על הפריט אינו זמין</p>
      </div>
    );
  }

  return (
    <div dir="rtl" className="bg-white rounded-2xl shadow-md hover:shadow-lg p-6 transition-all duration-300 border border-gray-100 text-right min-h-[400px]">

      {/* Image container */}
      <div className="w-full h-40 bg-gray-100 rounded-md mb-4 flex items-center justify-center overflow-hidden">
        {item.imageUrl ? (
          <img src={item.imageUrl} alt={item.name} className="object-contain h-full" />
        ) : (
          <span className="text-gray-400">אין תמונה</span>
        )}
      </div>

      {/* Product name*/}
      <h2
        onClick={() => setShowModal(true)}
        className="text-xl font-bold text-blue-700 mb-2 cursor-pointer hover:underline text-center"
      >
        {item.name}
      </h2>

      {/* Price range display */}
      <p className="text-sm text-gray-600 text-center">
        טווח מחירים:
        {item.lowestPrice != null && item.highestPrice != null
          ? ` ₪${item.lowestPrice.toFixed(2)} - ₪${item.highestPrice.toFixed(2)}`
          : ""}
      </p>

      
      <div className="flex items-center justify-between mt-15">
      {!currentUser ? (
  <button
    disabled
    className="bg-gray-300 text-gray-500 font-semibold px-4 py-2 rounded-lg shadow-md cursor-not-allowed"
  >
    התחברי כדי להוסיף
  </button>
) : (
  <button
    onClick={handleAddToCart}
    disabled={isAdding}
    className={`${
      isAdding ? "bg-blue-400" : "bg-blue-600 hover:bg-blue-700"
    } text-white font-semibold px-4 py-2 rounded-lg shadow-md transition cursor-pointer active:scale-95`}
  >
    {isAdding ? "מוסיף..." : "הוסף לעגלה"}
  </button>
)}


       {/* Quantity adjustment controls */}
        <div className="flex items-center gap-2">
            {/* Decrease quantity */}
          <button
            onClick={decreaseQty}
            disabled={quantity <= 1 || isAdding}
            className="w-8 h-8 flex items-center justify-center bg-red-100 hover:bg-red-200 text-red-700 font-bold rounded-full disabled:opacity-50 transition"
          >
            –
          </button>

           {/* Quantity input */}
          <input
            type="number"
            value={quantity}
            min="1"
            max="99"
            disabled={isAdding}
            onChange={(e) => {
              const val = parseInt(e.target.value);
              if (!isNaN(val) && val >= 1 && val <= 99) setQuantity(val);
            }}
            className="w-12 h-8 text-center border border-gray-300 rounded-md text-gray-800 font-bold focus:outline-none focus:ring-2 focus:ring-blue-400"
          />

          {/* Increase quantity button – green */}
          <button
            onClick={increaseQty}
            disabled={isAdding}
            className="w-8 h-8 flex items-center justify-center bg-green-100 hover:bg-green-200 text-green-700 font-bold rounded-full disabled:opacity-50 transition"
          >
            +
          </button>
                {/* Detailed item modal */}
          {showModal && (
      <ItemModal item={item} onClose={() => setShowModal(false)} />
    )}
        </div>
      </div>
    </div>
  );
};

export default ItemCard;