import ComparisonCard from "../components/ComparisonCard";
import { useStore } from "../context/StoreContext";

const CompareResultsPage = () => {
    // Retrieve the array of comparison results from the global store context
  const { comparisonResults } = useStore();

  return (
    <div className="p-6 max-w-7xl mx-auto bg-white rounded-xl shadow space-y-6">
            {/* Page heading */}
      <h2 className="text-2xl font-bold text-blue-800 mb-6 text-center">
        תוצאות השוואת מחירים 🧾
      </h2>

      {comparisonResults.length === 0 ? (
        <p className="text-center text-gray-600">לא נמצאו תוצאות השוואה.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {comparisonResults.map((result) => (
            <ComparisonCard key={result.store.storeId} storeResult={result} />
          ))}
        </div>
      )}
    </div>
  );
};

export default CompareResultsPage;