import React, { useEffect, useState } from 'react';
import { StyleSheet, ScrollView, Alert, Text } from 'react-native';
import Header from '../Header';
import OrderCard from '../OrderCard';
import { fetchOrders } from '../../services/order-service';
import { OrderDTO } from '../../types/OrderDTO';
import { useIsFocused, useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../types/RootStackParamLis';
import { TouchableWithoutFeedback } from 'react-native-gesture-handler';

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'OrderDetails'>;

export default function Orders() {

    const navigation = useNavigation<HomeScreenNavigationProp>();
    const [orders, setOrders] = useState<OrderDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const isFocused = useIsFocused();

    const fetchData = () => {
        setIsLoading(true);
        fetchOrders()
            .then(response => setOrders(response.data))
            .catch(() => Alert.alert('Não há mais pedidos para serem entregues.'))
            .finally(() => setIsLoading(false))
    }

    useEffect(() => {
        if (isFocused) {
            fetchData();
        }
    }, [isFocused])


    const handleOnPress = (order: OrderDTO) => {
        navigation.navigate('OrderDetails', { order });
    }

    return (
        <>
            <Header />
            <ScrollView style={styles.container}>
                {isLoading ? (
                    <Text>
                        Buscandos pedidos...
                    </Text>
                )
                    :
                    (
                        orders.map(order => (
                            <TouchableWithoutFeedback
                                key={order.id}
                                onPress={() => handleOnPress(order)}
                            >
                                <OrderCard order={order} />
                            </TouchableWithoutFeedback>
                        ))
                    )
                }
            </ScrollView>

        </>

    );
}

const styles = StyleSheet.create({
    container: {
        paddingRight: '5%',
        paddingLeft: '5%',
    }
});
