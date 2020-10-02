package ufcg.so;

public class Philosopher implements Runnable {
    private Table table;
    private int number;
    private int REST_TIME = 3;
    private int EAT_TIME = 1;

    public Philosopher(int number, Table table) {
        this.number = number;
        this.table = table;
        new Thread(this).start();
    }

    public boolean getCutlery() {
        return this.table.getCutlery(this.number);
    }

    public void restCutlery() {
        this.table.putCutlery(this.number);
    }

    public void eat() {
        while (!this.getCutlery());
        try {
            System.out.println("Philosopher " + this.number + " is eating.");
            Thread.sleep(EAT_TIME * 1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrompida");
        }
        this.restCutlery();
        // System.out.println("Philosopher " + this.number + " rests now.");
    }

    public void think() {
        try {
            System.out.println("Philosopher " + this.number + " is thinking.");
            Thread.sleep(REST_TIME * 1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrompida");
        }
    }

    @Override
    public void run() {
        while (true) {
            think();
            eat();
        }
    }
}
