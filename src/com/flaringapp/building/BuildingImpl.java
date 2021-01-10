package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.Floor;
import com.flaringapp.person.Person;
import com.flaringapp.person.PersonInBuilding;

import java.util.List;

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
    public int floorsCount() {
        return floors.size();
    }

    @Override
    public void enterQueue(PersonInBuilding person) {

    }

    @Override
    public void enterQueue(Person person, int floorIndex, int queueIndex) {
        floors.get(floorIndex).enterQueue(person, queueIndex);
    }

    private void leaveQueue(Person person, int floorIndex, int queueIndex) {
        floors.get(floorIndex).leaveQueue(person, queueIndex);
    }

    private void enterElevator(ElevatorConsumer person, Elevator elevator) {
        Elevator elevator = elevators.get(elevatorIndex);
        if (elevator.getCurrentFloor() != floorIndex) {
            throw new IllegalStateException("Person " + person + " tried to enter elevator at floor" +
                    floorIndex + " but elevator is currently at floor " + elevator.getCurrentFloor()
            );
        }

        elevator.use(person);
    }
}
