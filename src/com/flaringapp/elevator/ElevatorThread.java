package com.flaringapp.elevator;

public class ElevatorThread extends Thread implements Elevator {

//    private static final int DOOR_OPERATION_DURATION = 3000;

    private static final int speed = 2000;

    private static final int LEAVE_DELAY = 5000;

    private final Object stateLock = new Object();

    private final ElevatorControllable elevator;

    private boolean canLeave = true;

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

    private void waitForFloorMovement() {
        try {
            stateLock.wait(speed);
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
