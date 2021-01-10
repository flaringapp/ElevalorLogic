package com.flaringapp.person;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorCallbacks;
import com.flaringapp.floor.QueueConsumer;

public class PersonInBuilding implements Person, QueueConsumer {

    private final Person person;

    private final int initialFloor;
    private final int targetFloor;

    private final int elevator;

    private final ElevatorCallbacks elevatorCallbacks;

    public PersonInBuilding(Person person, int initialFloor, int targetFloor, int elevator, ElevatorCallbacks elevatorCallbacks) {
        this.person = person;
        this.initialFloor = initialFloor;
        this.targetFloor = targetFloor;
        this.elevator = elevator;
        this.elevatorCallbacks = elevatorCallbacks;
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
    public void onElevatorStartedMovement(Elevator elevator) {
        elevatorCallbacks.onElevatorStartedMovement(elevator);
    }

    @Override
    public void onElevatorCompletedMovement(Elevator elevator) {
        elevator.leave(this);
        elevatorCallbacks.onElevatorCompletedMovement(elevator);
    }
}
