import "./Login.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, Setusername] = useState<string>("");
  const [password, Setpassword] = useState<string>("");

  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    const res = await fetch("http://localhost:8080/flashcard/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
      credentials: "include",
    });

    const data = await res.json();

    if (res.ok) {
      navigate("/");
    } else {
      alert("Login failed: " + data.message);
    }
  };

  return (
    <>
      <div className="login-container">
        <form className="form-container" onSubmit={handleLogin}>
          <div className="form-username">
            <label>Username</label>
            <input
              type="text"
              placeholder="Sing username in here..."
              value={username}
              onChange={(e) => Setusername(e.target.value)}
              required
            ></input>
          </div>
          <div className="form-password">
            <label>Password</label>
            <input
              type="password"
              placeholder="Sing password in here..."
              value={password}
              onChange={(e) => Setpassword(e.target.value)}
              required
            ></input>
          </div>
          <div className="form-submit">
            <button type="submit">Login</button>
          </div>
        </form>
      </div>
    </>
  );
};

export default Login;
