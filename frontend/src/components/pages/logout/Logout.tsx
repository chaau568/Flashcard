import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import style from "./Logout.module.css";

interface LogoutProps {
  onLogoutSuccess: () => void;
}

const Logout: React.FC<LogoutProps> = ({ onLogoutSuccess }) => {
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add(style.logout_page);

    return () => {
      document.body.classList.remove(style.logout_page);
    };
  }, []);

  const handleLogout = async (e: React.FormEvent, state: string) => {
    e.preventDefault();

    if (state == "cancle") {
      navigate("/");
    } else {
      const res = await fetch("http://localhost:8080/flashcard/user/logout", {
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
      <div className={style.logout_container}>
        <div className={style.header}>
          <h2>LOGOUT</h2>
          <h3>Are you sure to logout</h3>
        </div>
        <div className={style.logout_content}>
          <div className={style.cancel}>
            <button id="btn_cancel" onClick={(e) => handleLogout(e, "cancle")}>
              CANCLE
            </button>
          </div>
          <div className={style.confirm}>
            <button
              id="btn_confirm"
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
