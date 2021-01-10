package com.flaringapp.person;

import com.flaringapp.building.Building;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class PersonThread extends Thread implements Person {

    public static final int NO_INDEX = -1;

//    private static final PersonListener emptyListener = new EmptyPersonListener();

    private final Person person;

    private final Building building;

//    private PersonListener listener = emptyListener;

    private final int fromFloor;
    private final int toFloor;

    private boolean reachedDestination = false;

    public PersonThread(Person person, Building building, int fromFloor, int toFloor) {
        this.person = person;
        this.building = building;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
    }

//    public void setListener(PersonListener listener) {
//        if (listener == null) this.listener = emptyListener;
//        else this.listener = listener;
//    }

    @Override
    public void run() {
//        listener.onPersonEnteredFloor(fromFloor);

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

//    @Override
//    public void onLeftElevator() {
//        reachedDestination = true;
//        notify();
//    }
//
//    @Override
//    public void onPersonEnteredFloor(int floor) {
//        listener.onPersonEnteredFloor(floor);
//    }
//
//    @Override
//    public void onPersonEnteredQueue(int floor, int queue) {
//        listener.onPersonEnteredQueue(floor, queue);
//    }
//
//    @Override
//    public void onPersonEnteredElevator(int floorIndex, int elevatorIndex) {
//        listener.onPersonEnteredElevator(floorIndex, elevatorIndex);
//    }
//
//    @Override
//    public void onPersonLeftElevator(int floorIndex, int elevatorIndex) {
//        listener.onPersonEnteredElevator(floorIndex, elevatorIndex);
//    }

    private void enterSmallestQueue() {
        Floor floor = building.getFloors().get(fromFloor);
        int smallestQueueIndex = resolveRandomSmallestQueueIndex(floor);

        PersonInBuilding personInBuilding = new PersonInBuilding(person, fromFloor, toFloor, smallestQueueIndex);
        building.enterQueue(personInBuilding);

//        listener.onPersonEnteredQueue(smallestQueueIndex);
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
//        listener.onPersonLeftElevator(toFloor, enteredQueueIndex);
    }
}
