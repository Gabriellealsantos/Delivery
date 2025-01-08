import { AxiosRequestConfig } from "axios";
import { requestBackend } from "../utils/requests";

export function fetchOrders() {
    return requestBackend({ url: '/orders' });
}

export function confirmDelivery(orderId: number) {
    const config: AxiosRequestConfig = {
        method: "PUT",
        url: `/orders/${orderId}/delivered`,
    }
    return requestBackend(config)
}