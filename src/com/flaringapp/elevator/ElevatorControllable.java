package com.flaringapp.elevator;

import com.flaringapp.elevator.strategy.ElevatorStrategy;

public interface ElevatorControllable extends Elevator {

    ElevatorStrategy getMovementStrategy();

    void setCurrentFloor(int floor);

}
