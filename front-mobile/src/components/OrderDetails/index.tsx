import { Alert, Image, Linking, StyleSheet, Text, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../types/RootStackParamLis';
import React from 'react';
import Header from '../Header';
import { OrderDTO } from '../../types/OrderDTO';
import OrderCard from '../OrderCard';
import { RectButton } from 'react-native-gesture-handler';
import { confirmDelivery } from '../../services/order-service';

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Orders'>;

type Props = {
    route: {
        params: {
            order: OrderDTO
        }
    }
}

export default function OrderDetails({ route }: Props) {

    const { order } = route.params;
    const navigation = useNavigation<HomeScreenNavigationProp>();


    const handleOnPress = () => {
        navigation.navigate('Orders');
    }

    const handleOnCancel = () => {
        navigation.navigate('Orders');
    }

    const handleConfirmDelivery = () => {
        confirmDelivery(order.id)
        .then(() => {
            Alert.alert(`Pedido ${order.id} confirmado com sucesso!`)
            navigation.navigate('Orders');
        })
        .catch(() => Alert.alert(`Houve um erro ao confirmar o pedido ${order.id}`))
    }
    
    const handleStartNavigation = () => {
        Linking.openURL(`https://www.google.com/maps/dir/?api=1&travelmode=driving&dir_action=navigate&destination=${order.latitude},${order.longitude}`)
    }



    return (
        <>
            <Header />
            <View style={styles.container} >
                <OrderCard order={order} />
                <RectButton style={styles.button}>
                    <Text style={styles.buttonText} onPress={handleStartNavigation}>INICIAR NAVEGAÇÃO</Text>
                </RectButton>
                <RectButton style={styles.button} onPress={handleConfirmDelivery}>
                    <Text style={styles.buttonText}>CONFIRMAR ENTREGA</Text>
                </RectButton>
                <RectButton style={styles.button} onPress={handleOnCancel}> 
                    <Text style={styles.buttonText}>CANCELAR</Text>
                </RectButton>
            </View>

        </>

    );
}

const styles = StyleSheet.create({
    container: {
        paddingRight: '5%',
        paddingLeft: '5%'
    },
    button: {
        backgroundColor: '#DA5C5C',
        flexDirection: 'row',
        borderRadius: 10,
        marginTop: 40,
        alignItems: 'center',
        justifyContent: 'center'
    },
    buttonText: {
        paddingTop: 15,
        paddingBottom: 15,
        paddingLeft: 50,
        paddingRight: 50,
        fontWeight: 'bold',
        fontSize: 18,
        color: '#FFF',
        letterSpacing: -0.24,
        fontFamily: 'OpenSans_700Bold'
    }
});
