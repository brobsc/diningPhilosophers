package ufcg.so;

public class Main {
    public static void main(String[] args) {
        int NUM = 5;
        System.out.println("Hello World!");

        Table table = new Table(NUM);

        for (int i = 0; i < NUM; i++) {
            new Philosopher(i, table);
        }
    }
}
