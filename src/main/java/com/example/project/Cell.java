package com.example.project;

import lombok.Getter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Cell implements Runnable{
    @Getter
    private double plants = ThreadLocalRandom.current().nextInt(100, 200);
    @Getter
    private final HashMap<Type, List<Animal>> hashMapAnimalListOnCell = new HashMap<>();

    @Override
    public synchronized void run() {
        while(true){
            try {
                move();
                eat();
                reproduce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void move() throws InterruptedException {
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::move));
        waitAnotherThreadComplete();
    }

    private void eat() throws InterruptedException {
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::eat));
        waitAnotherThreadComplete();
    }

    private void reproduce() throws InterruptedException {
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::reproduce));
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(x -> x.setReadyForReproduce(true)));
        waitAnotherThreadComplete();
    }

    public void clearCollectionsOnCell(){
        hashMapAnimalListOnCell.clear();
    }

    public void getPlants(double weight){
        plants -= weight;
    }


    private void waitAnotherThreadComplete() throws InterruptedException {
        CountDownLatch.getCountDownLatch().countDown();
        synchronized (Controller.getController()){
            Controller.getController().wait();
        }
        plants = Math.min(plants + ThreadLocalRandom.current().nextInt(5,10), 200);
    }

    public String getGraphicsCell(){
        if (hashMapAnimalListOnCell.size() == 0)
            return Type.PLANTS.getGraphics();
        return hashMapAnimalListOnCell.keySet().stream()
                .toList().get(ThreadLocalRandom.current().nextInt(hashMapAnimalListOnCell.size())).getGraphics();
    }

}
