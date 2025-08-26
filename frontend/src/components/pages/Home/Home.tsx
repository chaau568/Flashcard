import { Link } from 'react-router-dom'
import './Home.css'

const Home = () => {
    return (
        <>
            <div className='home-container'>
                <div className='left'>
                    <Link className='link' to="/">Home</Link>
                </div>
                <div className="right">
                    <Link className='link' to="/registor">Registor1</Link>
                    <Link className='link' to="/registor">Registor2</Link>
                    <Link className='link' to="/registor">Registor3</Link>
                    <Link className='link' to="/registor">Registor4</Link>
                </div>
            </div>
        </>
    )
}

export default Home