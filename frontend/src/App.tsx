import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  Outlet,
} from "react-router-dom";
import { useEffect, useState, type JSX } from "react";

import Inventory from "./components/pages/inventory/Inventory";
import Home from "./components/pages/home/Home";
import Register from "./components/pages/register/Register";
import Login from "./components/pages/login/Login";
import Logout from "./components/pages/logout/Logout";
import Navbar from "./components/pages/navbar/Navbar";
import DeckOwner from "./components/pages/deck_owner/DeckOwner";
import DeckPublic from "./components/pages/deck_public/DeckPublic";
import DeckFinish from "./components/pages/deck_finish/DeckFinish";
import DeckCreate from "./components/pages/deck_create/DeckCreate";
import DeckUpdate from "./components/pages/deck_update/DeckUpdate";

function LayoutWithNavbar() {
  return (
    <>
      <Navbar />
      <Outlet />
    </>
  );
}

// ProtectedRoute ที่ handle loading และ session
function ProtectedRoute({
  isLoggedIn,
  loading,
  children,
}: {
  isLoggedIn: boolean;
  loading: boolean;
  children: JSX.Element;
}) {
  if (loading) return <div>Loading...</div>; // รอจนกว่า checkAuth จะเสร็จ
  if (!isLoggedIn) return <Navigate to="/login" replace />;
  return children;
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await fetch("http://localhost:8080/flashcard/authen", {
          method: "GET",
          credentials: "include", // สำคัญ ต้อง include cookie ของ session
        });
        if (res.ok) {
          setIsLoggedIn(true);
          console.log("session exists");
        } else {
          setIsLoggedIn(false);
          console.log("session exists");
        }
      } catch (err) {
        setIsLoggedIn(false);
      } finally {
        setLoading(false); // ✅ รอจนเช็คเสร็จ
      }
    };

    checkAuth();
  }, []);

  if (loading) return <div>Loading...</div>; // render หน้า loading จนกว่า checkAuth จะเสร็จ

  return (
    <Router>
      <Routes>
        {/* Login */}
        <Route
          path="/login"
          element={
            isLoggedIn ? (
              <Navigate to="/" replace />
            ) : (
              <Login
                onLoginSuccess={() => {
                  setIsLoggedIn(true);
                }}
              />
            )
          }
        />

        {/* Logout */}
        <Route
          path="/logout"
          element={
            <Logout
              onLogoutSuccess={() => {
                setIsLoggedIn(false);
              }}
            />
          }
        />

        {/* Register */}
        <Route path="/register" element={<Register />} />

        {/* Deck Owner */}
        <Route
          path="/deck_owner/:deckId"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
              <DeckOwner />
            </ProtectedRoute>
          }
        />

        {/* Deck Public */}
        <Route
          path="/deck_public/:deckId"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
              <DeckPublic />
            </ProtectedRoute>
          }
        />

        {/* Deck Finish */}
        <Route
          path="/deck_finish"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
              <DeckFinish />
            </ProtectedRoute>
          }
        />

        {/* Deck Create */}
        <Route
          path="/deck_create"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
              <DeckCreate />
            </ProtectedRoute>
          }
        />

        {/* Deck Update */}
        <Route
          path="/deck_update/:deckId"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
              <DeckUpdate />
            </ProtectedRoute>
          }
        />

        {/* Layout With Navbar */}
        <Route element={<LayoutWithNavbar />}>
          <Route
            path="/"
            element={
              <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
                <Home />
              </ProtectedRoute>
            }
          />
          <Route
            path="/inventory"
            element={
              <ProtectedRoute isLoggedIn={isLoggedIn} loading={loading}>
                <Inventory />
              </ProtectedRoute>
            }
          />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
