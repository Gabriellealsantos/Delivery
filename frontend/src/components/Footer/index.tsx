import SocialIcon from '../SocialIcon';
import './styles.css';

export default function Footer() {
  return (
    <footer className='main-footer'>
      App desenvolvido durante a 2ª ed. do evento Semana DevSuperior
      <div className='footer-icons'>
        <SocialIcon />
      </div>

    </footer>
  );
}