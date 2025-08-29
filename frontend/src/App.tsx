import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Home from "./components/pages/home/Home";
import Register from "./components/pages/register/Register";
import Login from "./components/pages/login/Login";
import Logout from "./components/pages/logout/Logout";
import Navbar from "./components/pages/navbar/Navbar";
import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";

function LayoutWithNavbar() {
  return (
    <>
      <Navbar />
      <Outlet />
    </>
  );
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await fetch("http://localhost:8080/flashcard/greeting", {
          method: "GET",
          credentials: "include",
        });
        if (res.ok) {
          setIsLoggedIn(true);
        } else {
          setIsLoggedIn(false);
        }
      } catch (err) {
        setIsLoggedIn(false);
      } finally {
        setLoading(false);
      }
    };
    checkAuth();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            isLoggedIn ? (
              <Navigate to="/" />
            ) : (
              <Login onLoginSuccess={() => setIsLoggedIn(true)} />
            )
          }
        />
        <Route
          path="/logout"
          element={<Logout onLogoutSuccess={() => setIsLoggedIn(false)} />}
        />

        <Route element={<LayoutWithNavbar />}>
          <Route
            path="/"
            element={isLoggedIn ? <Home /> : <Navigate to="/login" />}
          />
          <Route
            path="/register"
            element={isLoggedIn ? <Register /> : <Navigate to="/login" />}
          />
          {/* <Route path="/logout" element={<Logout />} /> */}
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
