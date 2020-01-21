package ru.otus;

public class Main {
    private final Object monitor = new Object();
    private int count = 0;
    private boolean firstWrote = false;
    private boolean isUp = true;

    public static void main(String[] args) throws InterruptedException {
        Main counter = new Main();
        counter.go();
    }

    private void write() {
        while (!Thread.currentThread().isInterrupted())
            synchronized (monitor) {
                if (!firstWrote && Thread.currentThread().getName().equals("t1")) {
                    if (count == 10)
                        isUp = false;
                    if (count == 1)
                        isUp = true;
                    if (isUp)
                        count++;
                    else
                        count--;
                    firstWrote = true;
                    System.out.println("Thread " + Thread.currentThread().getName() + ": write " + count);
                }
                if (firstWrote && Thread.currentThread().getName().equals("t2")) {
                    firstWrote = false;
                    System.out.println("Thread " + Thread.currentThread().getName() + ": write " + count);
                }
                /*try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/ //Можно использовать для более просто просмотра лога
            }
    }

    private void go() {
        Thread thread1 = new Thread(this::write);
        Thread thread2 = new Thread(this::write);

        thread1.setName("t1");
        thread2.setName("t2");

        thread1.start();
        thread2.start();
    }
}
