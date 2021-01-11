package com.flaringapp.elevator;

import com.flaringapp.elevator.strategy.ElevatorStrategy;
import com.flaringapp.utils.observable.Observable;
import com.flaringapp.utils.observable.SimpleObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ElevatorImpl implements ElevatorControllable {

    private final float maxWeight;
    private final int maxSize;
    private final ElevatorStrategy movementStrategy;

    private int currentFloor;

    private final Observable<Integer> floorObservable = new SimpleObservable<>();

    private final Set<Integer> calledFloors = new TreeSet<>();

    private final List<ElevatorConsumer> consumers = new ArrayList<>();

    private final Observable<List<ElevatorConsumer>> consumersObservable = new SimpleObservable<>();


    private boolean isBeingInteracted = false;

    public ElevatorImpl(float maxWeight, int maxSize, ElevatorStrategy strategy) {
        this(maxWeight, maxSize, strategy, 1);
    }

    public ElevatorImpl(float maxWeight, int maxSize, ElevatorStrategy strategy, int currentFloor) {
        this.maxWeight = maxWeight;
        this.maxSize = maxSize;
        this.movementStrategy = strategy;
        this.currentFloor = currentFloor;
    }

    @Override
    public ElevatorStrategy getMovementStrategy() {
        return movementStrategy;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    @Override
    public Observable<Integer> getFloorObservable() {
        return floorObservable;
    }

    public Set<Integer> getCalledFloors() {
        return calledFloors;
    }

    @Override
    public void callAtFloor(int floor) {
        calledFloors.add(floor);
    }

    @Override
    public List<ElevatorConsumer> getConsumers() {
        return consumers;
    }

    @Override
    public boolean canEnter(ElevatorConsumer consumer) {
        return consumer.sourceFloor() == currentFloor &&
                currentWeight() + consumer.getWeight() <= maxWeight &&
                currentSize() + 1 <= maxSize;
    }

    @Override
    public void enter(ElevatorConsumer consumer) {
        if (!canEnter(consumer)) {
            throw new IllegalStateException("Consumer " + consumer + " cannot use elevator!");
        }
        if (consumers.contains(consumer)) {
            throw new IllegalStateException("Consumer " + consumer + "already uses this elevator!");
        }
        consumers.add(consumer);
        consumersObservable.notifyObservers(consumers);
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        consumers.remove(consumer);
        consumersObservable.notifyObservers(consumers);
    }

    @Override
    public Observable<List<ElevatorConsumer>> getConsumersObservable() {
        return consumersObservable;
    }

    @Override
    public boolean isBeingInteracted() {
        return isBeingInteracted;
    }

    @Override
    public void setIsBeingInteracted(boolean isBeingInteracted) {
        this.isBeingInteracted = isBeingInteracted;
    }

    private float currentWeight() {
        return consumers.stream()
                .map(ElevatorConsumer::getWeight)
                .reduce(0f, Float::sum);
    }

    private float currentSize() {
        return consumers.size();
    }
}
