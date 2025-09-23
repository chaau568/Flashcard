import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import playQ from "../../../assets/playQ.png";
import style from "./Login.module.css";

interface LoginProps {
  onLoginSuccess: () => void;
}

const Login: React.FC<LoginProps> = ({ onLoginSuccess }) => {
  const [username, Setusername] = useState<string>("");
  const [password, Setpassword] = useState<string>("");

  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add(style.login_page);

    return () => {
      document.body.classList.remove(style.login_page);
    };
  }, []);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    const res = await fetch("http://localhost:8080/flashcard/user/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
      credentials: "include",
    });

    const data = await res.json();

    if (res.ok) {
      onLoginSuccess();
      navigate("/");
    } else {
      alert("Login failed: " + data.message);
    }
  };

  return (
    <>
      <div className={style.login_container}>
        <div className={style.header}>
          <img src={playQ} alt="playQ"></img>
        </div>
        <div className={style.login_content}>
          <form className={style.form_container} onSubmit={handleLogin}>
            <div className={style.username}>
              <h4>Username</h4>
              <input
                type="text"
                value={username}
                onChange={(event) => Setusername(event.target.value)}
                required
              ></input>
            </div>
            <div className={style.password}>
              <h4>Password</h4>
              <input
                type="password"
                value={password}
                onChange={(event) => Setpassword(event.target.value)}
                required
              ></input>
            </div>
            <div className={style.form_btn}>
              <button type="submit">Login</button>
            </div>
            <div className={style.alternative_login}>
              <h5>
                DON'T HAVE AN ACCOUNT?
                <a
                  onClick={() => navigate("/register")}
                  className={style.register_link}
                >
                  {" "}
                  REGISTER NOW
                </a>
              </h5>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Login;
