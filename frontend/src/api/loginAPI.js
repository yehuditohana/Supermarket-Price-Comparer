/**
 * Attempt to authenticate a user with email and password.
 *
 * @param {string} email       The user’s email address.
 * @param {string} password    The user’s password.
 * @returns {Promise<Object>}  Resolves with user info or auth token on success.
 * @throws {Error}             If the server returns an error or login fails.
 */

export const loginUser = async (email, password) => {
  const response = await fetch(
    `http://localhost:8080/api/users/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || "Login failed");
  }

  return await response.json(); 
};
