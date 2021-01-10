package com.flaringapp.elevator;

import java.util.List;
import java.util.Set;

public class ElevatorThread extends Thread implements Elevator {

//    private static final int DOOR_OPERATION_DURATION = 3000;

    private static final int SPEED = 2000;
    private static final int LEAVE_DELAY = 5000;

    private final ElevatorControllable elevator;

    private final Object stateLock = new Object();

    private boolean canLeave = true;

    public ElevatorThread(ElevatorControllable elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        // TODO end condition
        while (true) {
            waitToActivate();
        }
    }

    @Override
    public int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        return elevator.getConsumers();
    }

//    @Override
//    public boolean isOpened() {
//        return elevator.isOpened();
//    }

    @Override
    public boolean canEnter(ElevatorConsumer consumer) {
        return elevator.canEnter(consumer);
    }

    @Override
    public void enter(ElevatorConsumer consumer) {
        elevator.enter(consumer);
        resetDelayedMove();
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        elevator.leave(consumer);
        resetDelayedMove();
    }

    @Override
    public Set<Integer> getCalledFloors() {
        return elevator.getCalledFloors();
    }

    @Override
    public void callAtFloor(int floor) {
        elevator.callAtFloor(floor);
    }

    @Override
    public boolean goesUpstairs() {
        return elevator.goesUpstairs();
    }

    private void goToFloor(int floor) {
        synchronized (stateLock) {
            int distance = floor - elevator.getCurrentFloor();
            boolean increment = distance < 0;
            while (distance != 0) {
                waitForFloorMovement();
                if (increment) distance++;
                else distance--;
                elevator.setCurrentFloor(floor - distance);
            }

            moveAfterDelay();
        }
    }

    private void moveAfterDelay() {
        canLeave = false;
        while (!canLeave) {
            canLeave = true;
            waitForMove();
        }

        // TODO move further
    }

    private void resetDelayedMove() {
        synchronized (stateLock) {
            canLeave = false;
            stateLock.notify();
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

    private void waitToActivate() {
        synchronized (stateLock) {
            while (getCalledFloors().isEmpty() && getConsumers().isEmpty()) {
                try {
                    stateLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void waitForFloorMovement() {
        try {
            stateLock.wait(SPEED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForMove() {
        try {
            stateLock.wait(LEAVE_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
