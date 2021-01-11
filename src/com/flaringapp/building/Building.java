package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.floor.Floor;
import com.flaringapp.person.PersonInBuilding;

import java.util.List;

public interface Building {

    List<Floor> getFloors();
    List<Elevator> getElevators();

    int floorsCount();
    int elevatorsCount();

    void enterQueue(PersonInBuilding person);
}
