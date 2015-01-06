package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {

    protected final List<Task> tasks;
    protected final List<Resource> resources;

    public Scheduler(InputBuilder inputBuilder){
        resources = new ArrayList<>(inputBuilder.getResources());
        resources.forEach(Resource::setToDefault);
        tasks = new ArrayList<>(inputBuilder.getTasks());
        tasks.forEach(Task::setToDefault);
    }
    abstract void run();

    protected void printStat(String name, Double time) {
        System.out.println(name+": "+time);
        long successCount = tasks.stream().filter(Task::isSucceed).count();
        long finishedCount = tasks.stream().filter(Task::isFinished).count();
        System.out.println(finishedCount +"/"+successCount);
    }
}
