package com.flaringapp.elevator.strategy;

import com.flaringapp.elevator.Elevator;

public interface ElevatorStrategy {

    int resolveFloorToGo(Elevator elevator);

}
