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
    public Queue<QueueConsumer> getQueueAtFloor(int floor) {
        return queues.get(floor);
    }

    @Override
    public void enterQueue(QueueConsumer person) {
        queues.get(person.elevatorIndex()).add(person);
    }

    @Override
    public void leaveQueue(QueueConsumer person) {
        Queue<QueueConsumer> queue = queues.get(person.elevatorIndex());
        if (queue.peek() != person) {
            throw new IllegalStateException("Person " + person + " tried to enter elevator " + person.elevatorIndex() +
                    " without entering it's queue"
            );
        }
        queue.poll();
    }
}
