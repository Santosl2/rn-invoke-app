import * as React from 'react';

import { StyleSheet, View, Text, AppState } from 'react-native';
import { invokeMyApp } from 'rn-invoke-app';

export default function App() {

  React.useEffect(() => {

    const listenerRemove = AppState.addEventListener('change', (state) => {
      if (state === 'background') {
        console.log('App is in Background Mode.');
        invokeMyApp();
      }
    });

    return () => {
      listenerRemove.remove();
    }

  }, []);

  return (
    <View style={styles.container}>
      <Text>Put your app in background to see the magic.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
