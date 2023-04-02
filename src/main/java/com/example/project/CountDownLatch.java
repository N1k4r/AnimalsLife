package com.example.project;

public class CountDownLatch {
    private static CountDownLatch countDownLatch;
    private int count;
    private CountDownLatch(){
        updateValue();
    }

    public static CountDownLatch getCountDownLatch(){
        if (countDownLatch == null){
            countDownLatch = new CountDownLatch();
        }
        return countDownLatch;
    }

    public synchronized void countDown(){
        count--;
        if (count == 0){
            updateValue();
            synchronized (this){
                notify();
            }
        }
    }

    public void updateValue(){
        count = Config.FIELD_SIZE.getValue();
    }
}
