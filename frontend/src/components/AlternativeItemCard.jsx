import React from "react";
/**
 * Card component displaying an alternative item option.
 *
 * @param {Object}   props
 * @param {Object}   props.item           The alternative item data.
 * @param {string}   props.item.name      Name of the item.
 * @param {string}   props.item.imageUrl  Image URL for the item.
 * @param {number}   props.item.lowestPrice  Lowest available price.
 * @param {number}   [props.item.highestPrice]  Highest available price (if different).
 * @param {Function} props.onSelect       Handler invoked when the user selects this item.
 * @param {Function} props.onNameClick    Handler invoked when the user clicks the item name.
 * @returns {JSX.Element}
 */
const AlternativeItemCard = ({ item, onSelect, onNameClick }) => {
  return (
    <div className="flex items-center gap-4 bg-white border border-gray-200 rounded-lg p-4 shadow hover:shadow-md transition">
      <img
        src={item.imageUrl || "https://via.placeholder.com/80"}
        alt={item.name}
        className="w-20 h-20 object-cover rounded"
      />
      <div className="flex-1">
        <h4
          onClick={onNameClick}
          className="text-md font-semibold text-gray-800 mb-1 cursor-pointer hover:text-blue-500"
        >
          {item.name}
        </h4>
        <p className="text-sm text-gray-600">
          ₪{item.lowestPrice ?? "—"}
          {item.highestPrice && item.highestPrice !== item.lowestPrice && (
            <> - ₪{item.highestPrice}</>
          )}
        </p>
      </div>
      <button
        onClick={onSelect}
        className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded text-sm"
      >
        החלף למוצר זה
      </button>
    </div>
  );
};

export default AlternativeItemCard;