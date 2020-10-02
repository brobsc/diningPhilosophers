package ufcg.so;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class Table {
    private Philosopher[] philosophers;
    private Semaphore mutex;

    private Philosopher findRightPhilosopher(int num) {
        return philosophers[Math.floorMod(--num, this.philosophers.length)];
    }

    private Philosopher findLeftPhilosopher(int num) {
        return philosophers[++num % this.philosophers.length];
    }

    public Table(int count) {
        this.philosophers = new Philosopher[count];
        this.mutex = new Semaphore(1);

        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(i, this);
        }

        while (true) {
            printStates();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printStates() {
        String states = Arrays.stream(philosophers)
                .map(Philosopher::toString)
                .collect(Collectors.joining("\n"));
        try {
            Thread.sleep(1000);
            clearScreen();
            System.out.println(states);
        } catch (InterruptedException e) {
            System.exit(1);
        }
    }

    public void getCutlery(Philosopher philosopher) {
        int philId = philosopher.getId();
        Philosopher leftPhilosopher = findLeftPhilosopher(philId);
        Philosopher rightPhilosopher = findRightPhilosopher(philId);

        try {
            this.mutex.acquire();
            philosopher.setStarving();

            if (leftPhilosopher.isNotEating() &&
                rightPhilosopher.isNotEating()) {
                philosopher.setEating();
                philosopher.useTurn();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt na thread: " + philId);
        }

        this.mutex.release();
        philosopher.awaitTurn();
    }

    public void putCutlery(Philosopher philosopher) {
        int philId = philosopher.getId();
        Philosopher leftPhilosopher = findLeftPhilosopher(philId);
        Philosopher rightPhilosopher = findRightPhilosopher(philId);

        try {
            this.mutex.acquire();
            philosopher.setThinking();

            if (leftPhilosopher.isStarving() &&
                    findLeftPhilosopher(leftPhilosopher.getId()).isNotEating()) {
                leftPhilosopher.setEating();
                leftPhilosopher.useTurn();
            }

            if (rightPhilosopher.isStarving() &&
                    findRightPhilosopher(rightPhilosopher.getId()).isNotEating()) {
                rightPhilosopher.setEating();
                rightPhilosopher.useTurn();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt na thread: " + philId);
        }

        this.mutex.release();
    }
}
