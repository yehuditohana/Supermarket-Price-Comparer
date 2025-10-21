import React, { createContext, useContext, useState, useEffect } from "react";
import { logoutUser } from "../api/userAPI";

const UserContext = createContext();
/**
 * Provides user authentication state and actions.
 */
export const UserProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

   // Attempt to restore user from localStorage on mount
  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
       // Normalize user object to include userId for Hibernate compatibility
      const normalizedUser = {
        ...parsedUser,
        userId: parsedUser.userId || parsedUser.id || parsedUser.userID, 
      };
      setCurrentUser(normalizedUser);
    }
    setLoading(false);
  }, []);

 /**
   * Logs in the user, normalizing the ID field and persisting to localStorage.
   *
   * @param {object} userData - Data returned from login API
   */
  const login = (userData) => {
    console.log(" userData שמתקבל מהשרת:", userData); 
    const normalizedUser = {
      ...userData,
      userId: userData.userId || userData.id || userData.userID, 
    
    };

    setCurrentUser(normalizedUser);
    localStorage.setItem("user", JSON.stringify(normalizedUser));
  };
 /**
   * Logs out the user, informs server, and clears localStorage.
   */
  const logout = async () => {
    const storedUser = JSON.parse(localStorage.getItem("user"));
    const sessionNumber = storedUser?.sessionNumber;

    if (sessionNumber) {
      try {
        await logoutUser(sessionNumber);
      } catch (err) {
        console.error("שגיאה בעת התנתקות מהשרת:", err);
      }
    }

    setCurrentUser(null);
    localStorage.removeItem("user");
  };

  return (
    <UserContext.Provider value={{ currentUser, login, logout, loading }}>
      {children}
    </UserContext.Provider>
  );
};
/**
 * Custom hook to access user context.
 *
 * @returns {object} User context value.
 */
export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};
