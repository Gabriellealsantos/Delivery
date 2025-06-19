import './styles.css';

type Props = {
    amount: number,
    totalPrice: string,
    onSubmit: () => void,
}

export default function OrderSummary({ amount, totalPrice, onSubmit }: Props) {
    return (
        <div className='order-summary-container'>
            <div className='order-summary-content'>
                <div>
                    <span className='amount-selected-container'>
                        <strong className='amount-selected'>{amount}</strong>
                        PEDIDOS SELECIONADOS
                    </span>
                    <span className='order-summary-total'>
                        <strong>R$ {totalPrice}</strong>
                    </span>
                </div>
                <button 
                    className='order-summary-make-order'
                    onClick={onSubmit}
                    disabled={false}
                >
                    FAZER PEDIDO
                </button>
            </div>
        </div>
    );
}