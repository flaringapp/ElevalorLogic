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

    private final Object accessLock = new Object();
    private final Object movementLock = new Object();

    private boolean isWaitingForTask = true;
    private boolean canLeave = false;

    public ElevatorThread(ElevatorControllable elevator) {
        this.elevator = elevator;
    }

    @Override
    public void run() {
        Logger.getInstance().log("Started elevator thread " + getName() + " - " + elevator);

        ElevatorStrategy strategy = elevator.getMovementStrategy();
        // TODO end condition
        while (true) {
            Logger.getInstance().log("Elevator " + elevator + " waiting to activate");

            synchronized (accessLock) {
                elevator.setIsOpened(true);
            }

            waitBeforeMoveFurther();

            waitToActivate();

            Logger.getInstance().log("Elevator " + elevator + " activated. Resolving floor index to go...");

            synchronized (accessLock) {
                while (strategy.hasWhereToGo(this)) {
                    int floor = strategy.resolveFloorToGo(this);
                    Logger.getInstance().log("Elevator " + elevator + " goes to floor " + floor);
                    goToFloor(floor);
                }
            }
        }
    }

    @Override
    public int getCurrentFloor() {
        synchronized (accessLock) {
            return elevator.getCurrentFloor();
        }
    }

    @Override
    public Observable<Integer> getFloorObservable() {
        return elevator.getFloorObservable();
    }

    @Override
    public boolean isOpened() {
        synchronized (accessLock) {
            return elevator.isOpened();
        }
    }

    @Override
    public Set<Integer> getCalledFloors() {
        synchronized (accessLock) {
            return elevator.getCalledFloors();
        }
    }

    @Override
    public void callAtFloor(int floor) {
        synchronized (accessLock) {
            Logger.getInstance().log("Elevator " + elevator + " called at floor " + floor);
            elevator.callAtFloor(floor);

            if (isWaitingForTask) {
                accessLock.notify();
            }
        }
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        synchronized (accessLock) {
            return elevator.getConsumers();
        }
    }

    @Override
    public boolean enter(ElevatorConsumer consumer) {
        synchronized (accessLock) {
            if (elevator.enter(consumer)) {
                resetMovementDelay();
                return true;
            }
            return false;
        }
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        synchronized (accessLock) {
            elevator.leave(consumer);
            resetMovementDelay();
        }
    }

    @Override
    public Observable<List<ElevatorConsumer>> getConsumersObservable() {
        return elevator.getConsumersObservable();
    }

    private void waitToActivate() {
        synchronized (accessLock) {
            while (!elevator.getMovementStrategy().hasWhereToGo(this)) {
                isWaitingForTask = true;
                try {
                    accessLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isWaitingForTask = false;
        }
    }

    private void goToFloor(int floor) {
        synchronized (accessLock) {
            elevator.setIsOpened(false);
        }
        Logger.getInstance().log("Elevator " + elevator + " closed the doors at floor " + getCurrentFloor());

        Logger.getInstance().log("Elevator " + elevator + " performs movement to floor " + floor);
        performMovement(floor);
        Logger.getInstance().log("Elevator " + elevator + " reached floor " + floor);

        synchronized (accessLock) {
            elevator.setIsOpened(true);
            elevator.removeCalledFloor(floor);
            getConsumers().forEach(consumer -> consumer.onElevatorDockedToFloor(this, floor));
            getFloorObservable().notifyObservers(floor);
        }

        Logger.getInstance().log("Elevator " + elevator + " opened doors at floor " + floor);

        waitBeforeMoveFurther();
    }

    private void performMovement(int floor) {
        int distance;
        synchronized (accessLock) {
            distance = floor - elevator.getCurrentFloor();
        }
        boolean increment = distance < 0;
        while (distance != 0) {
            moveToNextFloor();
            if (increment) distance++;
            else distance--;

            synchronized (accessLock) {
                elevator.setCurrentFloor(floor - distance);
            }
        }
    }

    private void moveToNextFloor() {
        synchronized (movementLock) {
            try {
                movementLock.wait(SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitBeforeMoveFurther() {
        synchronized (movementLock) {
            canLeave = false;
            while (!canLeave) {
                canLeave = true;
                waitBeforeStartMovement();
            }
        }
    }

    private void waitBeforeStartMovement() {
        synchronized (movementLock) {
            try {
                movementLock.wait(LEAVE_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetMovementDelay() {
        synchronized (movementLock) {
            canLeave = false;
            movementLock.notify();
        }
    }

}
