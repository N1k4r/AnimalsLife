package com.example.project;
public class FieldService {
    public static int animalsDieOfHungry = 0;
    public static int animalsDieOfEaten = 0;
    public static int animalsGivenBirth = 0;
    public static int animalsDieOfPoisonousPlant = 0;

    public static void clear(){
        animalsDieOfHungry = 0;
        animalsDieOfEaten = 0;
        animalsGivenBirth = 0;
        animalsDieOfPoisonousPlant = 0;
    }
}
