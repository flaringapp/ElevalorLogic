package com.flaringapp.floor;

import java.util.List;
import java.util.Queue;

public interface Floor {

    List<Queue<QueueConsumer>> getFloorQueues();

    void enterQueue(QueueConsumer person, int queueIndex);

    void leaveQueue(QueueConsumer person, int elevatorIndex);

}
