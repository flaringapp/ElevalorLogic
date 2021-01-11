package com.flaringapp.building;

import com.flaringapp.floor.Floor;

public interface BuildingFloor extends Floor {

    void notifyHeadConsumerCanEnterElevator(int elevator);

}
