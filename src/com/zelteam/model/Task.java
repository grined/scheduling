package com.zelteam.model;

public class Task {
    private final double computationalSize;
    private final double appearTime;
    private final double deadlineTime;
    private double startedAt;
    private boolean finished;
    private boolean succeed;

    public Task(double computationalSize, double appearTime, double slowestResourceSpeed) {
        this.computationalSize = computationalSize;
        this.appearTime = appearTime;
        this.deadlineTime = appearTime + computationalSize/slowestResourceSpeed;
    }

    public void setToDefault(){
        startedAt = 0d;
        finished = false;
        succeed = false;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public double getComputationalSize() {
        return computationalSize;
    }

    public double getAppearTime() {
        return appearTime;
    }

    public double getDeadlineTime() {
        return deadlineTime;
    }

    public double getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(double startedAt) {
        this.startedAt = startedAt;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public double calcDeadlineFactor(Double time) {
        double toDeadlineLeft = deadlineTime - time;
        if (toDeadlineLeft <= 0) {
            return 1d;
        }
        double timeToExecute = deadlineTime - appearTime;
        double factor = toDeadlineLeft / timeToExecute;
        if (factor >= 0.999d) {
            return 0d;
        } else if (factor > 0.75d){
            return 0.05d;
        } else if (factor > 0.5d){
            return 0.25d;
        }  else {
            return 0.5d;
        }
    }
}
