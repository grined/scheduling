package com.zelteam.schedule;

import com.zelteam.input.Inputable;
import com.zelteam.model.Resource;
import com.zelteam.model.Task;
import sun.text.CollatorUtilities;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepScheduler extends Scheduler {
    private LinkedList<Resource> reputationList;
    private LinkedList<Task> deadlineFactorList;
    private boolean useFirst = true;
    private int nextIndex;

    public RepScheduler(Inputable inputable) {
        super(inputable);
        reputationList = new LinkedList<>(resources);
        deadlineFactorList = new LinkedList<>();
    }

    @Override
    public void run(){
        Double time = 0d;
        ListIterator<Task> taskIterator = tasks.listIterator();
        while (true) {
            //Check resources to finish their tasks and update rrep
            checkResourceToFinish(time);
            //Check for new tasks will appear and update df
            if (checkForNewTasks(time, taskIterator)){
                sortDF(time);
            }
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

        printStat("Rep",time);

    }

    private void manageTasks(Double time) {
        Resource firstNotBusyResource = findFirstNotBusyResource();
        Task firstTask = !deadlineFactorList.isEmpty()
                ?(useFirst
                    ?deadlineFactorList.getFirst()
                    :deadlineFactorList.getLast())
                : null;
        if (firstNotBusyResource!= null && firstTask != null){
            firstNotBusyResource.applyTask(firstTask, time);
            deadlineFactorList.remove(firstTask);
            useFirst = !useFirst;
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

    private boolean checkForNewTasks(final Double time, ListIterator<Task> taskIterator) {
        boolean added = false;
        if (taskIterator.hasNext()){
            Task next = taskIterator.next();
            if (next.getAppearTime()<=time){
                deadlineFactorList.add(next);
                added = true;
                checkForNewTasks(time, taskIterator);
            } else {
                taskIterator.previous();
            }
        }
        if (added){
            System.out.println(taskIterator.nextIndex()*100 / tasks.size() + "%");
        }
        return added;
    }

    private void sortDF(Double time) {
        ListIterator<Task> taskIterator;
        try {
            taskIterator = deadlineFactorList.listIterator(nextIndex);
        } catch (IndexOutOfBoundsException e) {
            taskIterator = deadlineFactorList.listIterator();
        }
        while (taskIterator.hasNext()) {
            if (taskIterator.next().calcDeadlineFactor(time) != 1) {
                taskIterator.previous();
                break;
            }
        }
        while (taskIterator.hasPrevious()) {
            if (taskIterator.previous().calcDeadlineFactor(time) == 1) {
                taskIterator.next();
                break;
            }
        }
        nextIndex = taskIterator.nextIndex();

        List<Task> tail = deadlineFactorList.subList(nextIndex, deadlineFactorList.size());

        Collections.sort(tail, (t2, t1) -> {
            double v1 = t1.calcDeadlineFactor(time);
            double v2 = t2.calcDeadlineFactor(time);
            if (Double.compare(v1, v2) == 0) {
                return Double.compare(t2.getAppearTime(), t1.getAppearTime());
            }
            return Double.compare(v1, v2);
        });
    }

    private void checkResourceToFinish(final Double time) {
        reputationList.stream()
                .filter(r -> r.whenFinished() <= time)
                .forEachOrdered(r-> r.finishTask(time));
        //RRep updated and we have to sort data
        reputationList.sort((r2,r1) ->{
            double v1 = r1.getRRep();
            double v2 = r2.getRRep();
            if (Double.compare(v1, v2) == 0){
                return Double.compare(r1.getSpeed(),r2.getSpeed());
            }
            return Double.compare(v1, v2);
        });
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
