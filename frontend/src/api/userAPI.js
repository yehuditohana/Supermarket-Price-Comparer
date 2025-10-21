const BASE_URL = "http://localhost:8080/api/users";
/**
 * Attempt to authenticate a user with email and password.
 *
 * @param {string} email       The user’s email address.
 * @param {string} password    The user’s password.
 * @returns {Promise<Object>}  Resolves with the user object (including session info) on success.
 * @throws {Error}             If the login request fails or credentials are invalid.
 */
export const loginUser = async (email, password) => {
  const url = `${BASE_URL}/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
  const response = await fetch(url, { method: "POST" });
  if (!response.ok) throw new Error("Login failed");
  return await response.json();
};
/**
 * Log out the current user session.
 *
 * @param {string|number} sessionNumber  The session identifier to invalidate.
 * @returns {Promise<void>}              Resolves with no value when logout succeeds.
 * @throws {Error}                       If the logout request fails.
 */
export const logoutUser = async (sessionNumber) => {
  const response = await fetch(`http://localhost:8080/api/users/logout?session=${encodeURIComponent(sessionNumber)}`, {
    method: "POST",
  });

  if (!response.ok) {
    throw new Error("Logout failed");
  }
};