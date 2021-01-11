package com.flaringapp.person;

import com.flaringapp.elevator.Elevator;

public interface PersonLifecycle {

    void onEnteredQueue(int queue);

    void onLeftQueue(int queue);

    void onEnteredElevator(Elevator elevator);

    void onLeftElevator(Elevator elevator);

}
