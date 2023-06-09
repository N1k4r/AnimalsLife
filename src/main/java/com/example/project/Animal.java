package com.example.project;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal{
    @Getter
    private final Type TYPE_ANIMAL;
    @Getter
    private int positionX;
    @Getter
    private int positionY;
    @Getter
    private final long ID;
    @Getter @Setter
    private boolean alive = true;
    @Getter @Setter
    private boolean readyForReproduce = true;
    private double kgBeforeFull;
    @Setter
    private int degreeOfHungry;

    public Animal(int positionX, int positionY, long ID, Type TYPE_ANIMAL) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.ID = ID;
        this.TYPE_ANIMAL = TYPE_ANIMAL;
        kgBeforeFull = TYPE_ANIMAL.getFoodKg();
        degreeOfHungry = TYPE_ANIMAL.getDegreeOfHungry();
    }

    public void eat(){
        Cell cell = Field.getField().getCell(positionX, positionY);
        List<Animal> animals = cell.getHashMapAnimalListOnCell().keySet().stream()
                .filter(x -> TYPE_ANIMAL.getFoodRation().containsKey(x))
                .flatMap(x -> cell.getHashMapAnimalListOnCell().get(x).stream())
                .toList();

        for (Animal animal : animals) {
            if (kgBeforeFull <= 0){
                setFull();
                return;
            }
            int probability = ThreadLocalRandom.current().nextInt(100);
            if (probability <= TYPE_ANIMAL.getFoodRation().get(animal.getTYPE_ANIMAL()) && animal.isAlive()) {
                FieldService.animalsDieOfEaten++;
                Field.getField().getAnimalsList().remove(animal);
                animal.setAlive(false);
                kgBeforeFull -= animal.getTYPE_ANIMAL().getWeight();
            }
        }
        if (kgBeforeFull > 0 && TYPE_ANIMAL.getFoodRation().containsKey(Type.PLANTS)){
            if (cell.getPlants() >= TYPE_ANIMAL.getFoodKg()){
                int poisonousPlant = ThreadLocalRandom.current().nextInt(100);
                if (poisonousPlant < 5){
                    alive = false;
                    FieldService.animalsDieOfPoisonousPlant++;
                    Field.getField().getAnimalsList().remove(this);
                    return;
                }
                cell.getPlants(TYPE_ANIMAL.getFoodKg());
                setFull();
                return;
            }
        }
        if (kgBeforeFull > 0 && --degreeOfHungry == 0){
            Field.getField().getAnimalsList().remove(this);
            alive = false;
            FieldService.animalsDieOfHungry++;
        }
    }

    private void setFull(){
        degreeOfHungry = TYPE_ANIMAL.getDegreeOfHungry();
        kgBeforeFull = TYPE_ANIMAL.getFoodKg();
    }

    public void move(){
        int step = TYPE_ANIMAL.getSpeed();
        positionX = ThreadLocalRandom.current().nextInt(Math.max(positionX - step, 0), Math.min(positionX + step, Config.FIELD_HEIGHT.getValue() - 1) + 1);
        positionY = ThreadLocalRandom.current().nextInt(Math.max(positionY - step, 0), Math.min(positionY + step, Config.FIELD_LENGTH.getValue() - 1) + 1);
    }

    public void reproduce(){
        HashMap<Type, List<Animal>> hashMapAnimalsList = Field.getField().getCell(positionX, positionY).getHashMapAnimalListOnCell();
        if (!hashMapAnimalsList.containsKey(TYPE_ANIMAL) || hashMapAnimalsList.get(TYPE_ANIMAL).size() >= TYPE_ANIMAL.getMaxPopulation())
            return;

        for (Animal animal : hashMapAnimalsList.get(TYPE_ANIMAL)){
            if (animal.getID() != ID && readyForReproduce && animal.isReadyForReproduce()){
                if (ThreadLocalRandom.current().nextInt(100) <= TYPE_ANIMAL.getReproducePercent()){
                    int childes = ThreadLocalRandom.current().nextInt(1, animal.getTYPE_ANIMAL().getMaxChildes() + 1);
                    for (int i = 0; i < childes; i++) {
                        Animal newAnimal = TYPE_ANIMAL.getNewAnimal(positionX, positionY, ThreadLocalRandom.current().nextLong());
                        newAnimal.setDegreeOfHungry(degreeOfHungry);
                        Field.getField().getAnimalsList().add(newAnimal);
                        FieldService.animalsGivenBirth++;
                    }
                    readyForReproduce = false;
                    animal.setReadyForReproduce(false);
                    return;
                }
            }
        }
    }
}
