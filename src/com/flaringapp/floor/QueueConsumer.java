package com.flaringapp.floor;

import com.flaringapp.elevator.Elevator;

public interface QueueConsumer {

    int elevatorIndex();

    void onQueueEntered();
    boolean enterElevator(Elevator elevator);

}
