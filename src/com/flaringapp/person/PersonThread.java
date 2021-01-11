package com.flaringapp.person;

import com.flaringapp.building.Building;
import com.flaringapp.elevator.Elevator;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;
import com.flaringapp.logger.Logger;

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
        Logger.getInstance().log("Started person thread " + getName() + " - " + person);

        enterSmallestQueue();

        Logger.getInstance().log("Person " + person + " waiting for reaching the target floor");
        waitForReachingTargetFloor();
        Logger.getInstance().log("Person " + person + " reached the target floor");
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
        Logger.getInstance().log("Person " + person + " entered queue " + queue);
    }

    @Override
    public void onLeftQueue(int queue) {
        Logger.getInstance().log("Person " + person + " left queue " + queue);
    }

    @Override
    public void onEnteredElevator(Elevator elevator) {
        Logger.getInstance().log("Person " + person + " entered elevator " + elevator);
    }

    @Override
    public void onLeftElevator(Elevator elevator) {
        Logger.getInstance().log("Person " + person + " left elevator " + elevator);
        reachedDestination = true;
        synchronized (this) {
            notify();
        }
    }

    private void enterSmallestQueue() {
        Floor floor = building.getFloors().get(fromFloor);
        int smallestQueueIndex = resolveRandomSmallestQueueIndex(floor);

        Logger.getInstance().log("Person " + person + " entering queue " + smallestQueueIndex);

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
