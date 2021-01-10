package com.flaringapp.elevator.controller;

import com.flaringapp.person.Person;

import java.util.List;
import java.util.Queue;

public class ElevatorControllerImpl implements ElevatorController {

    private final List<Queue<Person>> floorQueues;

    public ElevatorControllerImpl(List<Queue<Person>> floorQueues) {
        this.floorQueues = floorQueues;
    }

    @Override
    public int calculateNextDestinationFloor() {
        return 0;
    }
}
