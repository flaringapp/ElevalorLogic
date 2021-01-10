package com.flaringapp.elevator;

public interface ElevatorConsumer {

    float getWeight();

    int sourceFloor();
    int destinationFloor();

    void onEnteredElevator();
    void onLeftElevator();

}
