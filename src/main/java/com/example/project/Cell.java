package com.example.project;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Cell implements Runnable{
    @Getter
    private final Type PLANTS = Type.PLANTS;
    @Getter
    private final HashMap<Type, List<Animal>> hashMapAnimalListOnCell = new HashMap<>();

    @SneakyThrows
    @Override
    public synchronized void run() {
        while(Field.getField().getANIMALS_LIST().size() != 0){
            move();
            eat();
            reproduce();
        }
    }

    private void move() {
        FieldService.action = "Move...";
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::move));
        waitAnotherThreadComplete();
    }

    private void eat() {
        FieldService.action = "Eat...";
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::eat));
        waitAnotherThreadComplete();
    }

    private void reproduce() {
        FieldService.action = "Reproduce...";
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(Animal::reproduce));
        hashMapAnimalListOnCell.forEach((key, value) -> value.forEach(x -> x.setReadyForReproduce(true)));
        waitAnotherThreadComplete();
    }

    public void clearCollectionsOnCell(){
        hashMapAnimalListOnCell.clear();
    }

    @SneakyThrows
    private void waitAnotherThreadComplete(){
        CountDownLatch.getCountDownLatch().countDown();
        synchronized (Controller.getController()){
            Controller.getController().wait();
        }
    }

    public String getGraphicsCell(){
        if (hashMapAnimalListOnCell.size() == 0)
            return PLANTS.getGraphics();
        return hashMapAnimalListOnCell.keySet().stream()
                .toList().get(ThreadLocalRandom.current().nextInt(hashMapAnimalListOnCell.size())).getGraphics();
    }

}
