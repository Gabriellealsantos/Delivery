import { ProductDTO } from "./ProductDTO";

export type OrderDTO = {
    id: number,
    address: string,
    latitude: number,
    longitude: number,
    moment: string,
    status: string,
    total: number,
    products: ProductDTO[];
    
}