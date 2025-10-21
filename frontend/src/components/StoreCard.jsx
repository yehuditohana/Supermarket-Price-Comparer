import React from "react";

/**
 * StoreCard displays information about a store and allows selecting or deselecting it.
 *
 * @param {Object} props
 * @param {Object} props.store - Store data with chain and location info
 * @param {boolean} props.isSelected - Whether this store is currently selected
 * @param {function} props.toggle - Handler to select or deselect this store
 * @param {boolean} props.disabled - Whether selection is disabled
 */
const StoreCard = ({ store, isSelected, toggle, disabled }) => {
  return (
    <div
      className={`border rounded-2xl p-4 shadow hover:shadow-md transition-all flex flex-col items-center text-center
      ${isSelected ? "border-green-600 bg-green-50" : "border-gray-200 bg-white"}`}
    >
      {/* Chain name*/}
      <h3 className="text-lg font-bold text-blue-800 mb-2">{store.chainName}</h3>

            {/* Chain logo */}
      {store.chainImageUrl && (
        <img
          src={store.chainImageUrl.replace(/^"|"$/g, '')}
          alt={`לוגו של ${store.chainName}`}
          className="h-20 object-contain mb-4"
        />
      )}
      {/* Store branch name */}
      <p className="text-base font-semibold text-gray-700">סניף: {store.storeName}</p>

        {/* City and address */}
      <p className="text-sm text-gray-600">עיר: {store.storeCity}</p>
      <p className="text-sm text-gray-600">כתובת: {store.storeAddress}</p>

    
      <button
        className={`mt-4 px-4 py-2 rounded-lg text-white font-semibold transition-all ${isSelected
            ? "bg-red-600 hover:bg-red-700"
            : disabled
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
        onClick={() => toggle(store.storeId)}
        disabled={!isSelected && disabled}
      >
        {isSelected ? "הסר" : "בחר"}
      </button>
    </div>
  );
};

export default StoreCard;