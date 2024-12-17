import logo from '../../assets/Logo.svg';
import './styles.css';

export default function Navbar() {
    return (
        <nav className="main-navbar">
            <img src={logo} alt="Logotipo" className="logo" />
            <a href="home" className='logo-text'>Delivery</a>
        </nav>
    );
}