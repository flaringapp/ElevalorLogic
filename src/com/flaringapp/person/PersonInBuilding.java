package com.flaringapp.person;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.QueueConsumer;

public class PersonInBuilding implements Person, QueueConsumer, ElevatorConsumer {

    private final Person person;

    private final int initialFloor;
    private final int targetFloor;

    private final int elevator;

    private final PersonLifecycle lifecycle;

    public PersonInBuilding(
            Person person,
            int initialFloor,
            int targetFloor,
            int elevator,
            PersonLifecycle lifecycle
    ) {
        this.person = person;
        this.initialFloor = initialFloor;
        this.targetFloor = targetFloor;
        this.elevator = elevator;
        this.lifecycle = lifecycle;
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
    public void onQueueEntered() {
        lifecycle.onEnteredQueue(elevator);
    }

    @Override
    public boolean enterElevator(Elevator elevator) {
        if (elevator.canEnter(this)) {
            elevator.enter(this);
            lifecycle.onLeftQueue(this.elevator);
            lifecycle.onEnteredElevator(elevator);
            return true;
        }
        return false;
    }

    @Override
    public void onElevatorDockedToFloor(Elevator elevator, int floor) {
        if (floor == targetFloor) {
            elevator.leave(this);
            lifecycle.onLeftElevator(elevator);
        }
    }
}
