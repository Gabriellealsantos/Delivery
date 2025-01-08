import { ProductDTO } from '../../models/OrderDTO';
import { formatPriece } from '../../utils/helpers';
import './styles.css';

type Props = {
  product: ProductDTO;
  onSelectProduct: (product: ProductDTO) => void;
  isSelected: boolean;
}

export default function ProductCard({ product, onSelectProduct, isSelected }: Props) {
  return (
    <div 
    className={`order-card-container ${isSelected ? 'selected' : ''}`}
    onClick={() => onSelectProduct(product)}
    >
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