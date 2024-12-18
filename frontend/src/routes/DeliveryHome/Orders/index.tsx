import ProductList from '../../../components/ProductList';
import StepsHeader from '../../../components/StepsHeader';
import './styles.css';

export default function Orders() {
    return (
        <div className='orders-container'>
            <StepsHeader />
            <ProductList />


        </div>
    );
}