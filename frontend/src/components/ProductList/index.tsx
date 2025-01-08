import { ProductDTO } from '../../models/OrderDTO';
import { checkIsSelected } from '../../utils/helpers';
import ProductCard from '../ProductCard';
import './styles.css';

type Props = {
  products: ProductDTO[];
  selectedProducts: ProductDTO[];
  onSelectProduct: (product: ProductDTO) => void;
}

export default function ProductList({ products, onSelectProduct, selectedProducts }: Props) {
  return (
    <div className='orders-list-container'>
      <div className='orders-list-items'>
        {
          products.map(product =>
          (<ProductCard
            key={product.id}
            product={product}
            onSelectProduct={onSelectProduct}
            isSelected={checkIsSelected(selectedProducts, product)}
          />)
          )
        }
      </div>
    </div>
  );
}