import { Link } from "react-router-dom";
import "./Home.css";

const Home = () => {
  return (
    <>
      <div className="home-container">
        <div className="left">
          <Link className="link" to="/">
            Home
          </Link>
        </div>
        <div className="right">
          <Link className="link" to="/registor">
            Registor
          </Link>
          <Link className="link" to="/login">
            Login
          </Link>
        </div>
      </div>
    </>
  );
};

export default Home;
