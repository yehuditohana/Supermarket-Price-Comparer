import React, { useEffect, useState } from "react";
import { FiArchive, FiShoppingCart } from "react-icons/fi";
import { getCartHistory } from "../api/cartAPI";
import { useCart } from "../context/CartContext";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import SavedCartCard from "../components/SavedCartCard";

const SavedCartsPage = () => {
   // State for storing the list of saved carts
  const [saved, setSaved] = useState([]);
    // Get currentUser and loading flag from UserContext
  const { currentUser, loading } = useUser();
  const navigate = useNavigate();

  // Function to fetch saved (archived) carts from the server
  // Can be called internally or passed down as a prop for child components
  const fetchSavedCarts = async () => {
    try {
      const carts = await getCartHistory(currentUser.id);
            // Call API to fetch the current user's cart history
      carts.sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt));
      setSaved(carts);
    } catch (err) {
      console.error("Error loading saved carts:", err);
    }
  };

  useEffect(() => {
    if (!loading && !currentUser) {
      navigate("/login");
      return;
    }
        // Once we have a logged-in user, fetch their saved carts
    if (currentUser) {
      fetchSavedCarts();
    }
  }, [currentUser, loading, navigate]);
  // While loading or if no user, render nothing (could show a spinner here)
  if (loading || !currentUser) return null;

  return (
    <div dir="rtl" className="max-w-6xl mx-auto px-[5%] py-12 text-right">
      <h1 className="text-4xl font-extrabold text-blue-700 text-center mb-10 flex items-center justify-center gap-2">
        עגלות שמורות
        <FiArchive className="text-blue-600" />
      </h1>

      {saved.length === 0 ? (
        <div className="bg-white text-center p-10 rounded-2xl shadow-md text-gray-600">
          <FiShoppingCart className="mx-auto text-5xl mb-4 text-blue-500" />
          <p className="text-lg">לא נשמרו עגלות עד כה</p>
        </div>
      ) : (
        <div className="space-y-6">
          {saved.map((cart) => (
            <SavedCartCard key={cart.id} cart={cart} onRefresh={fetchSavedCarts} />
          ))}
        </div>
      )}
    </div>
  );
};

export default SavedCartsPage;
