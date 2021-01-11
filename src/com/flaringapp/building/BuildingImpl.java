package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;
import com.flaringapp.person.PersonInBuilding;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class BuildingImpl implements Building {

    private final List<Floor> floors;
    private final List<Elevator> elevators;

    public BuildingImpl(List<Floor> floors, List<Elevator> elevators) {
        this.floors = floors;
        this.elevators = elevators;

        elevators.forEach(elevator -> {
            elevator.getFloorObservable()
                    .subscribe(floor -> {
                        kickConsumersFromElevator(elevator);
                    });

            elevator.getConsumersObservable()
                    .subscribe(consumers -> {
                        if (countConsumersInElevatorWithDestination(elevator) == 0) {
                            fillElevatorWithConsumers(elevator);
                        }
                    });
        });
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
        List<ElevatorConsumer> consumersLeaving = consumersInElevatorWithDestination(elevator);
        if (consumersLeaving.isEmpty()) {
            fillElevatorWithConsumers(elevator);
        } else {
            consumersLeaving.forEach(elevator::leave);
        }
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

    private int countConsumersInElevatorWithDestination(Elevator elevator) {
        return consumersInElevatorWithDestination(elevator).size();
    }

    private List<ElevatorConsumer> consumersInElevatorWithDestination(Elevator elevator) {
        return elevator.getConsumers().stream()
                .filter(consumer -> consumer.destinationFloor() == elevator.getCurrentFloor())
                .collect(Collectors.toList());
    }
}
