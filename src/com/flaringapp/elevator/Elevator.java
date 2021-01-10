package com.flaringapp.elevator;

public interface Elevator {

    int getCurrentFloor();

    boolean canEnter(ElevatorConsumer consumer);

    void enter(ElevatorConsumer consumer);
    void leave(ElevatorConsumer consumer);

}
