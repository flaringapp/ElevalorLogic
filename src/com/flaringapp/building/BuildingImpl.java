package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;
import com.flaringapp.person.PersonInBuilding;

import java.util.List;
import java.util.Queue;

public class BuildingImpl implements Building {

    private final List<Floor> floors;
    private final List<Elevator> elevators;

    public BuildingImpl(List<Floor> floors, List<Elevator> elevators) {
        this.floors = floors;
        this.elevators = elevators;
    }

    @Override
    public List<Floor> getFloors() {
        return floors;
    }

    @Override
    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public int floorsCount() {
        return floors.size();
    }

    @Override
    public int elevatorsCount() {
        return elevators.size();
    }

    @Override
    public void enterQueue(PersonInBuilding person) {
        Floor floor = floors.get(person.sourceFloor());
        boolean callElevator = floor.getQueueAtElevator(person.elevatorIndex()).isEmpty();
        floor.enterQueue(person);
        if (callElevator) elevators.get(person.elevatorIndex()).callAtFloor(person.sourceFloor());
    }

    private void kickConsumersFromElevator(Elevator elevator) {
        elevator.getConsumers().forEach(consumer -> {
            if (consumer.destinationFloor() == elevator.getCurrentFloor()) {
                elevator.leave(consumer);
            }
        });
    }

    private void fillElevatorWithConsumers(Elevator elevator) {
        int elevatorIndex = elevators.indexOf(elevator);
        Floor floor = floors.get(elevator.getCurrentFloor());
        Queue<QueueConsumer> queue = floor.getQueueAtElevator(elevatorIndex);

        while (!queue.isEmpty()) {
            QueueConsumer consumer = queue.peek();
            if (!elevator.canEnter(consumer)) break;
            queue.poll();
            elevator.enter(consumer);
        }
    }

    private void leaveQueue(QueueConsumer consumer) {
        floors.get(consumer.sourceFloor()).leaveQueue(consumer);
    }

    private void enterElevator(ElevatorConsumer consumer, Elevator elevator) {
        if (elevator.getCurrentFloor() != consumer.sourceFloor()) {
            throw new IllegalStateException("Consumer " + consumer + " tried to enter elevator at floor" +
                    consumer.sourceFloor() + " but elevator is currently at floor " + elevator.getCurrentFloor()
            );
        }
        elevator.enter(consumer);
    }
}
