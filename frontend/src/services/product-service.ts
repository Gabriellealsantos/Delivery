import { requestBackend } from "../utils/requests.ts";

export function fetchProduct() {
    return requestBackend({url: '/products'});
}

