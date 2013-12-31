package com.example.backgroundworkdemo;

import com.example.backgroundworkdemo.work.CpuIntensiveIterable;
import com.example.backgroundworkdemo.runnables.IteratingRunnable;
import com.example.backgroundworkdemo.runnables.PrioritizedRunnableWrapper;

import java.util.Iterator;

public class SimpleThreadBackgroundWorker implements BackgroundWorker {

  private static final String TAG = SimpleThreadBackgroundWorker.class.getSimpleName();

  private static final int ITERATIONS = 100;
  private static final long WORKLOAD = 5000;

  private final String mName;
  private final ProcessThreadPriority mPriority;
  private final IteratingRunnable<Integer, SimpleThreadBackgroundWorker> mRunnable;

  private BackgroundWorkCallback mCallback;
  private Object mCallbackContext;

  public SimpleThreadBackgroundWorker(String name, ProcessThreadPriority priority) {
    mName = name;
    mPriority = priority;
    Iterator<Integer> it = new CpuIntensiveIterable(ITERATIONS, WORKLOAD).iterator();
    mRunnable = new IteratingRunnable<Integer, SimpleThreadBackgroundWorker>(this, it,
        new BackgroundThreadCallback());
  }

  @Override
  public void start() {
    Thread t = new Thread(new PrioritizedRunnableWrapper(mPriority.value(), mRunnable));
    t.setName(mName);
    t.start();
  }

  @Override
  public void stop() {
    mRunnable.stop();
  }

  @Override
  public int getIterations() {
    return ITERATIONS;
  }

  @Override
  public boolean isRunning() {
    return mRunnable.isRunning();
  }

  @Override
  public int getProgress() {
    Integer current = mRunnable.getCurrentValue();
    return current == null ? 0 : current;
  }

  @Override
  public String getName() {
    return mName;
  }

  @Override
  public String getDescription() {
    return mPriority.toString();
  }

  @Override
  public void setWorkCallback(Object context, BackgroundWorkCallback callback) {
    mCallbackContext = context;
    mCallback = callback;
  }

  private class BackgroundThreadCallback
      implements IteratingRunnable.IteratingRunnableCallback<Integer, SimpleThreadBackgroundWorker> {

    @Override
    public void onComplete(final IteratingRunnable<Integer, SimpleThreadBackgroundWorker> runnable) {
      final BackgroundWorkCallback callback = mCallback;
      if (callback != null) {
        mCallback.onComplete(mCallbackContext, SimpleThreadBackgroundWorker.this);
      }
    }

    @Override
    public void onIterationComplete(final IteratingRunnable<Integer,
        SimpleThreadBackgroundWorker> runnable, final Integer result) {
      final BackgroundWorkCallback callback = mCallback;
      if (callback != null) {
        mCallback.onProgress(mCallbackContext, SimpleThreadBackgroundWorker.this);
      }
    }

  }

  @Override
  public String toString() {
    return mName + ":" + mPriority.toString();
  }
}
