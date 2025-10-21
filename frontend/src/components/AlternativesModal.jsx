import React, { useState, useEffect } from "react";
import { fetchItemAlternatives } from "../api/comparisonAPI";
import AlternativeItemCard from "./AlternativeItemCard";
import ItemModal from "./ItemModal";
/**
 * Modal dialog that displays alternative items for a given product and store.
 *
 * @param {Object}   props
 * @param {string|number} props.storeId     Identifier of the store to search in.
 * @param {Object}   props.item             The original item being replaced.
 * @param {string|number} props.item.itemId Identifier of the original item.
 * @param {string}   props.item.itemName    Name of the original item.
 * @param {Function} props.onClose          Handler invoked to close this modal.
 * @param {Function} props.onSelect         Handler invoked with the chosen alternative item.
 * @returns {JSX.Element}
 */
const AlternativesModal = ({ storeId, item, onClose, onSelect }) => {
  const [alternatives, setAlternatives] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedItem, setSelectedItem] = useState(null);
// Fetch alternatives whenever the store or item changes
  useEffect(() => {
    fetchItemAlternatives(storeId, item.itemId)
      .then(setAlternatives)
      .finally(() => setLoading(false));
  }, [storeId, item.itemId]);

// Show the detailed view modal for this alternative
  const handleOpenItemModal = (altItem) => {
    setSelectedItem(altItem);
  };
 // Close the detailed view modal
  const handleCloseItemModal = () => {
    setSelectedItem(null);
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex justify-center z-50 pt-[10vh] backdrop-blur-[1px] overflow-y-auto">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-xl w-full max-h-[80vh] overflow-y-auto">
        <div className="flex justify-between items-start mb-4">
          <h2 className="text-lg font-bold text-blue-800">
            בחר חלופה עבור: {item.itemName}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-red-500 text-xl font-bold focus:outline-none"
            aria-label="Close modal"
          >
            &times;
          </button>
        </div>

        {loading ? (
          <p className="text-center">טוען חלופות...</p>
        ) : alternatives.length === 0 ? (
          <p className="text-center text-gray-600">לא נמצאו חלופות</p>
        ) : (
          <div className="space-y-3">
            {alternatives.map((alt) => (
              <AlternativeItemCard
                key={alt.id}
                item={alt}
                onSelect={() => onSelect(alt)}
                onNameClick={() => handleOpenItemModal(alt)}
              />
            ))}
          </div>
        )}
      </div>

      {selectedItem && <ItemModal item={selectedItem} onClose={handleCloseItemModal} />}
    </div>
  );
};

export default AlternativesModal;