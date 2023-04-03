package com.example.project;

import com.example.project.animals.herbivores.*;
import com.example.project.animals.predators.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

public enum Type {
    PLANTS("\uD83C\uDF31"),
    CATERPILLAR(0, 0.01, 0, 5,60, 1000, 1,
            Config.CATERPILLAR.getOccupancyRate(), "\uD83D\uDC1B",
            Map.of(PLANTS,100)),
    SHEEP(3, 50, 4, 15,4,140, 2,
            Config.SHEEP.getOccupancyRate(), "\uD83D\uDC11",
            Map.of(PLANTS,100)),
    GOAT(3,60,10, 20, 4, 140, 2,
            Config.GOAT.getOccupancyRate(), "\uD83D\uDC10",
            Map.of(PLANTS,100)),
    MOUSE(1, 0.05, 0.01, 20,8,500, 1,
            Config.MOUSE.getOccupancyRate(), "\uD83D\uDC01",
            Map.of(CATERPILLAR,90, PLANTS,100)),
    HORSE(4, 400, 60, 80,2,20, 2,
            Config.HORSE.getOccupancyRate(), "\uD83D\uDC0E",
            Map.of(PLANTS,100)),
    RABBIT(2, 2, 0.45, 25,5,150, 1,
            Config.RABBIT.getOccupancyRate(), "\uD83D\uDC07",
            Map.of(PLANTS,100)),
    DEER(4,300,50,50,3,20,2,
            Config.DEER.getOccupancyRate(), "\uD83E\uDD8C",
            Map.of(PLANTS,100)),
    DUCK(4,1,0.15,10,10,200, 1,
            Config.DUCK.getOccupancyRate(), "\uD83E\uDD86",
            Map.of(PLANTS,100, CATERPILLAR, 90)),
    HOG(2,400,50,25,4,50,2,
            Config.HOG.getOccupancyRate(), "\uD83D\uDC17",
            Map.of(MOUSE,50, CATERPILLAR,90, PLANTS,100)),
    BULL(3,700,100,30,2,10,3,
            Config.BULL.getOccupancyRate(), "\uD83D\uDC03",
            Map.of(PLANTS,100)),
    WOLF(3, 60, 8, 60,8,30, 2,
            Config.WOLF.getOccupancyRate(), "\uD83D\uDC3A",
            Map.of(SHEEP,70, MOUSE,80, RABBIT,60, HORSE,10, GOAT,60, DUCK,40, DEER,15, BULL,10,HOG,15)),
    FOX(2, 8, 2, 70,10,30, 2,
            Config.FOX.getOccupancyRate(), "\uD83E\uDD8A",
            Map.of(RABBIT, 70, MOUSE, 90, DUCK,60,CATERPILLAR,40)),
    SNAKE(1,15,3, 50, 12, 30, 2,
            Config.SNAKE.getOccupancyRate(), "\uD83D\uDC0D",
            Map.of(FOX,15, MOUSE,40, RABBIT,20, DUCK,10)),
    EAGLE(3,6,1,55,5,20,2,
            Config.EAGLE.getOccupancyRate(),"\uD83E\uDD85",
            Map.of(MOUSE,90, RABBIT,90, DUCK,80, FOX,10)),
    BEAR(2,500,80,30,3,5,4,
            Config.BEAR.getOccupancyRate(),"\uD83D\uDC3B",
            Map.of(MOUSE,90, RABBIT,80, SHEEP,70, GOAT,70, HORSE,40, DUCK,10, SNAKE,80, BULL,20, HOG,50, DEER,80))
    ;
    Type(int speed, double weight, double foodKg, int reproducePercent, int maxChildes, int maxPopulation,
         int degreeOfHungry, double occupancy, String graphics, Map<Type, Integer> foodRation) {

        this.speed = speed;
        this.weight = weight;
        this.foodKg = foodKg;
        this.reproducePercent = reproducePercent;
        this.maxChildes = maxChildes;
        this.maxPopulation = maxPopulation;
        this.degreeOfHungry = degreeOfHungry;
        this.occupancy = occupancy;
        this.graphics = graphics;
        this.foodRation = foodRation;
    }
    Type(String graphics) {
        this.graphics = graphics;
    }
    @Getter
    private int speed;
    @Getter
    private double weight;
    @Getter
    private double foodKg;
    @Getter
    private int reproducePercent;
    @Getter
    private int maxChildes;
    @Getter
    private int maxPopulation;
    @Getter
    private int degreeOfHungry;
    @Getter @Setter
    private double occupancy;
    @Getter
    private Map<Type, Integer> foodRation;
    @Getter
    private final String graphics;

    public Animal getNewAnimal(int positionX, int positionY, long id){
        return switch (this){
            case PLANTS -> null;
            case SHEEP -> new Sheep(positionX, positionY, id, this);
            case GOAT -> new Goat(positionX, positionY, id, this);
            case MOUSE -> new Mouse(positionX, positionY, id, this);
            case HORSE -> new Horse(positionX, positionY, id, this);
            case RABBIT -> new Rabbit(positionX, positionY, id, this);
            case CATERPILLAR -> new Caterpillar(positionX, positionY, id, this);
            case DEER -> new Deer(positionX, positionY, id, this);
            case HOG -> new Hog(positionX, positionY, id, this);
            case BULL -> new Bull(positionX, positionY, id, this);
            case DUCK -> new Duck(positionX, positionY, id, this);
            case WOLF -> new Wolf(positionX, positionY, id, this);
            case FOX -> new Fox(positionX, positionY, id, this);
            case SNAKE -> new Snake(positionX, positionY, id, this);
            case EAGLE -> new Eagle(positionX, positionY, id, this);
            case BEAR -> new Bear(positionX, positionY, id, this);
        };
    }


}
