import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Home from "../components/Home";
import Orders from "../components/Orders";
import OrderDetails from "../components/OrderDetails";

const Stack = createStackNavigator();

export default function Routes() {
    return (
        <NavigationContainer>
            <Stack.Navigator
                screenOptions={{
                    cardStyle: {
                        backgroundColor: '#FFF',
                    },
                    headerShown: false,
                }}
            >
                <Stack.Screen name="Home" component={Home} />
                <Stack.Screen name="Orders" component={Orders} />
                <Stack.Screen name="OrderDetails" component={OrderDetails} />

            </Stack.Navigator>
        </NavigationContainer>
    )
}