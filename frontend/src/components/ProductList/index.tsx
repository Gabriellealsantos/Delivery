import { ProductDTO } from '../../models/OrderDTO';
import ProductCard from '../ProductCard';
import './styles.css';

type Props = {
  products: ProductDTO[];
}

export default function ProductList({ products }: Props) {
  return (
    <div className='orders-list-container'>
      <div className='orders-list-items'>
        {
          products.map(product =>
            (<ProductCard key={product.id} product={product}/>)
          )
        }
      </div>
    </div>
  );
}