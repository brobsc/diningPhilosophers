package ufcg.so;

import java.util.concurrent.Semaphore;

public class Table {
    private boolean[] cutleries;
    private Semaphore mutex;

    private int findLeftCutlery(int num) {
        return num % this.cutleries.length;
    }

    private int findRightCutlery(int num) {
        return ++num % this.cutleries.length;
    }

    public Table(int count) {
        // cutlery = true = busy
        // cutlery = false = available
        this.cutleries = new boolean[count];
        this.mutex = new Semaphore(1);
    }

    public boolean getCutlery(int philId) {
        boolean got = false;
        try {
            this.mutex.acquire();
            int leftIndex = findLeftCutlery(philId);
            int rightIndex = findRightCutlery(philId);

            if (!cutleries[leftIndex] &&
                !cutleries[rightIndex]) {

                cutleries[leftIndex] = true;
                cutleries[rightIndex] = true;
                got = true;
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt na thread: " + philId);
        }

        this.mutex.release();
        return got;
    }

    public void putCutlery(int philId) {
        try {
            this.mutex.acquire();
            int leftIndex = findLeftCutlery(philId);
            int rightIndex = findRightCutlery(philId);

            cutleries[leftIndex] = false;
            cutleries[rightIndex] = false;
        } catch (InterruptedException e) {
            System.out.println("Interrupt na thread: " + philId);
        }

        this.mutex.release();
    }
}
