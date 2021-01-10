package com.flaringapp.person;

import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.QueueConsumer;
import com.flaringapp.person.listener.PersonListener;

public class PersonInBuilding implements Person, ElevatorConsumer, QueueConsumer {

    private final Person person;

    private final int initialFloor;
    private final int targetFloor;

    private final int elevator;

    private final PersonListener listener;

    public PersonInBuilding(Person person, int initialFloor, int targetFloor, int elevator, PersonListener listener) {
        this.person = person;
        this.initialFloor = initialFloor;
        this.targetFloor = targetFloor;
        this.elevator = elevator;
        this.listener = listener;
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
    public void onEnteredQueue(int queue) {
        listener.onPersonEnteredQueue(initialFloor, queue);
    }

    @Override
    public void onLeftQueue(int queue) {
        listener.onPersonEnteredQueue(initialFloor, queue);
    }

    @Override
    public void onEnteredElevator() {
        listener.onPersonEnteredElevator(initialFloor, elevator);
    }

    @Override
    public void onLeftElevator() {
        listener.onPersonLeftElevator(targetFloor, elevator);
    }
}
