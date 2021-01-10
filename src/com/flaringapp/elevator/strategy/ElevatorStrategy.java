package com.flaringapp.elevator.strategy;

import com.flaringapp.elevator.ElevatorConsumer;

import java.util.List;

public interface ElevatorStrategy {

    abstract int resolveFloorToGo(List<ElevatorConsumer> consumers);

}
