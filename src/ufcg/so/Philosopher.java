package ufcg.so;

import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    private Table table;
    private int id;
    private int REST_TIME = 1;
    private int EAT_TIME = 3;
    private Semaphore mutex;
    private PhilosopherState state;

    public Philosopher(int number, Table table) {
        this.id = number;
        this.table = table;
        this.state = PhilosopherState.THINKING;
        this.mutex = new Semaphore(0);
        new Thread(this).start();
    }

    public void getCutlery() {
        this.table.getCutlery(this);
    }

    public void restCutlery() {
        this.table.putCutlery(this);
    }

    public void eat() {
        try {
            this.getCutlery();
            Thread.sleep((long) (EAT_TIME + 3 * Math.random()) * 1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrompida");
        }
        this.restCutlery();
    }

    public void think() {
        try {
            Thread.sleep((long) (REST_TIME + 3 * Math.random()) * 1000);
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

    public int getId() {
        return this.id;
    }

    public void setThinking() {
        // System.out.println("Philosopher " + this.id + " is thinking.");
        this.state = PhilosopherState.THINKING;
    }

    public void setEating() {
        // System.out.println("Philosopher " + this.id + " is eating.");
        this.state = PhilosopherState.EATING;
    }

    public void setStarving() {
        // System.out.println("Philosopher " + this.id + " is starving.");
        this.state = PhilosopherState.STARVING;
    }

    public boolean isNotEating() {
        return this.state != PhilosopherState.EATING;
    }

    public boolean isStarving() {
        return this.state == PhilosopherState.STARVING;
    }

    public boolean isThinking() {
        return this.state == PhilosopherState.THINKING;
    }

    public boolean isEating() {
        return this.state == PhilosopherState.EATING;
    }

    public void awaitTurn() {
        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            System.out.println("Philosopher interrupted: " + this.id);
        }
    }

    public void useTurn() {
        this.mutex.release();
    }

    private String getColor() {
        String color = "\u001b[41m";
        if (this.isStarving()) {
            color = "\u001b[43m";
        } else if (this.isThinking()) {
            color = "\u001b[42m";
        }

        return color + "\u001b[30m";
    }

    @Override
    public String toString() {
        return this.getColor() + " " + this.id + " \u001b[0m";
    }
}
