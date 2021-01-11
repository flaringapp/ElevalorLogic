package com.flaringapp.elevator;


import com.flaringapp.elevator.strategy.ElevatorStrategy;
import com.flaringapp.logger.Logger;
import com.flaringapp.utils.observable.Observable;

import java.util.List;
import java.util.Set;

public class ElevatorThread extends Thread implements Elevator {

    private static final int SPEED = 2000;
    private static final int LEAVE_DELAY = 5000;

    private final ElevatorControllable elevator;

    private final Object stateLock = new Object();

    private boolean isWaitingForTask = true;
    private boolean canLeave = true;

    public ElevatorThread(ElevatorControllable elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        Logger.getInstance().log("Started elevator thread " + getName() + " - " + elevator);

        ElevatorStrategy strategy = elevator.getMovementStrategy();
        // TODO end condition
        while (true) {
            Logger.getInstance().log("Elevator " + getName() + " waiting to activate");

            synchronized (stateLock) {
                waitToActivate();
            }

            Logger.getInstance().log("Elevator " + getName() + " activated. Resolving floor index to go...");
            while (strategy.hasWhereToGo(this)) {
                int floor = strategy.resolveFloorToGo(this);
                Logger.getInstance().log("Elevator " + getName() + " goes to floor " + floor);
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
            Logger.getInstance().log("Elevator " + elevator + " called at floor " + floor);
            elevator.callAtFloor(floor);

            if (isWaitingForTask) {
                stateLock.notify();
            }
        }
    }

    private void goToFloor(int floor) {
        synchronized (stateLock) {
            Logger.getInstance().log("Elevator " + elevator + " performs movement to floor " + floor);

            performMovement(floor);

            Logger.getInstance().log("Elevator " + elevator + " reached floor " + floor);

            getConsumers().forEach(consumer -> consumer.onElevatorDockedToFloor(this, floor));
            getFloorObservable().notifyObservers(floor);

            Logger.getInstance().log("Elevator " + elevator + " waiting for further movement " + floor);
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
            while (!elevator.getMovementStrategy().hasWhereToGo(this)) {
                isWaitingForTask = true;
                try {
                    stateLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isWaitingForTask = false;
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
