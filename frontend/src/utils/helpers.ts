import { ProductDTO } from "../models/OrderDTO";

export function checkIsSelected(selectedProducts: ProductDTO[], product: ProductDTO) {
    return selectedProducts.some(item => item.id === product.id);
}

export function formatPriece(price: number) {
    const formatter = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL',
        minimumFractionDigits: 2
    });
    return formatter.format(price);
}