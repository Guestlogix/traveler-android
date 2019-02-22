package com.guestlogix.travelercorekit.task;

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

    private State mState;
    private ArrayList<TaskObserver> mObservers;
    private ArrayList<Task> mDependentTasks;
    private volatile boolean mCancelled = false;
    private Semaphore mSemaphore;
    private Performer mPerformer = null;

    public Task() {
        mObservers = new ArrayList<>();
        mDependentTasks = new ArrayList<>();
        mSemaphore = new Semaphore(0);
        setState(State.READY);
    }

    final public State getState() {
        return mState;
    }

    final void setPerformer(Performer performer) {
        mPerformer = performer;
    }

    final void start() {
        if (mState == State.FINISHED) {
            Log.d("TASK", "Task already finished.");
            return;
        }

        if (mState == State.RUNNING) {
            Log.d("TASK", "Task already running.");
            return;
        }

        for (int i = 0; i < mDependentTasks.size(); i++) {
            try {
                mSemaphore.acquire();
            } catch (InterruptedException e) {
                TravelerLog.e("Could not acquire lock for dependencies. Will abort task.");
                mCancelled = true;
                setState(State.FINISHED);
                return;
            }
        }

        if (mCancelled) {
            setState(State.FINISHED);
            return;
        }

        setState(State.RUNNING);

        if (mPerformer == null) {
            execute();
        } else {
            mPerformer.onPerform(this);
        }
    }

    public abstract void execute();

    public final void finish() {
        setState(State.FINISHED);
        mPerformer = null;
    }

    public final void cancel() {
        Log.d("Traveler", "Cancelling:" + this.getClass().getSimpleName());
        mCancelled = true;
    }

    protected void addObserver(TaskObserver observer) {
        mObservers.add(observer);
    }

    protected void removeObserver(TaskObserver observer) {
        mObservers.remove(observer);
    }

    private void setState(State state) {
        mState = state;

        for (int i = 0; i < mObservers.size(); i++) {
            mObservers.get(i).onStateChanged(this);
        }
    }

    // Dependency Management

    final public void addDependency(Task task) {
        if (mState != State.READY) {
            TravelerLog.e("Cannot add dependency when task has already started or finished.");
            return;
        }

        if (task.getState() != State.READY) {
            TravelerLog.e("Dependent task must not have been started or finished.");
            return;
        }

        mDependentTasks.add(task);
        task.addObserver(this);
    }

    final public void removeDependency(Task task) {
        if (mState != State.READY) {
            TravelerLog.e("Cannot remove dependency when task has already started or finished.");
            return;
        }

        if (task.getState() == State.READY) {
            TravelerLog.e("Dependent task must not have been started or finished.");
            return;
        }

        mDependentTasks.remove(task);
        task.addObserver(this);
    }

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case READY:
                break;
            case FINISHED:
                mSemaphore.release();
            case RUNNING:
                break;
        }
    }
}