package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Task;

import java.util.LinkedList;
import java.util.ListIterator;

public class NoRepScheduler extends Scheduler {
    private LinkedList<Resource> resourceLinkedList;
    private LinkedList<Task> taskLinkedList;

    public NoRepScheduler(InputBuilder inputBuilder) {
        super(inputBuilder);
        resourceLinkedList = new LinkedList<>(resources);
        taskLinkedList = new LinkedList<>();
    }

    @Override
    public void run(){
        Double time = 0d;
        ListIterator<Task> taskIterator = tasks.listIterator();
        resourceLinkedList.sort((r2, r1) -> Double.compare(r1.getSpeed(), r2.getSpeed()));
        while (true) {
            //Check resources to finish their tasks
            checkResourceToFinish(time);
            //Check for new tasks will appear
            checkForNewTasks(time, taskIterator);
            //Manage tasks
            manageTasks(time);
            //Find first action witch will happen
            double min = findFirstActionWillHappen(taskIterator);
            // If all values are INF , that means that all resources are free and there are no tasks in queue
            if (min == Double.MAX_VALUE){
                break;
            }
            time = min;
        }
        printStat("NoRep", time);
    }

    private void manageTasks(Double time) {
        Resource firstNotBusyResource = findFirstNotBusyResource();
        Task firstTask = !taskLinkedList.isEmpty() ? taskLinkedList.getFirst() : null;
        if (firstNotBusyResource!= null && firstTask != null){
            firstNotBusyResource.applyTask(firstTask, time);
            taskLinkedList.remove(firstTask);
            manageTasks(time);
        }
    }

    private Resource findFirstNotBusyResource() {
        for (Resource resource : resourceLinkedList) {
            if (!resource.isBusy()) {
                return resource;
            }
        }
        return null;
    }

    private void checkForNewTasks(final Double time, ListIterator<Task> taskIterator) {
        if (taskIterator.hasNext()){
            Task next = taskIterator.next();
            if (next.getAppearTime()<=time){
                taskLinkedList.add(next);
                checkForNewTasks(time, taskIterator);
            } else {
                taskIterator.previous();
            }
        }
        //No sort here!
    }

    private void checkResourceToFinish(final Double time) {
        resourceLinkedList.stream()
                .filter(r -> r.whenFinished() <= time)
                .forEachOrdered(r-> r.finishTask(time));
        //Here we do not sort data!
    }

    private double findFirstActionWillHappen(ListIterator<Task> taskIterator) {
        double firstFinishedTask = resourceLinkedList.stream()
                .filter(Resource::isBusy)
                .mapToDouble(Resource::whenFinished)
                .min()
                .orElse(Double.MAX_VALUE);
        double nextTaskAppear = Double.MAX_VALUE;
        if (taskIterator.hasNext()){
            int nextIndex = taskIterator.nextIndex();
            nextTaskAppear = tasks.get(nextIndex).getAppearTime();
        }

        return Double.min(firstFinishedTask, nextTaskAppear);
    }

}
