import { useEffect, useState } from 'react';
import ProductList from '../../../components/ProductList';
import StepsHeader from '../../../components/StepsHeader';
import { ProductDTO } from '../../../models/OrderDTO';
import { fetchProduct } from '../../../services/product-service';
import OrderLocation from '../OrderLocation';
import './styles.css';

export default function Orders() {

    const [products, setProducts] = useState<ProductDTO[]>([]);

    useEffect(() => {
        fetchProduct()
            .then(response => setProducts(response.data))
            .catch(error => console.log(error))
    }, [])

    return (
        <div className='orders-container'>
            <StepsHeader />
            <ProductList products={products} />
            <OrderLocation />
        </div>
    );
}