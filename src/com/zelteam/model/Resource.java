package com.zelteam.model;

public class Resource {
    private final double speed;
    private Task currentTask;

    private int completedTasks = 0;
    private int successfulTasks = 0;
    private double executionTime = 0;
    private double waitingTime = 0d;
    private double freeFrom = 0d;

    private double rRep = 0d;

    public Resource(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public double getRRep() {
        return rRep;
    }

    public void setToDefault(){
        currentTask = null;
        completedTasks = 0;
        successfulTasks = 0;
        executionTime = 0;
        waitingTime = 0d;
        freeFrom = 0d;
        rRep = 0d;
    }

    public void applyTask(Task task, double time) {
        currentTask = task;
        task.setStartedAt(time);
        waitingTime += time - freeFrom;
    }

    public boolean isBusy(){
        return currentTask != null;
    }

    public double whenFinished(){
        if (currentTask == null) {
            return Double.MAX_VALUE;
        }
        return currentTask.getStartedAt() + currentTask.getComputationalSize() / speed;
    }

    public void finishTask(double time) {
        currentTask.setFinished(true);
        completedTasks++;
        if (currentTask.getDeadlineTime() >= time){
            successfulTasks++;
            currentTask.setSucceed(true);
        }
        executionTime += currentTask.getComputationalSize() / speed;
        freeFrom = time;
        currentTask = null;
        updateResourceReputation();
    }

    private double updateResourceReputation() {
        double pr = completedTasks/executionTime + waitingTime/completedTasks;
        double ppr = successfulTasks + executionTime/completedTasks;
        rRep = ppr / pr;
        return rRep;
    }


}
