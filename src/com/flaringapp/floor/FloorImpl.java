package com.flaringapp.floor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FloorImpl implements Floor {

    private final List<Queue<QueueConsumer>> queues;

    public FloorImpl(int elevatorsCount) {
        queues = new ArrayList<Queue<QueueConsumer>>() {{
            for (int i = 0; i < elevatorsCount; i++) {
                set(i, new ArrayDeque<>());
            }
        }};
    }

    @Override
    public List<Queue<QueueConsumer>> getFloorQueues() {
        return queues;
    }

    @Override
    public void enterQueue(QueueConsumer person, int queueIndex) {
        queues.get(queueIndex).add(person);
    }

    @Override
    public void leaveQueue(QueueConsumer person, int elevatorIndex) {
        Queue<QueueConsumer> queue = queues.get(elevatorIndex);
        if (queue.peek() != person) {
            throw new IllegalStateException("Person " + person + " tried to enter elevator " + elevatorIndex +
                    " without entering it's queue"
            );
        }
        queue.poll();
    }
}
