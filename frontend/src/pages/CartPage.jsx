import React, { useState } from "react";
import { useCart } from "../context/CartContext";
import { useSearchParams } from "react-router-dom";
import { FiShoppingCart, FiSave } from "react-icons/fi";
import CartItem from "../components/CartItem";
import ItemModal from "../components/ItemModal"; 
import { useItems as useProducts } from "../context/ItemContext";
import { useUser } from "../context/UserContext";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const CartPage = () => {
  const {
    cart,
    fetchActiveCartItems,
    clearCart,
  } = useCart();

  const navigate = useNavigate();
  const { products } = useProducts();
  const { currentUser } = useUser();

  const [searchParams] = useSearchParams();


  const [showConfirm, setShowConfirm] = useState(false);
  const [showSaveModal, setShowSaveModal] = useState(false);
  const [cartName, setCartName] = useState("");
  // Get product ID from query string, find matching product
  const productId = searchParams.get("id");
  const selectedProduct = productId
    ? products.find((p) => String(p.id) === productId)
    : null;


  const [saveMessage, setSaveMessage] = useState("");
  const { archiveActiveCart } = useCart();

  // Save the active cart to archive
  const handleArchivCart = async () => {
    const savedCart = await archiveActiveCart(cartName);
    setCartName("");
    setShowSaveModal(false);
  };



  //Displaying the updated cart upon entering the page
  useEffect(() => {
    fetchActiveCartItems();
  }, []);

  return (
    <div dir="rtl" className="max-w-6xl mx-auto px-[5%] py-12 text-right">
      <h1 className="text-4xl font-extrabold text-blue-700 text-center mb-10">
        עגלת הקניות שלך 🛒
      </h1>


      {cart.length === 0 ? (
        <div className="bg-white text-center p-10 rounded-2xl shadow-md text-gray-600">
          <FiShoppingCart className="mx-auto text-5xl mb-4 text-blue-500" />
          <p className="text-lg">
            העגלה שלך ריקה! ניתן להוסיף פריטים לבחירתך 🛍️
          </p>
        </div>
      ) : (
        <>
          <div className="flex justify-between mb-6 gap-4 flex-wrap">
            <div className="relative group">
              <button
                onClick={() => setShowSaveModal(true)}
                disabled={!currentUser}
                className={`font-semibold px-4 py-2 rounded-lg shadow flex items-center gap-2
                  ${currentUser
                    ? "bg-green-600 hover:bg-green-700 text-white"
                    : "bg-gray-300 text-gray-500 cursor-not-allowed"
                  }
                `}
              >
                <FiSave />
                העבר לארכיון
              </button>
              {!currentUser && (
                <div className="absolute bottom-full mb-2 right-0 bg-black text-white text-xs px-3 py-1 rounded shadow-lg opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                  על מנת לשמור עגלות עליך להתחבר
                </div>
              )}
            </div>

            <button
              onClick={() => setShowConfirm(true)}
              className="bg-red-500 hover:bg-red-600 text-white font-semibold px-4 py-2 rounded-lg shadow"
            >
              נקה עגלה
            </button>
          </div>

          <div className="space-y-6">
            {cart.map((cartItem) => (
              <CartItem key={cartItem.itemId} cartItem={cartItem} />
            ))}
          </div>

          <button
            onClick={() => navigate("/select-stores")}
            className="w-full text-center mt-12 bg-green-600 p-6 rounded-2xl shadow-lg hover:shadow-xl hover:bg-green-700 transition-all"
          >
            <div className="flex justify-center items-center gap-4 mb-4">
              <h2 className="text-xl font-bold text-white">
                טווח מחירים משוער לעגלה:
              </h2>
              <span className="text-white font-semibold text-lg">
                {cart.length > 0 ? (
                  <>
                    ₪{cart.reduce((sum, item) => sum + (item.totalMinPrice ?? 0), 0).toFixed(2)}
                    {" - "}
                    ₪{cart.reduce((sum, item) => sum + (item.totalMaxPrice ?? item.totalMinPrice ?? 0), 0).toFixed(2)}
                  </>
                ) : (
                  "אין פריטים לחישוב"
                )}
              </span>
            </div>

            <div className="space-y-2">
              <div className="flex justify-center items-center text-3xl font-medium py-2">
                <span className="text-white">בחר סניפים להשוואה </span>
              </div>
            </div>
          </button>

          {/* Item modal if product selected */}
          {selectedProduct && <ItemModal item={selectedProduct} />}
        </>
      )}
      {/* Confirm clear cart modal */}
      {showConfirm && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-lg w-[90%] max-w-md text-center space-y-4">
            <h3 className="text-xl font-semibold text-gray-800">
              האם את/ה בטוח/ה שברצונך לרוקן את כל העגלה?
            </h3>
            <div className="flex justify-center gap-4 mt-6">
              <button
                onClick={() => {
                  clearCart();
                  setShowConfirm(false);
                }}
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg font-semibold"
              >
                כן, נקה
              </button>
              <button
                onClick={() => setShowConfirm(false)}
                className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded-lg font-semibold"
              >
                ביטול
              </button>
            </div>
          </div>
        </div>
      )}
      {/* Archive cart modal */}
      {showSaveModal && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-lg w-[90%] max-w-md text-center space-y-4">
            <h3 className="text-xl font-semibold text-gray-800">העבר עגלה לארכיון</h3>
            <input
              type="text"
              value={cartName}
              onChange={(e) => setCartName(e.target.value)}
              placeholder="הקלד שם לעגלה (אופציונלי)"
              className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <div className="flex justify-center gap-4 mt-4">
              <button
                onClick={() => handleArchivCart()}
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-semibold"
              >
                העבר עגלה לארכיון
              </button>
              <button
                onClick={() => setShowSaveModal(false)}
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

export default CartPage;