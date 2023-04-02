package com.example.project;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Field {
    private static Field field;
    private static final int HEIGHT = Config.FIELD_HEIGHT.getValue();
    private final int LENGTH = Config.FIELD_LENGTH.getValue();
    private final Cell[][] cells = new Cell[HEIGHT][LENGTH];
    @Getter
    private final List<Animal> ANIMALS_LIST = new ArrayList<>();

    private Field(){}

    public static Field getField(){
        if (field == null)
            field = new Field();

        return field;
    }

    public void setAnimalsOnField(){
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++)
                cells[i][j] = new Cell();
        }

        Type[] type = Type.values();
        Config[] config = Config.values();
        for (int i = 1; i < type.length; i++) {
            for (int j = 0; j < (Config.FIELD_SIZE.getValue() * config[i-1].getOccupancyRate()) / 100; j++)
                ANIMALS_LIST.add(type[i].getNewAnimal(new Random().nextInt(HEIGHT), new Random().nextInt(LENGTH), new Random().nextLong()));
        }
        updateAnimalsOnCells();
    }

    public Cell getCell(int positionX, int positionY){
        return cells[positionX][positionY];
    }

    public String getNumberOfAnimals() {
        return String.valueOf(ANIMALS_LIST.size());
    }

    public String getGraphicsField(){
        StringBuilder graphicsCell = new StringBuilder();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                graphicsCell.append(cells[i][j].getGraphicsCell()).append("\t");
                cells[i][j].clearCollectionsOnCell();
            }
            graphicsCell.append("\n");
        }
        return graphicsCell.toString();
    }

    public String getTypePopulation(){
        StringBuilder builder = new StringBuilder();
        Field.getField().getANIMALS_LIST().stream()
                .collect(Collectors.groupingBy(Animal::getTYPE_ANIMAL, Collectors.counting()))
                .forEach((key, value) -> builder.append(key.name()).append(" - ").append(value).append("\n"));
        return builder.toString();
    }

    public String getStatistic(){
        StringBuilder builder = new StringBuilder();
        if (FieldService.animalsDieOfEaten > 0)
            builder.append("Died from being eaten - ").append(FieldService.animalsDieOfEaten).append("\n");

        if (FieldService.animalsDieOfHungry > 0)
            builder.append("Died from hungry - ").append(FieldService.animalsDieOfHungry);

        if (FieldService.animalsGivenBirth > 0)
            builder.append("Given birth - ").append(FieldService.animalsGivenBirth);

        FieldService.clear();
        return builder.toString();
    }

    public void updateAnimalsOnCells(){
        ANIMALS_LIST.removeIf(Objects::isNull);

        ANIMALS_LIST.forEach(x -> {
            var hashMapAnimalListOnCell = cells[x.getPositionX()][x.getPositionY()].getHashMapAnimalListOnCell();
            if (!hashMapAnimalListOnCell.containsKey(x.getTYPE_ANIMAL())){
                hashMapAnimalListOnCell.put(x.getTYPE_ANIMAL(), new ArrayList<>());
                hashMapAnimalListOnCell.get(x.getTYPE_ANIMAL()).add(x);
            } else
                hashMapAnimalListOnCell.get(x.getTYPE_ANIMAL()).add(x);
        });
    }

    public void runThreads(){
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < LENGTH; j++)
                new Thread(cells[i][j]).start();
    }
}
