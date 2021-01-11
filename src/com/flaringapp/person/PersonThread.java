package com.flaringapp.person;

import com.flaringapp.building.Building;
import com.flaringapp.elevator.Elevator;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class PersonThread extends Thread implements Person, PersonLifecycle {

    private final Person person;

    private final Building building;

    private final int fromFloor;
    private final int toFloor;

    private boolean reachedDestination = false;

    public PersonThread(Person person, Building building, int fromFloor, int toFloor) {
        this.person = person;
        this.building = building;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
    }

    @Override
    public void run() {
        enterSmallestQueue();

        waitForReachingTargetFloor();
    }

    @Override
    public String getPersonName() {
        return person.getPersonName();
    }

    @Override
    public float getWeight() {
        return person.getWeight();
    }

    @Override
    public void onEnteredQueue(int queue) {
    }

    @Override
    public void onLeftQueue(int queue) {
    }

    @Override
    public void onEnteredElevator(Elevator elevator) {
    }

    @Override
    public void onLeftElevator(Elevator elevator) {
        reachedDestination = true;
        notify();
    }

    private void enterSmallestQueue() {
        Floor floor = building.getFloors().get(fromFloor);
        int smallestQueueIndex = resolveRandomSmallestQueueIndex(floor);

        PersonInBuilding personInBuilding = new PersonInBuilding(this, fromFloor, toFloor, smallestQueueIndex, this);
        building.enterQueue(personInBuilding);
    }

    private int resolveRandomSmallestQueueIndex(Floor floor) {
        List<Queue<QueueConsumer>> queues = floor.getFloorQueues();

        int smallestQueueLength = Integer.MAX_VALUE;
        List<Integer> smallestQueueIndices = new ArrayList<>();

        for (int i = 0; i < queues.size(); i++) {
            int size = queues.get(i).size();
            if (size < smallestQueueLength) {
                smallestQueueLength = size;
                smallestQueueIndices.clear();
                smallestQueueIndices.add(i);
            }
            else if (size == smallestQueueLength) {
                smallestQueueIndices.add(i);
            }
        }

        int randomIndexOfSmallestQueues = new Random().nextInt(smallestQueueIndices.size());
        return smallestQueueIndices.get(randomIndexOfSmallestQueues);
    }

    private synchronized void waitForReachingTargetFloor() {
        try {
            while (!reachedDestination) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
