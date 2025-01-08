import { AxiosRequestConfig } from "axios";
import { requestBackend } from "../utils/requests";
import { OrderPayload } from "../models/OrderPayload";


export function saveOrder(paylod: OrderPayload) {
    const config: AxiosRequestConfig = {
        method: "POST",
        url: `/orders`,
        data: paylod
    }
    return requestBackend(config)

}