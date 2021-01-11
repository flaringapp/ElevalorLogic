package com.flaringapp.spawner;

import com.flaringapp.building.Building;
import com.flaringapp.logger.Logger;
import com.flaringapp.person.Person;
import com.flaringapp.person.PersonImpl;
import com.flaringapp.person.PersonThread;

import java.util.Random;

public class PersonSpawner {

    private static final int DELAY = 2000;

    private final Building building;

    private final Thread spawnerThread = new Thread(this::infiniteSpawning, "SpawnerThread");
    private final Object activeLock = new Object();

    private boolean isActive = false;

    private int counter = 1;

    public PersonSpawner(Building building) {
        this.building = building;
    }

    public void startSpawn() {
        isActive = true;
        if (spawnerThread.isAlive()) return;
        spawnerThread.start();

        Logger.getInstance().logTitle("Requested person spawner to start");
    }

    public void stopSpawn() {
        isActive = false;
        activeLock.notify();

        Logger.getInstance().logTitle("Requested person spawner to stop");
    }

    private void infiniteSpawning() {
        Logger.getInstance().logTitle("Person spawner started successfully");
        synchronized (activeLock) {
            while (isActive) {
                try {
                    activeLock.wait(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executeSpawn();
            }
        }
        Logger.getInstance().logTitle("Person spawner stopped successfully");
    }

    private void executeSpawn() {
        Logger.getInstance().log("Trying to spawn a new person...");

        Person person = new PersonImpl(
                String.valueOf(counter),
                new Random().nextInt(20) + 60f
        );

        counter++;

        int from = randomFloorFrom();
        int to = randomFloorTo(from);
        PersonThread personThread = new PersonThread(
                person,
                building,
                from, to
        );
        Logger.getInstance().log(person + " spawned. " + from + " -> " + to + "." + "Thread created with name " + personThread.getName());
        personThread.start();
        Logger.getInstance().log(person + " started");
    }

    private int randomFloorFrom() {
        return new Random().nextInt(building.floorsCount());
    }

    private int randomFloorTo(int from) {
        int to = new Random().nextInt(building.floorsCount());
        if (to != from) return to;

        if (to == building.floorsCount() - 1) return to - 1;
        else return from - 1;
    }
}
