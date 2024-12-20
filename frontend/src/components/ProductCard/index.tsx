import { ProductDTO } from '../../models/OrderDTO';
import './styles.css';

type Props = {
  product: ProductDTO;
}

function formatPriece(price: number) {
  const formatter = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
    minimumFractionDigits: 2
  });
  return formatter.format(price);
}

export default function ProductCard({ product }: Props) {
  return (
    <div className='order-card-container'>
      <h3 className='order-card-title'>
        {product.name}
      </h3>
      <img src={product.imageUri} alt={product.name} className='order-card-image' />
      <h3 className='order-card-price'>{formatPriece(product.price)}</h3>
      <div className='order-card-description'>
        <h3>
          Descrição
        </h3>
        <p>
          {product.description}
        </p>

      </div>

    </div>
  );
}