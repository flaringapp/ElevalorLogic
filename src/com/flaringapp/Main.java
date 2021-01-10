package com.flaringapp;

import com.flaringapp.building.Building;
import com.flaringapp.building.BuildingImpl;
import com.flaringapp.elevator.Elevator;
import com.flaringapp.elevator.ElevatorImpl;
import com.flaringapp.elevator.ElevatorThread;
import com.flaringapp.floor.Floor;
import com.flaringapp.floor.FloorImpl;
import com.flaringapp.spawner.UserSpawner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int ELEVATORS_COUNT = 4;

    public static void main(String[] args) {
        Building building = createBuilding();
        UserSpawner spawner = new UserSpawner(building);
        spawner.startSpawn();
    }

    private static Building createBuilding() {
        List<Floor> floors = createFloors(10);
        List<Elevator> elevators = createElevators(2);
        return new BuildingImpl(floors, elevators);
    }

    private static List<Floor> createFloors(int count) {
        List<Floor> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            floors.add(createFloor());
        }
        return floors;
    }

    private static Floor createFloor() {
        return new FloorImpl(ELEVATORS_COUNT);
    }

    private static List<Elevator> createElevators(int count) {
        List<Elevator> floors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            floors.add(createElevatorThread());
        }
        return floors;
    }

    private static ElevatorThread createElevatorThread() {
        ElevatorThread thread = new ElevatorThread(createElevator());
        thread.start();
        return thread;
    }

    private static ElevatorImpl createElevator() {
        return new ElevatorImpl(600f, 5);
    }
}
