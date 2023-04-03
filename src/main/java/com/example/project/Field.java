package com.example.project;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

public class Field {
    private static Field field;
    private static int height;
    private int length;
    private Cell[][] cells;
    @Getter
    private List<Animal> animalsList;
    @Getter
    private ThreadGroup threadGroup;

    private Field(){}

    public static Field getField(){
        if (field == null)
            field = new Field();

        return field;
    }

    @SneakyThrows
    public void initializationField(){
        threadGroup = new ThreadGroup("group");
        height = Config.FIELD_HEIGHT.getValue();
        length = Config.FIELD_LENGTH.getValue();
        animalsList = Collections.synchronizedList(new ArrayList<>());
        cells = new Cell[height][length];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++)
                cells[i][j] = new Cell();
        }

        Type[] type = Type.values();
        for (int i = 1; i < type.length; i++) {
            for (int j = 0; j < (Config.FIELD_SIZE.getValue() * type[i].getOccupancy()) / 100; j++)
                animalsList.add(type[i].getNewAnimal(new Random().nextInt(height), new Random().nextInt(length), new Random().nextLong()));
        }
        updateAnimalsOnCells();
        synchronized (this){
            notify();
        }
    }

    public Cell getCell(int positionX, int positionY){
        return cells[positionX][positionY];
    }

    public String getNumberOfAnimals() {
        return String.valueOf(animalsList.size());
    }

    public String getGraphicsField(){
        StringBuilder graphicsCell = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                graphicsCell.append(cells[i][j].getGraphicsCell()).append("\t");
                cells[i][j].clearCollectionsOnCell();
            }
            graphicsCell.append("\n");
        }
        return graphicsCell.toString();
    }

    public String getTypePopulation(){
        StringBuilder builder = new StringBuilder();
        Field.getField().getAnimalsList().stream()
                .collect(Collectors.groupingBy(Animal::getTYPE_ANIMAL, Collectors.counting()))
                .forEach((key, value) -> builder.append(key.name()).append(" - ").append(value).append("\n"));
        return builder.toString();
    }

    public String getStatistic(){
        StringBuilder builder = new StringBuilder();
        if (FieldService.animalsDieOfEaten > 0)
            builder.append("Died of being eaten - ").append(FieldService.animalsDieOfEaten).append("\n");

        if (FieldService.animalsDieOfHungry > 0)
            builder.append("Died of hungry - ").append(FieldService.animalsDieOfHungry).append("\n");

        if (FieldService.animalsGivenBirth > 0)
            builder.append("Given birth - ").append(FieldService.animalsGivenBirth).append("\n");

        if (FieldService.animalsDieOfPoisonousPlant > 0)
            builder.append("Died of a poisonous plant - ").append(FieldService.animalsDieOfPoisonousPlant).append("\n");

        FieldService.clear();
        return builder.toString();
    }

    public void updateAnimalsOnCells(){
        animalsList.forEach(x -> {
            var hashMapAnimalListOnCell = cells[x.getPositionX()][x.getPositionY()].getHashMapAnimalListOnCell();
            if (!hashMapAnimalListOnCell.containsKey(x.getTYPE_ANIMAL())){
                hashMapAnimalListOnCell.put(x.getTYPE_ANIMAL(), new ArrayList<>());
                hashMapAnimalListOnCell.get(x.getTYPE_ANIMAL()).add(x);
            } else
                hashMapAnimalListOnCell.get(x.getTYPE_ANIMAL()).add(x);
        });
    }

    public void runThreads(){
        for (int i = 0; i < height; i++)
            for (int j = 0; j < length; j++){
                Thread thread = new Thread(threadGroup, cells[i][j]);
                thread.start();
            }
    }
}
