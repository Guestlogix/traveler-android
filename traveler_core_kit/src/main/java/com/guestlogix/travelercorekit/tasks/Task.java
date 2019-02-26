package com.guestlogix.travelercorekit.tasks;

import android.util.Log;
import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public abstract class Task implements TaskObserver {

    public enum State {
        READY, RUNNING, FINISHED
    }

    interface Performer {
        void onPerform(Task task);
    }

    private State state;
    private ArrayList<TaskObserver> observers;
    private ArrayList<Task> dependentTasks;
    private volatile boolean cancelled = false;
    private Semaphore semaphore;
    private Performer performer = null;

    public Task() {
        observers = new ArrayList<>();
        dependentTasks = new ArrayList<>();
        semaphore = new Semaphore(0);
        setState(State.READY);
    }

    final public State getState() {
        return state;
    }

    final void setPerformer(Performer performer) {
        this.performer = performer;
    }

    final void start() {
        if (state == State.FINISHED) {
            return;
        }

        if (state == State.RUNNING) {
            return;
        }

        for (int i = 0; i < dependentTasks.size(); i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                TravelerLog.e("Could not acquire lock for dependencies. Will abort task.");
                cancelled = true;
                setState(State.FINISHED);
                return;
            }
        }

        if (cancelled) {
            setState(State.FINISHED);
            return;
        }

        setState(State.RUNNING);

        if (performer == null) {
            execute();
        } else {
            performer.onPerform(this);
        }
    }

    public abstract void execute();

    public final void finish() {
        setState(State.FINISHED);
        performer = null;
    }

    public final void cancel() {
        cancelled = true;
    }

    protected void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    protected void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    private void setState(State state) {
        this.state = state;

        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).onStateChanged(this);
        }
    }

    // Dependency Management

    final public void addDependency(Task task) {
        if (state != State.READY) {
            TravelerLog.e("Cannot add dependency when task has already started or finished.");
            return;
        }

        if (task.getState() != State.READY) {
            TravelerLog.e("Dependent task must not have been started or finished.");
            return;
        }

        dependentTasks.add(task);
        task.addObserver(this);
    }

    final public void removeDependency(Task task) {
        if (state != State.READY) {
            TravelerLog.e("Cannot remove dependency when task has already started or finished.");
            return;
        }

        if (task.getState() == State.READY) {
            TravelerLog.e("Dependent task must not have been started or finished.");
            return;
        }

        dependentTasks.remove(task);
        task.addObserver(this);
    }

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case READY:
                break;
            case FINISHED:
                semaphore.release();
            case RUNNING:
                break;
        }
    }
}