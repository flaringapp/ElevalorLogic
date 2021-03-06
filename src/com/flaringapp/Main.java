package com.flaringapp;

import com.flaringapp.building.Building;
import com.flaringapp.building.BuildingFloor;
import com.flaringapp.building.BuildingImpl;
import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorControllable;
import com.flaringapp.elevator.ElevatorImpl;
import com.flaringapp.elevator.ElevatorThread;
import com.flaringapp.elevator.strategy.DumbElevatorStrategy;
import com.flaringapp.floor.FloorImpl;
import com.flaringapp.logger.Logger;
import com.flaringapp.spawner.PersonSpawner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int FLOORS_COUNT = 4;
    private static final int ELEVATORS_COUNT = 1;
    private static final float ELEVATOR_WEIGHT = 600f;
    private static final int ELEVATOR_SIZE = 5;

    public static void main(String[] args) {
        Logger.getInstance().logTitle("App started");

        Building building = createBuilding();
        Logger.getInstance().logTitle("Building created successfully");

        PersonSpawner spawner = new PersonSpawner(building);
        spawner.startSpawn();

        Logger.getInstance().logTitle("App initialized successfully");
    }

    private static Building createBuilding() {
        List<BuildingFloor> floors = createFloors(FLOORS_COUNT);
        Logger.getInstance().log("Floors created successfully");

        List<Elevator> elevators = createElevators(ELEVATORS_COUNT);
        Logger.getInstance().log("Elevators created successfully");

        return new BuildingImpl(floors, elevators);
    }

    private static List<BuildingFloor> createFloors(int count) {
        List<BuildingFloor> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BuildingFloor floor = createFloor();
            Logger.getInstance().log("Created floor " + i);
            floors.add(floor);
        }
        return floors;
    }

    private static FloorImpl createFloor() {
        return new FloorImpl(ELEVATORS_COUNT);
    }

    private static List<Elevator> createElevators(int count) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ElevatorControllable elevator = createElevator(i);
            Logger.getInstance().log(elevator + " created. Index: " + i);

            ElevatorThread elevatorThread = createElevatorThread(elevator);
            elevators.add(elevatorThread);
            elevatorThread.start();
        }
        return elevators;
    }

    private static ElevatorThread createElevatorThread(ElevatorControllable elevator) {
        return new ElevatorThread(elevator);
    }

    private static ElevatorImpl createElevator(int index) {
        return new ElevatorImpl(String.valueOf(index), ELEVATOR_WEIGHT, ELEVATOR_SIZE, new DumbElevatorStrategy());
    }
}
