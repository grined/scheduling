package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Task;

import java.util.LinkedList;
import java.util.ListIterator;

public class NoRepScheduler extends Scheduler {
    private LinkedList<Resource> reputationList;
    private LinkedList<Task> deadlineFactorList;

    public NoRepScheduler(InputBuilder inputBuilder) {
        super(inputBuilder);
        reputationList = new LinkedList<>(resources);
        deadlineFactorList = new LinkedList<>();
    }

    @Override
    public void run(){
        Double time = 0d;
        ListIterator<Task> taskIterator = tasks.listIterator();
        reputationList.sort((r2,r1) -> Double.compare(r1.getSpeed(),r2.getSpeed()));
        while (true) {
            //Check resources to finish their tasks and update rrep
            checkResourceToFinish(time);
            //Check for new tasks will appear and update df
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
        Task firstTask = !deadlineFactorList.isEmpty() ? deadlineFactorList.getFirst() : null;
        if (firstNotBusyResource!= null && firstTask != null){
            firstNotBusyResource.applyTask(firstTask, time);
            deadlineFactorList.remove(firstTask);
            manageTasks(time);
        }
    }

    private Resource findFirstNotBusyResource() {
        for (Resource resource : reputationList) {
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
                deadlineFactorList.add(next);
                checkForNewTasks(time, taskIterator);
            } else {
                taskIterator.previous();
            }
        }
        //No sort here!
    }

    private void checkResourceToFinish(final Double time) {
        reputationList.stream()
                .filter(r -> r.whenFinished() <= time)
                .forEachOrdered(r-> r.finishTask(time));
        //Here we do not sort data!
    }

    private double findFirstActionWillHappen(ListIterator<Task> taskIterator) {
        double firstFinishedTask = reputationList.stream()
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
