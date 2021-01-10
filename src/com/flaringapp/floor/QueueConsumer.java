package com.flaringapp.floor;

import com.flaringapp.elevator.ElevatorConsumer;

public interface QueueConsumer extends ElevatorConsumer {

    void onEnteredQueue(int queue);
    void onLeftQueue(int queue);

}
