package com.example.backgroundworkdemo;

public interface BackgroundWorker {

  public interface BackgroundWorkCallback {
    public void onProgress(Object context, BackgroundWorker worker);
    public void onComplete(Object context, BackgroundWorker worker);
  }

  public void start();
  public void stop();
  public boolean isRunning();
  public void setWorkCallback(Object context, BackgroundWorkCallback callback);
  public int getIterations();
  public int getProgress();
  public String getName();
  public String getDescription();
}
