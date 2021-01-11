package com.flaringapp.building;

import com.flaringapp.floor.Floor;
import com.flaringapp.floor.QueueConsumer;

public interface BuildingFloor extends Floor {

    QueueConsumer getQueueHead(int elevator);

}
