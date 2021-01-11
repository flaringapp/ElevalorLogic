package com.flaringapp.elevator.strategy;

import com.flaringapp.elevator.Elevator;

public interface ElevatorStrategy {

    boolean hasWhereToGo(Elevator elevator);

    int resolveFloorToGo(Elevator elevator);

}
