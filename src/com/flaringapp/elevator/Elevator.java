package com.flaringapp.elevator;

import java.util.List;
import java.util.Set;

public interface Elevator {

    int getCurrentFloor();

    Set<Integer> getCalledFloors();
    void callAtFloor(int floor);

    List<ElevatorConsumer> getConsumers();

    boolean canEnter(ElevatorConsumer consumer);
    void enter(ElevatorConsumer consumer);

    void leave(ElevatorConsumer consumer);

    boolean goesUpstairs();

}
