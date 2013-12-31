package com.example.backgroundworkdemo.work;

import java.util.Iterator;

public class CpuIntensiveIterable implements Iterable<Integer> {

  private final int mIterations;
  private final long mIterationCost;

  public CpuIntensiveIterable(int iterations, long iterationCost) {
    mIterations = iterations;
    mIterationCost = iterationCost;
  }

  private void doIterationOfWork() {
    // naiively calculate all primes up to mIterationCost
    for (long n = 1; n <= mIterationCost; n++) {
      for (long i = 3; i < n; i += 1) {
        if (n % i == 0) {
          // found a divisor, i is not prime
          break;
        }
      }
    }

  }

  @Override
  public Iterator<Integer> iterator() {
    return new Iterator<Integer>() {

      private int mCurrent = 0;

      @Override
      public boolean hasNext() {
        return mCurrent < mIterations;
      }

      @Override
      public Integer next() {
        doIterationOfWork();
        return ++mCurrent;
      }

      @Override
      public void remove() {
      }
    };
  }
}
