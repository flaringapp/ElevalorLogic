package com.flaringapp.elevator.strategy;

import com.flaringapp.elevator.ElevatorConsumer;

import java.util.List;

public class DumbElevatorStrategy implements ElevatorStrategy {

    @Override
    public int resolveFloorToGo(List<ElevatorConsumer> consumers) {
        if (consumers.isEmpty()) throw new IllegalStateException(
            "Cannot resolve elevator floor to go because there are no consumers"
        );
        return consumers.get(0).destinationFloor();
    }
}
