import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import Header from './src/Components/Header';
import { useFonts, OpenSans_400Regular, OpenSans_700Bold } from '@expo-google-fonts/open-sans';
import Home from './src/Components/Home';
import { GestureHandlerRootView } from 'react-native-gesture-handler';


export default function App() {

  let [fontsLoaded] = useFonts({
    OpenSans_400Regular,
    OpenSans_700Bold
  });

  return (
    <View style={styles.container}>
      <StatusBar style="auto" />
      <Header />
      <GestureHandlerRootView style={{ flex: 1 }}>
        <Home />
      </GestureHandlerRootView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
