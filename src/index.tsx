import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-invoke-app' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnInvokeApp = NativeModules.RnInvokeApp
  ? NativeModules.RnInvokeApp
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );


export function invokeMyApp() {
  return RnInvokeApp.invokeApp();
}
