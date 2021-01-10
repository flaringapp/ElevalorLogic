package com.flaringapp.floor;

import java.util.List;
import java.util.Queue;

public interface Floor {

    List<Queue<QueueConsumer>> getFloorQueues();

    Queue<QueueConsumer> getQueueAtElevator(int elevator);

    void enterQueue(QueueConsumer person);

    void leaveQueue(QueueConsumer person);

}
