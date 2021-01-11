package com.flaringapp.elevator;


import com.flaringapp.elevator.strategy.ElevatorStrategy;
import com.flaringapp.utils.observable.Observable;

import java.util.List;
import java.util.Set;

public class ElevatorThread extends Thread implements Elevator {

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
        ElevatorStrategy strategy = elevator.getMovementStrategy();
        // TODO end condition
        while (true) {
            waitToActivate();
            while (strategy.hasWhereToGo(this)) {
                int floor = strategy.resolveFloorToGo(this);
                goToFloor(floor);
            }
        }
    }

    @Override
    public int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }

    @Override
    public Observable<Integer> getFloorObservable() {
        return elevator.getFloorObservable();
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        return elevator.getConsumers();
    }

    @Override
    public boolean canEnter(ElevatorConsumer consumer) {
        return elevator.canEnter(consumer);
    }

    @Override
    public void enter(ElevatorConsumer consumer) {
        elevator.enter(consumer);
        resetMovementDelay();
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        elevator.leave(consumer);
        resetMovementDelay();
    }

    @Override
    public Observable<List<ElevatorConsumer>> getConsumersObservable() {
        return elevator.getConsumersObservable();
    }

    @Override
    public boolean isBeingInteracted() {
        return elevator.isBeingInteracted();
    }

    @Override
    public void setIsBeingInteracted(boolean isBeingInteracted) {
        elevator.setIsBeingInteracted(isBeingInteracted);
    }

    @Override
    public Set<Integer> getCalledFloors() {
        synchronized (stateLock) {
            return elevator.getCalledFloors();
        }
    }

    @Override
    public void callAtFloor(int floor) {
        synchronized (stateLock) {
            elevator.callAtFloor(floor);
        }
    }

    private void goToFloor(int floor) {
        synchronized (stateLock) {
            performMovement(floor);

            getConsumers().forEach(consumer -> consumer.onElevatorDockedToFloor(this, floor));
            getFloorObservable().notifyObservers(floor);

            waitBeforeMoveFurther();
        }
    }

    private void performMovement(int floor) {
        int distance = floor - elevator.getCurrentFloor();
        boolean increment = distance < 0;
        while (distance != 0) {
            waitForFloorMovement();
            if (increment) distance++;
            else distance--;
            elevator.setCurrentFloor(floor - distance);
        }
    }

    private void waitBeforeMoveFurther() {
        canLeave = false;
        while (!canLeave) {
            canLeave = true;
            waitForMove();
        }
    }

    private void resetMovementDelay() {
        synchronized (stateLock) {
            canLeave = false;
            stateLock.notify();
        }
    }

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
