import { Link } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {
  return (
    <>
      <div className="navbar-container">
        <div className="navbar-left">
          <Link className="link" to="/">
            HOME
          </Link>
        </div>
        <div className="navbar-right">
          <Link className="link" to="/register">
            REGISTER
          </Link>
          <Link className="link" to="/logout">
            LOGOUT
          </Link>
        </div>
      </div>
    </>
  );
};

export default Navbar;
