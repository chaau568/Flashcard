import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Register from './components/pages/register/Register'
import Home from './components/pages/Home/Home'

function App() {

  return (
    <>
      <Router>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/registor' element={<Register />} />
        </Routes>
      </Router>
    </>
  )
}

export default App
