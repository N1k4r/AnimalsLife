package com.example.project;

import lombok.Getter;
import lombok.Setter;

public enum Config {
    CATERPILLAR(100.0),
    SHEEP(70.0),
    GOAT(65.0),
    MOUSE(85.0),
    HORSE(50.0),
    RABBIT(90.0),
    DEER(30.0),
    DUCK(85.0),
    HOG(40.0),
    BULL(10.0),
    WOLF(20.0),
    FOX(30.0),
    SNAKE(15.0),
    EAGLE(45.0),
    BEAR(10.0),
    FIELD_HEIGHT(10),
    FIELD_LENGTH(10),
    FIELD_SIZE(FIELD_HEIGHT.getValue() * FIELD_LENGTH.getValue());

    @Getter @Setter
    private int value;

    @Getter
    private double occupancyRate;

    Config(int value) {
        this.value = value;
    }

    Config(double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public void updateSize(){
        this.value = FIELD_LENGTH.value * FIELD_HEIGHT.value;
    }
}
