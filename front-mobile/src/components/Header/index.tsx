import { Image, StyleSheet, Text, View } from 'react-native';
import { TouchableWithoutFeedback } from 'react-native-gesture-handler';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../types/RootStackParamLis';
import React from 'react';

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;

export default function Header() {

    const navigation = useNavigation<HomeScreenNavigationProp>();

    const handleOnPress = () => {
        navigation.navigate('Home');
    }

    return (
        <>
            <TouchableWithoutFeedback onPress={handleOnPress}>
                <View style={styles.container}>
                    <Image source={require('../../assets/logo.png')} />
                    <Text style={styles.text}>Delivery!</Text>
                </View>
            </TouchableWithoutFeedback>
        </>

    );
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#DA5C5C',
        height: 90,
        paddingTop: 50,
        flexDirection: 'row',
        justifyContent: 'center'
    },
    text: {
        fontWeight: 'bold',
        fontSize: 18,
        lineHeight: 25,
        letterSpacing: -0.24,
        color: '#FFF',
        marginLeft: 15,
        fontFamily: 'OpenSans_700Bold'
    }
});
