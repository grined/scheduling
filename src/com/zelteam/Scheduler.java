package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Site;
import com.zelteam.model.Task;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {

    protected final List<Site> sites;
    protected final List<Task> tasks;
    protected final List<Resource> resources;

    public Scheduler(InputBuilder inputBuilder){
        sites = inputBuilder.getSites();
        resources = new ArrayList<>(inputBuilder.getResources());
        resources.forEach(Resource::setToDefault);
        tasks = new ArrayList<>(inputBuilder.getTasks());
        tasks.forEach(Task::setToDefault);
    }
    abstract void run();

    protected void printStat(String name, Double time) {
        NumberFormat numberFormat = new DecimalFormat("#.##");
        System.out.println(name+": "+numberFormat.format(time) +"ms");
        System.out.println("Sites count: "+sites.size());
        System.out.println("Resources count: "+resources.size());
        System.out.println("Task count: "+tasks.size());
        long successCount = tasks.stream().filter(Task::isSucceed).count();
        long finishedCount = tasks.stream().filter(Task::isFinished).count();
        System.out.println("Finished tasks / success tasks : " +finishedCount +"/"+successCount);
        System.out.println("Success rate : " + numberFormat.format((successCount+.0)/finishedCount));
    }
}
