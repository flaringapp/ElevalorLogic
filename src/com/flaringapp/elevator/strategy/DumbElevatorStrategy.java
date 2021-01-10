package com.flaringapp.elevator.strategy;

import com.flaringapp.elevator.Elevator;

public class DumbElevatorStrategy implements ElevatorStrategy {

    @Override
    public int resolveFloorToGo(Elevator elevator) {
        if (elevator.getConsumers().isEmpty()) throw new IllegalStateException(
                "Cannot resolve elevator floor to go because there are no consumers"
        );
        return elevator.getConsumers().get(0).destinationFloor();
    }

}
