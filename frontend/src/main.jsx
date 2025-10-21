import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";
import { CartProvider } from "./context/CartContext";
import { UserProvider } from "./context/UserContext";
import { ItemProvider } from "./context/ItemContext";
import { StoreProvider } from "./context/StoreContext";



ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <UserProvider>
        <ItemProvider>
          <CartProvider>
          <StoreProvider>
            <App />
          </StoreProvider>
          </CartProvider>
        </ItemProvider>
      </UserProvider>
    </BrowserRouter>
  </React.StrictMode>
);