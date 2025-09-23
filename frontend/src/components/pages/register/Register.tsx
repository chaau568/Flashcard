import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import playQ from "../../../assets/playQ.png";
import style from "./Register.module.css";

const Register = () => {
  const [username, Setusername] = useState<string>("");
  const [password, Setpassword] = useState<string>("");
  const [confirmPassword, SetconfirmPassword] = useState<string>("");

  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add(style.register_page);

    return () => {
      document.body.classList.remove(style.register_page);
    };
  }, []);

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    if (password === confirmPassword) {
      const res = await fetch("http://localhost:8080/flashcard/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (res.ok) {
        alert("Login successed: " + data.message);
        navigate("/login");
      } else {
        alert("Login failed: " + data.message);
      }
    } else {
      alert("Your password and confirm password do not match.");
    }
  };

  return (
    <>
      <div className={style.register_container}>
        <div className={style.header}>
          <img src={playQ} alt="playQ"></img>
        </div>
        <div className={style.register_content}>
          <form className={style.form_container} onSubmit={handleRegister}>
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
            <div className={style.confirm_password}>
              <h4>Confirm Password</h4>
              <input
                type="password"
                value={confirmPassword}
                onChange={(event) => SetconfirmPassword(event.target.value)}
                required
              ></input>
            </div>
            <div className={style.form_btn}>
              <button type="submit">Register</button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Register;
