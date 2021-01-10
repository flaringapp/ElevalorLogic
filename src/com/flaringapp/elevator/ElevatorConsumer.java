package com.flaringapp.elevator;

public interface ElevatorConsumer extends ElevatorCallbacks {

    float getWeight();

    int sourceFloor();
    int destinationFloor();

}
