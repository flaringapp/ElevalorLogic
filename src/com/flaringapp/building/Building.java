package com.flaringapp.building;

import com.flaringapp.elevator.Elevator;
import com.flaringapp.floor.Floor;
import com.flaringapp.person.PersonInBuilding;

import java.util.List;

public interface Building {

    List<Floor> getFloors();

    int floorsCount();

    void enterQueue(PersonInBuilding person);

    void fillElevatorWithConsumers(Elevator elevator);

}
