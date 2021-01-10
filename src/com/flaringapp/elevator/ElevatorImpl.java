package com.flaringapp.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorImpl implements ElevatorControllable {

    private final float maxWeight;
    private final int maxSize;

    private int currentFloor;

    private boolean isOpened = false;

    private final List<ElevatorConsumer> consumers = new ArrayList<>();

    public ElevatorImpl(float maxWeight, int maxSize) {
        this(maxWeight, maxSize, 1);
    }

    public ElevatorImpl(float maxWeight, int maxSize, int currentFloor) {
        this.maxWeight = maxWeight;
        this.maxSize = maxSize;
        this.currentFloor = currentFloor;
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
    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public void setOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public boolean canEnter(ElevatorConsumer consumer) {
        return currentWeight() + consumer.getWeight() <= maxWeight &&
                currentSize() + 1 <= maxSize;
    }

    @Override
    public void enter(ElevatorConsumer consumer) {
        if (!isOpened) {
            throw new IllegalStateException("Consumer " + consumer + " tried to use a closed elevator!");
        }
        if (!canEnter(consumer)) {
            throw new IllegalStateException("Consumer " + consumer + " cannot use elevator!");
        }
        if (consumers.contains(consumer)) {
            throw new IllegalStateException("Consumer " + consumer + "already uses this elevator!");
        }
        consumers.add(consumer);
    }

    @Override
    public void leave(ElevatorConsumer consumer) {
        consumers.remove(consumer);
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
