package com.guestlogix.task;

interface TaskObserver {
    void onStateChanged(Task task);
}
