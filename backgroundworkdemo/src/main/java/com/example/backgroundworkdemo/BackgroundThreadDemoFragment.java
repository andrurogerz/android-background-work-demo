package com.example.backgroundworkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BackgroundThreadDemoFragment extends ListFragment {

  private static final String TAG = SimpleThreadBackgroundWorker.class.getSimpleName();

  private BackgroundWorkerAdapter mListAdapter;
  private Handler mHandler;
  private BackgroundWorker.BackgroundWorkCallback mCallback;

  static private ArrayList<SimpleThreadBackgroundWorker> sBackgroundWorkers = new ArrayList<SimpleThreadBackgroundWorker>();
  static private int sLastWorkerId = 0;

  private class BackgroundWorkerAdapter extends ArrayAdapter<SimpleThreadBackgroundWorker> {

    public BackgroundWorkerAdapter() {
      super(getActivity(), R.layout.list_item_thread, sBackgroundWorkers);
      mCallback = new BackgroundWorker.BackgroundWorkCallback() {
        @Override
        public void onProgress(final Object context,
                               final BackgroundWorker worker) {
          final View view = (View)context;
          final ProgressBar progressBar =
              (ProgressBar)view.findViewById(R.id.list_item_thread_iterationProgressBar);
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              // Double check that the view is still associated with this worker.
              int value = worker.getProgress();
              if (view.getTag().equals(worker)) {
                progressBar.setProgress(value);
              }
              Log.d(TAG, worker + " iteration: " + value);
            }
          });

        }

        @Override
        public void onComplete(final Object context, final BackgroundWorker worker) {
          final View view = (View)context;
          final ProgressBar progressBar =
              (ProgressBar)view.findViewById(R.id.list_item_thread_iterationProgressBar);
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              // Double check that the view is still associated with this worker.
              if (view.getTag().equals(worker)) {
                progressBar.setProgress(worker.getIterations());
                Button stopButton = (Button)view.findViewById(R.id.list_item_thread_stopButton);
                stopButton.setEnabled(false);
              }
              Log.d(TAG, worker + " complete!");
            }
          });

        }
      };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_thread, null);
      }

      final BackgroundWorker worker = mListAdapter.getItem(position);

      TextView nameTextView = (TextView)convertView.findViewById(R.id.list_item_thread_nameTextView);
      nameTextView.setText(worker.getName());

      TextView priorityTextView =
          (TextView)convertView.findViewById(R.id.list_item_thread_priorityTextView);
      priorityTextView.setText(worker.getDescription());

      final ProgressBar progressBar =
          (ProgressBar)convertView.findViewById(R.id.list_item_thread_iterationProgressBar);
      progressBar.setMax(worker.getIterations());

      final Button stopButton = (Button)convertView.findViewById(R.id.list_item_thread_stopButton);
      if (worker.isRunning()) {
        progressBar.setProgress(worker.getProgress());
        stopButton.setEnabled(true);
        stopButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            worker.stop();
            stopButton.setEnabled(false);
            progressBar.setProgress(worker.getIterations());
          }
        });
      } else {
        progressBar.setProgress(worker.getIterations());
        stopButton.setEnabled(false);
      }

      convertView.setTag(worker);
      worker.setWorkCallback(convertView, mCallback);
      return convertView;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(R.string.app_name);
    mListAdapter = new BackgroundWorkerAdapter();
    setListAdapter(mListAdapter);
    mHandler = new Handler();
  }

  private void startWorker(ProcessThreadPriority priority) {
    String workerId = new Integer(++sLastWorkerId).toString();
    SimpleThreadBackgroundWorker worker = new SimpleThreadBackgroundWorker(workerId, priority);
    mListAdapter.add(worker);
    worker.start();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_background_thread_demo, container, false);

    Button button = (Button) view.findViewById(R.id.button_startBackgroundThread);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startWorker(ProcessThreadPriority.BACKGROUND);
      }
    });

    button = (Button) view.findViewById(R.id.button_startDefaultThread);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startWorker(ProcessThreadPriority.DEFAULT);
      }
    });

    button = (Button) view.findViewById(R.id.button_startForegroundThread);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startWorker(ProcessThreadPriority.FOREGROUND);
      }
    });

    return view;
  }
}
