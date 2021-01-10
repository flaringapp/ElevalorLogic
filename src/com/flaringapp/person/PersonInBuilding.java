package com.flaringapp.person;

import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.QueueConsumer;

public class PersonInBuilding implements Person, ElevatorConsumer, QueueConsumer {

    private final Person person;

    private final int initialFloor;
    private final int targetFloor;

    private final int elevator;

    public PersonInBuilding(Person person, int initialFloor, int targetFloor, int elevator) {
        this.person = person;
        this.initialFloor = initialFloor;
        this.targetFloor = targetFloor;
        this.elevator = elevator;
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
    public int sourceFloor() {
        return initialFloor;
    }

    @Override
    public int destinationFloor() {
        return targetFloor;
    }

    @Override
    public int elevatorIndex() {
        return elevator;
    }

    @Override
    public void onEnteredQueue(int queue) {
//        listener.onPersonEnteredQueue(initialFloor, queue);
    }

    @Override
    public void onLeftQueue(int queue) {
//        listener.onPersonEnteredQueue(initialFloor, queue);
    }

    @Override
    public void onEnteredElevator() {
//        listener.onPersonEnteredElevator(initialFloor, elevator);
    }

    @Override
    public void onLeftElevator() {
//        listener.onPersonLeftElevator(targetFloor, elevator);
    }
}
