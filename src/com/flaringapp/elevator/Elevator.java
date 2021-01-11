package com.flaringapp.elevator;

import com.flaringapp.utils.observable.Observable;

import java.util.List;
import java.util.Set;

public interface Elevator {

    int getCurrentFloor();
    Observable<Integer> getFloorObservable();

    boolean isOpened();

    Set<Integer> getCalledFloors();
    boolean callAtFloor(int floor);

    List<ElevatorConsumer> getConsumers();

    boolean enter(ElevatorConsumer consumer);
    boolean leave(ElevatorConsumer consumer);

    Observable<List<ElevatorConsumer>> getConsumersObservable();

}
