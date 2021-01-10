package com.flaringapp.elevator;

public interface ElevatorControllable extends Elevator {

//    void setOpened(boolean isOpened);

    void setCurrentFloor(int floor);

    void setGoesUpstairs(boolean goesUpstairs);

}
