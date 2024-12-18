import pizza from '../../assets/pizza.svg';
import './styles.css';

export default function ProductCard() {
  return (
    <div className='order-card-container'>
      <h3 className='order-card-title'>
        Pizza Calabresa
      </h3>
      <img src={pizza} alt="Pizza" className='order-cad-image' />
      <h3 className='order-card-price'>35,90</h3>
      <div className='order-card-description'>
        <h3>
          Descrição
        </h3>
        <p>
          Uma deliciosa combinação de Linguiça Calabresa, rodelas de cebolas frescas, azeitonas pretas, mussarela, polpa de tomate, orégano e massa especial.
        </p>

      </div>

    </div>
  );
}