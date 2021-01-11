package com.flaringapp.elevator;

import com.flaringapp.utils.observable.Observable;

import java.util.List;
import java.util.Set;

public interface Elevator {

    int getCurrentFloor();
    Observable<Integer> getFloorObservable();

    Set<Integer> getCalledFloors();
    void callAtFloor(int floor);

    List<ElevatorConsumer> getConsumers();

    boolean canEnter(ElevatorConsumer consumer);
    void enter(ElevatorConsumer consumer);

    void leave(ElevatorConsumer consumer);

    Observable<List<ElevatorConsumer>> getConsumersObservable();

    boolean isBeingInteracted();
    void setIsBeingInteracted(boolean isBeingInteracted);

}
