package com.flaringapp.elevator;

public interface ElevatorCallbacks {

    void onElevatorStartedMovement(Elevator elevator);
    void onElevatorCompletedMovement(Elevator elevator);

}
