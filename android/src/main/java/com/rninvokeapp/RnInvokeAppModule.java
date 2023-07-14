package com.rninvokeapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.List;

@ReactModule(name = RnInvokeAppModule.NAME)
public class RnInvokeAppModule extends ReactContextBaseJavaModule {
  public static final String NAME = "RnInvokeApp";
  private static ReactApplicationContext reactAppContext = null;
  private static Bundle bundle = null;

  public static final String LOG_TAG = "RNInvokeApp";

  public RnInvokeAppModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactAppContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }



  public static void sendEvent() {
    if (bundle != null) {
      reactAppContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit("appIsInvoked", Arguments.fromBundle(bundle));
      bundle = null;
    }
  }

  @ReactMethod
  public void invokeApp() {
    String packageName = reactAppContext.getPackageName();
    Intent launchIntent = reactAppContext.getPackageManager().getLaunchIntentForPackage(packageName);
    String className = launchIntent.getComponent().getClassName();

    try {
      Class<?> activityClass = Class.forName(className);
      Intent activityIntent = new Intent(reactAppContext, activityClass);

      activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
        .FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
      reactAppContext.startActivity(activityIntent);
    } catch(Exception e) {
      Log.e(LOG_TAG, "Class not found", e);
      return;
    }


    sendEvent();
  }


  private boolean isAppOnForeground(ReactApplicationContext context) {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
    if (appProcesses == null) {
      return false;
    }

    final String packageName = context.getPackageName();
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        && appProcess.processName.equals(packageName)) {
        return true;
      }
    }


    return false;
  }
}
