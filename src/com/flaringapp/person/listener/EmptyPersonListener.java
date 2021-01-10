package com.flaringapp.person.listener;

public class EmptyPersonListener implements PersonListener {

    @Override
    public void onPersonEnteredFloor(int floorIndex) {
    }

    @Override
    public void onPersonEnteredQueue(int queueIndex) {
    }

    @Override
    public void onPersonEnteredElevator(int elevatorIndex) {
    }

    @Override
    public void onPersonLeftElevator(int floorIndex, int elevatorIndex) {
    }
}
