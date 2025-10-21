# Frontend README for myStore Application

This document explains how to set up and run the **frontend** part of the **myStore** project, which is a React-based web application for supermarket price comparison.

## Prerequisites

Before starting, make sure you have installed:

- **Node.js** (version 18 or higher recommended)
- **npm** (comes bundled with Node.js)

You can check by running:

```bash
node -v
npm -v
```

If not installed, download from [Node.js official website](https://nodejs.org/).

---

## Project Overview

The frontend project uses:

- **React 19** — for building the user interface
- **Vite** — for fast development and building
- **TailwindCSS** — for styling
- **Axios** — for making HTTP requests
- **React Router** — for page routing
- **Context API** — for managing state like Cart, Items, Users

The frontend communicates with the backend (Spring Boot) via an API proxy.

---

## How to Set Up and Run

1. **Open the Frontend Folder**

   Navigate to the folder containing the frontend files.

2. **Install All Dependencies**

   Run the following command:

   ```bash
   npm install
   ```

3. **Start the Development Server**

   Run:

   ```bash
   npm run dev
   ```

   By default, the app will be available at:

   ```
   http://localhost:5173
   ```

   (If port 5173 is occupied, Vite will suggest another available port.)

---

## Connecting to Backend

- API requests to `/api` are automatically forwarded to the backend (`http://localhost:8080`) through Vite's proxy.
- Make sure the backend server is running before starting the frontend.

---

## Available npm Scripts

- **`npm run dev`** — Starts the local development server.
- **`npm run build`** — Creates a production build inside the `/dist` folder.
- **`npm run preview`** — Previews the production build locally.
- **`npm run lint`** — Runs ESLint to check for code quality issues.
