import "./Logout.css";

import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

interface LogoutProps {
  onLogoutSuccess: () => void;
}

const Logout: React.FC<LogoutProps> = ({ onLogoutSuccess }) => {
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add("logout-page");

    return () => {
      document.body.classList.remove("logout-page");
    };
  }, []);

  const handleLogout = async (e: React.FormEvent, state: string) => {
    e.preventDefault();

    if (state == "cancle") {
      navigate("/");
    } else {
      const res = await fetch("http://localhost:8080/flashcard/logout", {
        method: "POST",
        credentials: "include",
      });

      if (res.ok) {
        console.log("Logout success: " + res.status);
        onLogoutSuccess();
        navigate("/login");
      } else {
        alert("Logout faild: " + res.status);
      }
    }
  };

  return (
    <>
      <div className="logout-container">
        <header>
          <h2>LOGOUT</h2>
          <h3>Are you sure to logout</h3>
        </header>
        <div className="logout-content">
          <div className="cancle">
            <button
              className="btn-cancle"
              onClick={(e) => handleLogout(e, "cancle")}
            >
              CANCLE
            </button>
          </div>
          <div className="confirm">
            <button
              className="btn-confirm"
              onClick={(e) => handleLogout(e, "confirm")}
            >
              CONFIRM
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default Logout;
