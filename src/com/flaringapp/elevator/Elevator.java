package com.flaringapp.elevator;

import java.util.List;

public interface Elevator {

    int getCurrentFloor();

    List<ElevatorConsumer> getConsumers();

    boolean canEnter(ElevatorConsumer consumer);

    void enter(ElevatorConsumer consumer);
    void leave(ElevatorConsumer consumer);

}
