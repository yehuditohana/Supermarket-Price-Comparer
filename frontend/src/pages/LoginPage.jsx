import React, { useState, useRef, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "../context/UserContext";
import { loginUser } from "../api/loginAPI";

const LoginPage = () => {
  const { login } = useUser(); // get login function from context to update global user state
  const navigate = useNavigate(); // hook to programmatically navigate after successful login

  const emailRef = useRef();// ref for email input, used to focus on mount
  const errRef = useRef();// ref for the error message, used to focus when an error occurs

  // form state holds email & password
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState(""); // holds any error message to display
  const [loading, setLoading] = useState(false);// toggles disabled state of submit button
// on initial mount, focus the email input
  useEffect(() => {
    emailRef.current?.focus();
  }, []);

// whenever form fields change, clear any existing error
  useEffect(() => {
    setError("");
  }, [form]);

  // update form state on each keystroke
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
            // call backend API to authenticate user
      const userData = await loginUser(form.email, form.password);

      // normalize returned data, extract token if needed
      const token = userData?.token || "";

            // update global user context with returned user info
      login({
        email: userData.email,
        name: userData.username,
        id: userData.userId,
        sessionNumber: userData.sessionNumber, 
      });

            // clear form fields
      setForm({ email: "", password: "" });
      navigate("/");
    } catch (err) {
            // show error message (either specific or generic)
      setError(
        "פרטי התחברות שגויים"
      );
      errRef.current?.focus();
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center h-full">
      <div className="max-w-md w-full bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-blue-800 mb-3">התחברות 🔐</h2>
        <p
          ref={errRef}
          className={error ? "text-red-500 text-sm mb-2" : "sr-only"}
          aria-live="assertive"
        >
          {error}
        </p>

        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <input
              name="email"
              ref={emailRef}
              onChange={handleChange}
              value={form.email}
              type="email"
              placeholder="אימייל"
              className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-600"
              required
            />
          </div>

          <div>
            <input
              name="password"
              onChange={handleChange}
              value={form.password}
              type="password"
              placeholder="סיסמה"
              className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-600"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white p-2 rounded-md font-semibold transition"
            disabled={loading}
          >
            {loading ? "מתחבר..." : "התחבר"}
          </button>
        </form>

        <div className="text-sm text-gray-600 mt-3">
          אין לך חשבון עדיין?
          <Link
            to="/register"
            className="text-blue-600 hover:text-blue-800 font-semibold mr-1"
          >
            הירשם כאן
          </Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
