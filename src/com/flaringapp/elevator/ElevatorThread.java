package com.flaringapp.elevator;

public class ElevatorThread extends Thread implements Elevator {

//    private static final int DOOR_OPERATION_DURATION = 3000;

    private static final int speed = 2000;

    private final Object movementLock = new Object();

    private final ElevatorControllable elevator;

    public ElevatorThread(ElevatorControllable elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }

    @Override
    public boolean isOpened() {
        return elevator.isOpened();
    }

    @Override
    public boolean canEnter(ElevatorConsumer consumer) {
        return elevator.canEnter(consumer);
    }

    @Override
    public void enter(ElevatorConsumer consumer) {
        elevator.enter(consumer);
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        elevator.leave(consumer);
    }

    private void goToFloor(int floor) {
        int distance = floor - elevator.getCurrentFloor();
        boolean increment = distance < 0;
        while (distance != 0) {
            waitForFloorMovement();
            if (increment) distance++;
            else distance--;
            elevator.setCurrentFloor(floor - distance);
        }
    }

//    private void openElevatorDoors() {
//        doorAction(() -> elevator.setOpened(true));
//    }
//
//    private void closeElevatorDoors() {
//        doorAction(() -> elevator.setOpened(false));
//    }
//
//    private void doorAction(Runnable action) {
//        try {
//            sleep(DOOR_OPERATION_DURATION);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        action.run();
//    }

    private void waitForFloorMovement() {
        try {
            movementLock.wait(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
