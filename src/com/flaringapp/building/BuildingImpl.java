package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.floor.Floor;
import com.flaringapp.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class BuildingImpl implements Building {

    private final List<BuildingFloor> floors;
    private final List<Elevator> elevators;

    public BuildingImpl(List<BuildingFloor> floors, List<Elevator> elevators) {
        this.floors = floors;
        this.elevators = elevators;

        elevators.forEach(elevator -> {
            elevator.getFloorObservable()
                    .subscribe(floor -> fillElevatorWithConsumers(elevator));

            elevator.getConsumersObservable()
                    .subscribe(consumers -> {
                        if (hasNobodyToLeave(elevator)) {
                            fillElevatorWithConsumers(elevator);
                        }
                    });
        });
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
    public List<Integer> smallestQueueIndicesAtFloor(int floorIndex) {
        Floor floor = floors.get(floorIndex);
        int smallestQueueLength = Integer.MAX_VALUE;
        List<Integer> smallestQueueIndices = new ArrayList<>();

        for (int i = 0; i < elevators.size(); i++) {
            int size = floor.getQueueSizeAtElevator(i);
            if (size < smallestQueueLength) {
                smallestQueueLength = size;
                smallestQueueIndices.clear();
                smallestQueueIndices.add(i);
            } else if (size == smallestQueueLength) {
                smallestQueueIndices.add(i);
            }
        }

        return smallestQueueIndices;
    }

    @Override
    public void enterQueue(BuildingConsumer consumer) {
        Floor floor = floors.get(consumer.sourceFloor());

        boolean callElevator = floor.enterQueue(consumer);
        consumer.onQueueEntered();

        Logger.getInstance().log(consumer + " entered queue " + consumer.elevatorIndex());

        if (callElevator) {
            Elevator elevator = elevators.get(consumer.elevatorIndex());
            if (elevator.enter(consumer)) {
                floor.leaveQueue(consumer);
                Logger.getInstance().log(consumer + " instantly left queue and entered " + elevator + " at floor " + consumer.sourceFloor());
            } else {
                elevators.get(consumer.elevatorIndex())
                        .callAtFloor(consumer.sourceFloor());
                Logger.getInstance().log(consumer + " called " + elevator + " at floor " + consumer.sourceFloor());
            }
        }
    }

    @Override
    public boolean enterElevator(BuildingConsumer consumer) {
        Floor floor = floors.get(consumer.sourceFloor());
        floor.leaveQueue(consumer);
        return elevators.get(consumer.elevatorIndex()).enter(consumer);
    }

    @Override
    public boolean leaveElevator(BuildingConsumer consumer) {
        return elevators.get(consumer.elevatorIndex()).leave(consumer);
    }

    private void fillElevatorWithConsumers(Elevator elevator) {
        BuildingFloor floor = floors.get(elevator.getCurrentFloor());
        int elevatorIndex = elevators.indexOf(elevator);
        floor.notifyHeadConsumerQueueCompleted(elevatorIndex);
    }

    private boolean hasNobodyToLeave(Elevator elevator) {
        return elevator.getConsumers().stream()
                .anyMatch(consumer -> consumer.destinationFloor() == elevator.getCurrentFloor());
    }


}
