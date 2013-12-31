package com.example.backgroundworkdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class BackgroundThreadDemoActivity extends FragmentActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_background_thread_demo);

    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = fragmentManager.findFragmentById(R.id.activity_background_thread_demo);
    if (fragment == null) {
      fragment = new BackgroundThreadDemoFragment();
      fragmentManager.beginTransaction()
          .add(R.id.activity_background_thread_demo, fragment)
          .commit();
    }
  }
}
