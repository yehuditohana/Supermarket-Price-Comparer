import React, { useState } from "react";
import { FiTrash2, FiShoppingCart, FiChevronDown, FiChevronUp } from "react-icons/fi";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";

/**
 * SavedCartCard displays a saved shopping cart with controls to expand details,
 * load the cart into the active cart, or delete the archived cart.
 */
const SavedCartCard = ({ cart, onRefresh }) => {
  const navigate = useNavigate();
  const [expanded, setExpanded] = useState(false);
  const [cartItems, setCartItems] = useState([]);
  const [loadingItems, setLoadingItems] = useState(false);
  const [error, setError] = useState(null);
  const { loadArchivedCartItems, deleteCartFromArchived, loadArchivedCart } = useCart();

  const [showLoadConfirm, setShowLoadConfirm] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
/**
   * Toggle expansion of the cart details. If collapsed, load items on first expand.
   */
  const toggleExpand = async () => {
    if (expanded) return setExpanded(false);

    if (cartItems.length === 0) {
      setLoadingItems(true);
      try {
        const items = await loadArchivedCartItems(cart.id);
        setCartItems(items);
      } catch (err) {
        console.error(err);
        setError("שגיאה בטעינת פריטים");
      } finally {
        setLoadingItems(false);
      }
    }
    setExpanded(true);
  };
/**
   * Load this archived cart into the active cart and navigate to the cart page.
   */
  const handleLoadCart = async () => {
    try {
      await loadArchivedCart(cart.id);
      navigate("/cart");
    } catch (err) {
      console.error(" שגיאה בטעינת העגלה:", err);
      setError("שגיאה בטעינת העגלה");
    }
  };
 /**
   * Delete this archived cart and refresh the list.
   */
  const handleDeleteCart = async () => {
    try {
      await deleteCartFromArchived(cart.id);
      onRefresh?.();
    } catch (err) {
      console.error(" שגיאה במחיקה:", err);
      setError("שגיאה במחיקת העגלה");
    }
  };

  return (
    <div dir="rtl" className="bg-white p-6 rounded-2xl shadow border border-blue-100 hover:shadow-md transition-all text-right">
      <div className="flex items-start justify-between gap-2">
        <div>
           {/* Header: cart name and last update */}
          <h2 className="text-xl font-bold text-blue-800">{cart.name}</h2>
          <p className="text-sm text-gray-500">
            עודכן לאחרונה: {new Date(cart.updatedAt).toLocaleString("he-IL")}
          </p>
        </div>

        <button onClick={toggleExpand} className="text-blue-700 hover:text-blue-900 text-xl mt-1">
          {expanded ? <FiChevronUp /> : <FiChevronDown />}
        </button>
      </div>
{/* Display error if any */}
      {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

      <div className="flex gap-3 mt-4">
        <button
          onClick={() => setShowLoadConfirm(true)}
          className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition"
        >
          <FiShoppingCart />
          טען עגלה
        </button>

        <button
          onClick={() => setShowDeleteConfirm(true)}
          className="flex items-center gap-2 bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg transition"
        >
          <FiTrash2 />
          מחק עגלה
        </button>
      </div>
 {/* Expanded section: list of items */}
      {expanded && (
        <div className="mt-6 space-y-4">
          {loadingItems ? (
            <p className="text-sm text-gray-500">טוען פריטים...</p>
          ) : cartItems.length === 0 ? (
            <p className="text-sm text-gray-500">אין פריטים בעגלה זו</p>
          ) : (
            <div className="flex flex-wrap gap-4">
              {cartItems.map((item) => (
                <div key={item.itemId} className="flex items-center bg-gray-100 rounded-lg px-4 py-3 shadow text-right w-full sm:basis-1/2 md:basis-1/3 lg:basis-1/4 max-w-full">
                  <div className="w-12 h-12 bg-white rounded overflow-hidden flex-shrink-0 ml-3">
                    {item.imageUrl ? (
                      <img src={item.imageUrl} alt={item.itemName} className="w-full h-full object-contain" />
                    ) : (
                      <span className="text-xs text-gray-400 flex items-center justify-center h-full">אין</span>
                    )}
                  </div>
                  <div className="text-sm text-gray-800">
                    <p className="font-semibold">{item.itemName}</p>
                    <p className="text-gray-600">כמות: {item.quantity}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Confirmation modal for loading cart */}
      {showLoadConfirm && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-lg w-[90%] max-w-md text-center space-y-4">
            <h3 className="text-xl font-semibold text-gray-800">
              טעינת העגלה תחליף את כל המוצרים שנמצאים כעת בעגלה. האם לטעון את "{cart.name}"?
            </h3>
            <div className="flex justify-center gap-4 mt-6">
              <button
                onClick={() => {
                  setShowLoadConfirm(false);
                  handleLoadCart();
                }}
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-semibold"
              >
                כן, טען
              </button>
              <button
                onClick={() => setShowLoadConfirm(false)}
                className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-lg font-semibold"
              >
                ביטול
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Confirmation modal for deleting cart */}
      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-lg w-[90%] max-w-md text-center space-y-4">
            <h3 className="text-xl font-semibold text-gray-800">
              האם את/ה בטוח/ה שברצונך למחוק את "{cart.name}"?
            </h3>
            <div className="flex justify-center gap-4 mt-6">
              <button
                onClick={() => {
                  setShowDeleteConfirm(false);
                  handleDeleteCart(); 
                }}
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg font-semibold"
              >
                כן, מחק
              </button>
              <button
                onClick={() => setShowDeleteConfirm(false)}
                className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-lg font-semibold"
              >
                ביטול
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default SavedCartCard;