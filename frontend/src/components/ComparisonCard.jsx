import React, { useState } from "react";
import AlternativesModal from "./AlternativesModal";
import ItemModal from "./ItemModal";

/**
 * Displays the comparison results for a single store:
 * shows store name, address, total cart price,
 * highlights missing items count, and lets the user
 * view item details or pick alternatives.
 *
 * @param {Object} props
 * @param {Object} props.storeResult           Comparison data for this store.
 * @param {Object} props.storeResult.store     Store metadata (chainName, storeName, address, city).
 * @param {Object[]} props.storeResult.items   List of cart items with price info.
 * @returns {JSX.Element}
 */

const ComparisonCard = ({ storeResult }) => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [showAlternativesModal, setShowAlternativesModal] = useState(false);
  const [showItemModal, setShowItemModal] = useState(false);
  const [storeItems, setStoreItems] = useState(storeResult.items);
  const [cartPrice, setCartPrice] = useState(storeResult.cartPrice ?? 0);

  const handleOpenAlternativesModal = (item) => {
    setSelectedItem(item);
    setShowAlternativesModal(true);
  };

  const handleCloseAlternativesModal = () => {
    setSelectedItem(null);
    setShowAlternativesModal(false);
  };
// Open detail view modal for any item
  const handleOpenItemModal = (item) => {
    setSelectedItem(item);
    setShowItemModal(true);
  };

  const handleCloseItemModal = () => {
    setSelectedItem(null);
    setShowItemModal(false);
  };
//Displaying the selected alternative product
  const handleAlternativeSelect = (alternativeItem) => {
    const enrichedItem = {
      itemId: alternativeItem.id,
      itemName: alternativeItem.name,
      price: alternativeItem.lowestPrice,
      quantityOfItem: selectedItem.quantityOfItem ?? 1, 
    };

    setStoreItems((prevItems) =>
      prevItems.map((item) =>
        item.itemId === selectedItem.itemId ? enrichedItem : item
      )
    );
// Safely adjust cart total
    if (typeof enrichedItem.price === "number" &&
       !isNaN(enrichedItem.price)&&
       typeof enrichedItem.quantityOfItem === "number"
      ) {
      setCartPrice((prevPrice) =>  prevPrice + (enrichedItem.price * enrichedItem.quantityOfItem));
    }

    setShowAlternativesModal(false);
  };

  const { store } = storeResult;
  const missingItems = storeItems.filter((item) => item.price === null);
  const hasMissing = missingItems.length > 0;

  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 border border-gray-200 hover:shadow-xl transition-all duration-300 flex flex-col justify-between h-full">
      <div className="mb-4">
        <h3 className="text-lg font-bold text-blue-800">
          {store.chainName} - {store.storeName}
        </h3>
        <p className="text-sm text-gray-600">
          {/* Full store address */}
          {store.storeAddress}, {store.storeCity}
        </p>
      </div>

      <div className="text-lg font-medium text-gray-700 mb-4">
        סה"כ מחיר לעגלה:{" "}
        <span className="text-green-600 font-bold">
          ₪{cartPrice.toFixed(2)}
        </span>
        {hasMissing && (
          <span className="text-red-500 text-sm ml-2">
             (חסרים {missingItems.length} מוצרים)
          </span>
        )}
      </div>

      <div className="flex-grow">
        <h4 className="text-sm font-semibold text-gray-500 mb-2"
        >פריטים בעגלה:
        </h4>
        <div className="overflow-x-auto">
    <table className="w-full text-sm text-right text-gray-700 border-collapse">
      <thead className="text-xs text-gray-500 border-b">
        <tr>
          <th className="py-2 px-3">שם מוצר</th>
          <th className="py-2 px-3">מחיר ליחידה</th>
          <th className="py-2 px-3">כמות</th>
          <th className="py-2 px-3">סה"כ</th>
        </tr>
      </thead>
      <tbody>
        {storeItems.map((item) => (
          <tr key={item.itemId} className="border-b">
            {/* item name pushbutton*/}
            <td className="py-2 px-3 font-semibold cursor-pointer hover:text-blue-600"
                onClick={() => handleOpenItemModal(item)}
            >
              {item.itemName}
            </td>

            {/*Display for any available item - price, quantity and total price*/}
            {item.price !== null ? (
              <>
                <td className="py-2 px-3">₪{item.price.toFixed(2)}</td>
                <td className="py-2 px-3">{item.quantityOfItem ?? 1}</td>
                <td className="py-2 px-3">₪{((item.quantityOfItem ?? 1) * item.price).toFixed(2)}</td>
              </>
            ) : (
              <td colSpan="3" className="py-2 px-3 text-red-500 flex items-center gap-2 justify-end">
                 חסר
                <button
                  onClick={() => handleOpenAlternativesModal(item)}
                  className="text-xs text-blue-600 underline hover:text-blue-800"
                >
                  בחר חלופה
                </button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  </div>
      </div>

      <div className="mt-4 pt-4">
        {hasMissing ? (
          <button className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded text-sm">
            קיימים מוצרים חסרים – בחר חלופות
          </button>
        ) : (
          <p className="text-sm text-gray-500 text-center">
            כל הפריטים זמינים בסניף זה
          </p>
        )}
      </div>
{/* Conditionally render modals */}
      {showAlternativesModal && selectedItem && (
        <AlternativesModal
          item={selectedItem}
          storeId={store.storeId}
          onClose={handleCloseAlternativesModal}
          onSelect={handleAlternativeSelect}
        />
      )}

      {showItemModal && selectedItem && (
        <ItemModal item={selectedItem} onClose={handleCloseItemModal} />
      )}
    </div>
  );
};

export default ComparisonCard;