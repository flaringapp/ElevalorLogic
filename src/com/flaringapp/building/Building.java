package com.flaringapp.building;

import java.util.List;

public interface Building {

    int floorsCount();
    int elevatorsCount();

    List<Integer> smallestQueueIndicesAtFloor(int floor);

    void enterQueue(BuildingConsumer consumer);
    void enterElevator(BuildingConsumer consumer);
    void leaveElevator(BuildingConsumer consumer);
}
