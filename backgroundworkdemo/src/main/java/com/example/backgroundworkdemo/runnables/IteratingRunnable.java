package com.example.backgroundworkdemo.runnables;

import java.util.Iterator;

public class IteratingRunnable<T, C> implements Runnable {

  public interface IteratingRunnableCallback<T, C> {
    public void onIterationComplete(IteratingRunnable<T, C> runnable, T result);
    public void onComplete(IteratingRunnable<T, C> runnable);
  }

  private T mCurrent = null;
  private boolean mKeepRunning = true;

  private final Iterator<T> mIt;
  private final IteratingRunnableCallback<T, C> mCallback;
  private final C mContext;

  public IteratingRunnable(C context, Iterator<T> it, IteratingRunnableCallback<T, C> callback) {
    mContext = context;
    mIt = it;
    mCallback = callback;
  }

  public C getContext() {
    return mContext;
  }

  public T getCurrentValue() {
    return mCurrent;
  }

  public void stop() {
    mKeepRunning = false;
  }

  public boolean isRunning() {
    return mIt.hasNext() && mKeepRunning;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (o == null) {
      return false;
    }

    if (!(o instanceof IteratingRunnable)) {
      return false;
    }

    IteratingRunnable other = (IteratingRunnable) o;
    return other.mContext.equals(mContext);
  }

  @Override
  public int hashCode() {
    return mContext.hashCode();
  }

  @Override
  public String toString() {
    return getContext() + " : " + getCurrentValue();
  }

  @Override
  public void run() {
    while (isRunning()) {
      mCurrent = mIt.next();
      mCallback.onIterationComplete(this, mCurrent);
    }
    mCallback.onComplete(this);
  }
}
