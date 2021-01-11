package com.flaringapp.floor;

public interface QueueConsumer {

    int elevatorIndex();

    void onQueueEntered();

    void onQueueCompleted();

}
