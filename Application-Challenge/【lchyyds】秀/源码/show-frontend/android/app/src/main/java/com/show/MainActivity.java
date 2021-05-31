package com.show;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import org.devio.rn.splashscreen.SplashScreen;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "Show";
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Intent intent = new Intent("onConfigurationChanged");
    intent.putExtra("newConfig", newConfig);
    this.sendBroadcast(intent);
  }

  /**
   * 设置启动页
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SplashScreen.show(this); // 展示启动页设置代码
    super.onCreate(savedInstanceState);
  }
}
