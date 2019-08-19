package com.guestlogix.travelercorekit.utilities;


import java.util.ArrayList;
import java.util.List;

public class MockTaskManager extends TaskManager {

    private int addTaskInvocationCount = 0;
    private List<Task> addTaskArguments = new ArrayList<>();

    public void addTask(Task task) {
        addTaskArguments.add(task);
        addTaskInvocationCount++;
    }

    public int getInvocationCount_AddTask() {
        return addTaskInvocationCount;
    }

    public List<Task> getArguments_AddTask() {
        return addTaskArguments;
    }
}
