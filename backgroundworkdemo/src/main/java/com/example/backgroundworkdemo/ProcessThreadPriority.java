package com.example.backgroundworkdemo;

import android.os.Process;

public enum ProcessThreadPriority {

  BACKGROUND(Process.THREAD_PRIORITY_BACKGROUND),
  DEFAULT(Process.THREAD_PRIORITY_DEFAULT),
  FOREGROUND(Process.THREAD_PRIORITY_FOREGROUND);

  private final int mPriority;

  public int value() {
    return mPriority;
  }

  ProcessThreadPriority(int priority) {
    mPriority = priority;
  }
}
