/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState } from 'react';
import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet';
import AsyncSelect from 'react-select/async';
import { fetchLocalMapBox } from '../../services/map-service';
import { OrderLocationData } from '../../models/OrderLocationData';
import './styles.css';

const initialPosition = {
    lat: -12.7319202,
    lng: -39.1865126
};

type Place = {
    label: string;
    value: string;
    position: {
        lat: number;
        lng: number;
    };
};

type Props = {
    onChangeLocation: (location: OrderLocationData | undefined) => void;
}

export default function OrderLocation({ onChangeLocation }: Props) {
    const [address, setAddress] = useState<Place>({
        label: '',
        value: '',
        position: initialPosition
    });

    const loadOptions = async (inputValue: string): Promise<Place[]> => {
        try {
            const response = await fetchLocalMapBox(inputValue);

            return response.data.features.map((item: any) => ({
                label: item.place_name,
                value: item.place_name,
                position: {
                    lat: item.center[1],
                    lng: item.center[0]
                }
            }));
        } catch (error) {
            console.error('Erro ao buscar locais no Mapbox:', error);
            return [];
        }
    };

    const handleChangeSelect = (place: Place | null) => {
        setAddress(place ?? {
            label: '',
            value: '',
            position: initialPosition
        });

        if (place) {
            onChangeLocation({
                latitude: place.position.lat,
                longitude: place.position.lng,
                address: place.label!
            });
        } else {
            onChangeLocation(undefined);
        }
    };

    return (
        <div className='order-location-container'>
            <div className='order-location-content'>
                <h3 className='order-location-title'>Selecione onde o pedido deve ser entregue:</h3>
                <div className='filter-container'>
                    <AsyncSelect
                        placeholder='Digite um endereço para entregar o pedido'
                        className='filter'
                        loadOptions={loadOptions}
                        onChange={handleChangeSelect}
                        isClearable
                    />
                </div>
                <MapContainer
                    center={address.position}
                    zoom={13}
                    key={address.position.lat}
                    scrollWheelZoom={true}
                    style={{ height: '400px', width: '100%' }}
                >
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Marker position={address.position}>
                        <Popup>
                            Local selecionado: {address.label || 'Nenhum endereço selecionado'}
                        </Popup>
                    </Marker>
                </MapContainer>
            </div>
        </div>
    );
}
