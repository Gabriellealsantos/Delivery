import { requestAPIMapBox, requestBackend } from "../utils/requests.ts";
import { MAP_BOX_TOKEN } from "../utils/system";

export function fetchProduct() {
    return requestBackend({url: '/products'});
}

export function fetchLocalMapBox(local: string) {
    const url = `https://api.mapbox.com/geocoding/v5/mapbox.places/${local}.json?access_token=${MAP_BOX_TOKEN}`;
    return requestAPIMapBox(url);
}
