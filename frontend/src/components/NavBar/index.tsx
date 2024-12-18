import { Link } from 'react-router-dom';
import logo from '../../assets/Logo.svg';
import './styles.css';

export default function Navbar() {
    return (
        <nav className="main-navbar">
            <img src={logo} alt="Logotipo" className="logo" />
            <Link to="home" className='logo-text'>Delivery</Link >
        </nav>
    );
}