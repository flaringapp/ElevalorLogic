package com.flaringapp.person.listener;

public interface PersonListener {

    void onPersonEnteredFloor(int floor);

    void onPersonEnteredQueue(int floor, int queue);

    void onPersonEnteredElevator(int floorIndex, int elevatorIndex);

    void onPersonLeftElevator(int floorIndex, int elevatorIndex);

}
