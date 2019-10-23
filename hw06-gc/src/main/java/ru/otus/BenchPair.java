package ru.otus;

public class BenchPair {
    private long times;
    private long gcTime;

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getGcTime() {
        return gcTime;
    }

    public void setGcTime(long gcTime) {
        this.gcTime = gcTime;
    }
}
