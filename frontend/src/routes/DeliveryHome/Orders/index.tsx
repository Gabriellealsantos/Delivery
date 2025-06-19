import { useEffect, useState } from 'react';
import ProductList from '../../../components/ProductList';
import StepsHeader from '../../../components/StepsHeader';
import { ProductDTO } from '../../../models/OrderDTO';
import { fetchProduct } from '../../../services/product-service';
import OrderLocation from '../../../components/OrderLocation';
import { OrderLocationData } from '../../../models/OrderLocationData';
import OrderSummary from '../../../components/OrderSummary';
import Footer from '../../../components/Footer';
import { checkIsSelected, formatPriece } from '../../../utils/helpers';
import { saveOrder } from '../../../services/order-service';
import { toast } from 'react-toastify';
import './styles.css';

export default function Orders() {

    const [products, setProducts] = useState<ProductDTO[]>([]);
    const [selectedProducts, setSelectedProducts] = useState<ProductDTO[]>([]);
    const [orderLocation, setOrderLocation] = useState<OrderLocationData | undefined>();
    const totalPrice = selectedProducts.reduce((sum, item) => {
        return sum + item.price
    }, 0)

    useEffect(() => {
        fetchProduct()
            .then(response => setProducts(response.data))
            .catch(() => toast.warning('Erro ao listar produtos'))
    }, [])

    const handleSelectProduct = (product: ProductDTO) => {
        const isAlreadySelected = checkIsSelected(selectedProducts, product);

        if (isAlreadySelected) {
            const selected = selectedProducts.filter(item => item.id !== product.id);
            setSelectedProducts(selected);
        } else {
            setSelectedProducts(previous => [...previous, product]);
        }
    }

    const handleSubmit = () => {
        if (!orderLocation) {
            toast.warning('Por favor, selecione o endereço de entrega.');
            return;
        }
        const productsIds = selectedProducts.map(({ id }) => ({ id }));
        const payload = {
            ...orderLocation,
            products: productsIds
        }

        saveOrder(payload).then((response) => {
            toast.success(`Pedido enviado com sucesso! Nº ${response.data.id}`);
            setSelectedProducts([]);
        }).catch(() => {
                toast.warning('Erro ao enviar pedido');
            })
    }

    return (
        <>
            <div className='orders-container'>
                <StepsHeader />
                <ProductList
                    products={products}
                    onSelectProduct={handleSelectProduct}
                    selectedProducts={selectedProducts}
                />
                <OrderLocation onChangeLocation={location => setOrderLocation(location)} />
                <OrderSummary
                    amount={selectedProducts.length}
                    totalPrice={formatPriece(totalPrice)}
                    onSubmit={handleSubmit}
                />
            </div>
            <Footer />
        </>
    );
}