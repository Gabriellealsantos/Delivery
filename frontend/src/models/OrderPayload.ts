import { OrderLocationData } from "./OrderLocationData";
import { ProductId } from "./ProductId";

export type OrderPayload = {
    products: ProductId[];
} & OrderLocationData;