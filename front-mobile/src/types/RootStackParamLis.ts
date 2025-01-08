import { OrderDTO } from "./OrderDTO";

// Defina um tipo para as rotas
export type RootStackParamList = {
    Home: undefined;
    Orders: undefined; 
    OrderDetails: { order: OrderDTO }; 
};
