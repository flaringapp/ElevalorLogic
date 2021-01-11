package com.flaringapp.building;

import com.flaringapp.elevator.ElevatorConsumer;
import com.flaringapp.floor.QueueConsumer;

public interface BuildingConsumer extends QueueConsumer, ElevatorConsumer {
}
