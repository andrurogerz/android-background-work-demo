package com.example.backgroundworkdemo.runnables;

public class PrioritizedRunnableWrapper implements Runnable {

  private final int mThreadPriority;
  private final Runnable mWrappedRunnable;

  public PrioritizedRunnableWrapper(int threadPriority, Runnable wrappedRunnable) {
    mThreadPriority = threadPriority;
    mWrappedRunnable = wrappedRunnable;
  }

  @Override
  public void run() {
    android.os.Process.setThreadPriority(mThreadPriority);
    mWrappedRunnable.run();
  }
}
