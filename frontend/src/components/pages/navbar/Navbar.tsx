import { Link } from "react-router-dom";
import style from "./Navbar.module.css";

const Navbar = () => {
  return (
    <>
      <div className={style.navbar_container}>
        <div className={style.navbar_left}>
          <Link className={style.link} to="/">
            HOME
          </Link>
        </div>
        <div className={style.navbar_right}>
          <Link className={style.link} to="/inventory">
            My Inventory
          </Link>
          <Link className={style.link} to="/logout">
            LOGOUT
          </Link>
        </div>
      </div>
    </>
  );
};

export default Navbar;
