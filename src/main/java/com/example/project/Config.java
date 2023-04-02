package com.example.project;

import lombok.Getter;
import lombok.Setter;

public enum Config {
    CATERPILLAR(400.0),
    SHEEP(80.0),
    GOAT(90.0),
    MOUSE(350.0),
    HORSE(70.0),
    RABBIT(250.0),
    DEER(30.0),
    DUCK(110.0),
    HOG(60.0),
    BULL(10.0),
    WOLF(30.0),
    FOX(50.0),
    SNAKE(30.0),
    EAGLE(45.0),
    BEAR(15.0),
    FIELD_HEIGHT(10),
    FIELD_LENGTH(10),
    FIELD_SIZE(FIELD_HEIGHT.getValue() * FIELD_LENGTH.getValue());

    Config(int value) {
        this.value = value;
    }

    Config(double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    @Getter @Setter
    private int value;

    public void updateSize(){
        this.value = FIELD_LENGTH.value * FIELD_HEIGHT.value;
    }

    @Getter @Setter
    private double occupancyRate;
}
