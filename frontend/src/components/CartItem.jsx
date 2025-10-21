import React from "react";
import { FiTrash2, FiMinus, FiPlus } from "react-icons/fi";
import { useCart } from "../context/CartContext";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import ItemModal from "./ItemModal";

/**
 * CartItem component displays a single item in the shopping cart,
 * allows updating its quantity, and opening a detail modal.
 *
 * @param {Object} props
 * @param {Object} props.cartItem              The cart item data.
 * @param {string|number} props.cartItem.itemId         Unique item identifier.
 * @param {string} props.cartItem.itemName               Item name.
 * @param {string} [props.cartItem.imageUrl]             URL for the item image.
 * @param {number} props.cartItem.quantity               Current quantity in cart.
 * @param {number} props.cartItem.minPrice               Unit lowest price.
 * @param {number} [props.cartItem.maxPrice]             Unit highest price.
 * @param {number} props.cartItem.totalMinPrice          Total lowest price.
 * @param {number} [props.cartItem.totalMaxPrice]        Total highest price.
 * @returns {JSX.Element}
 */


const CartItem = ({ cartItem }) => {
  const { addToCart, removeFromCart, updateQuantity } = useCart();
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  // open item detail modal
  const openModal = () => setShowModal(true);
  return (
    <div
      dir="rtl"
      className="bg-white p-6 rounded-2xl shadow-md border border-blue-100 hover:shadow-lg transition-all text-right"
    >
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">

        {/* Thumbnail or placeholder */}
        <div className="w-32 h-32 flex-shrink-0 bg-gray-100 rounded-lg overflow-hidden flex items-center justify-center">
          {cartItem.imageUrl ? (
            <img
              src={cartItem.imageUrl}
              alt={cartItem.itemName}
              className="w-full h-full object-contain"
            />
          ) : (
            <span className="text-gray-400 text-sm">אין תמונה</span>
          )}
        </div>

        {/*Product Details*/}
        <div className="flex-grow">
          <h2
            onClick={openModal}
            className="text-xl font-semibold text-blue-700 cursor-pointer hover:underline"
          >
            {cartItem.itemName}
          </h2>

          {/* Unit Prices */}
          <p className="text-sm text-gray-600 mt-2">
            מחיר ליחידה: ₪{(parseFloat(cartItem.minPrice) || 0).toFixed(2)}
            {cartItem.maxPrice && cartItem.maxPrice !== cartItem.minPrice && (
              <> - ₪{(parseFloat(cartItem.maxPrice) || 0).toFixed(2)}</>
            )}
          </p>
        </div>

        {/* Quantity controls and remove button */}
        <div className="flex flex-col items-center gap-2 md:items-end">
          <div className="flex items-center gap-3">
            <button
              onClick={() => removeFromCart(cartItem.itemId, 1)}
              disabled={cartItem.quantity <= 1}
              className="bg-blue-100 text-blue-700 rounded-full w-8 h-8 flex items-center justify-center hover:bg-blue-200 disabled:opacity-50"
            >
              <FiMinus />
            </button>
            <span className="text-lg font-semibold text-gray-800">
              {cartItem.quantity}
            </span>
            <button
              onClick={() => addToCart(cartItem.itemId, 1)}
              className="bg-blue-100 text-blue-700 rounded-full w-8 h-8 flex items-center justify-center hover:bg-blue-200"
            >
              <FiPlus />
            </button>
          </div>
          {/* Remove all units of this item */}
          <button
            onClick={() => removeFromCart(cartItem.itemId, cartItem.quantity)}
            className="mt-2 bg-red-500 text-white px-3 py-1.5 rounded-full flex items-center gap-1 hover:bg-red-600 transition-all"
            title="הסר מהעגלה"
          >
            <FiTrash2 className="text-sm" />
            <span className="text-sm">הסר</span>
          </button>

          {/* Total price for this line item */}
          <p className="text-sm text-gray-600 mt-3">
            מחיר כולל: ₪{(parseFloat(cartItem.totalMinPrice) || 0).toFixed(2)}
            {cartItem.totalMaxPrice && cartItem.totalMaxPrice !== cartItem.totalMinPrice && (
              <> - ₪{(parseFloat(cartItem.totalMaxPrice) || 0).toFixed(2)}</>
            )}
          </p>
          {/* Detail modal for this item */}
          {showModal && (
            <ItemModal
              item={{
                id: cartItem.itemId,
                name: cartItem.itemName,
                imageUrl: cartItem.imageUrl,
                lowestPrice: cartItem.minPrice,
                highestPrice: cartItem.maxPrice,
                quantity: cartItem.quantity,
                unitQty: cartItem.unitQty,
                manufacturerName: cartItem.manufacturerName,
                manufactureCountry: cartItem.manufactureCountry,
                weighted: cartItem.weighted,
              }}
              onClose={() => setShowModal(false)}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default CartItem;