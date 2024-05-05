package com.example.mlseriesdemonstrator.database;

public class Person {
    public String name;
    public float[] faceVector;

    public Person(String name, float[] faceVector) {
        this.name = name;
        this.faceVector = faceVector;
    }
}
