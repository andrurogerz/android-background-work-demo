  package com.example.backgroundworkdemo.tasks;

import android.os.AsyncTask;
import android.os.Build;

import com.example.backgroundworkdemo.BackgroundWorker;
import com.example.backgroundworkdemo.work.CpuIntensiveIterable;

import java.util.Iterator;

public class IteratingAsyncTask extends AsyncTask implements BackgroundWorker {

  private static final String TAG = IteratingAsyncTask.class.getSimpleName();

  private static final int ITERATIONS = 100;
  private static final long WORKLOAD = 5000;

  private final String mName;

  private Iterator<Integer> mIterator;
  private BackgroundWorkCallback mCallback;
  private Object mCallbackContext;
  private int mCurrent;

  public IteratingAsyncTask(String name) {
    mName = name;
  }

  @Override
  public void start() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    } else {
      this.execute();
    }
  }

  @Override
  public void stop() {
    this.cancel(true);
  }

  @Override
  public boolean isRunning() {
    return !this.isCancelled() && mIterator.hasNext();
  }

  @Override
  public void setWorkCallback(Object context, BackgroundWorkCallback callback) {
    mCallbackContext = context;
    mCallback = callback;
  }

  @Override
  public int getIterations() {
    return ITERATIONS;
  }

  @Override
  public int getProgress() {
    return mCurrent;
  }

  @Override
  public String getName() {
    return mName;
  }

  @Override
  public String getDescription() {
    return "AsyncTask";
  }

  @Override
  protected void onProgressUpdate(Object[] values) {
    if (mCallback != null) {
      mCallback.onProgress(mCallbackContext, this);
    }
  }

  @Override
  protected void onPostExecute(Object o) {
    if (mCallback != null) {
      mCallback.onComplete(mCallbackContext, this);
    }
  }

  @Override
  protected void onPreExecute() {
    mCurrent = 0;
    mIterator = new CpuIntensiveIterable(ITERATIONS, WORKLOAD).iterator();
  }

  @Override
  protected Object doInBackground(Object[] params) {
    while (isRunning()) {
      mCurrent = mIterator.next();
      this.onProgressUpdate(null);
    }
    return null;
  }
}
