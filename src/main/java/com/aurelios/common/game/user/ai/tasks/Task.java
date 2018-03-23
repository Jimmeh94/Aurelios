package com.aurelios.common.game.user.ai.tasks;

import com.aurelios.common.game.environment.nodes.PointOfInterest;
import com.aurelios.common.game.user.ai.DailySchedule;

import java.util.List;
import java.util.Random;

public abstract class Task {

    //this is assumed to be in MC hours
    //there is 72 MC seconds per 1 real world second -> 1 MC hour = 50 real world seconds
    private int howLongToAccomplish;
    private long whenStarted = 0;
    //This is the area the NPC needs to be in to perform the task
    protected PointOfInterest location;
    private DailySchedule owner;
    private TaskStage stage;
    private TaskType taskType;

    public Task(int howLongToAccomplish, DailySchedule owner, TaskType taskType) {
        this.howLongToAccomplish = howLongToAccomplish;
        this.owner = owner;
        this.taskType = taskType;

        setLocation();
    }

    public void performTask(){
        //TODO do something with this
        if(stage == TaskStage.MOVING_TO_LOCATION){
            //When the NPC is tied to a user, then we will check if the current area == location, if so, change stage
        } else if(stage == TaskStage.PERFORMING_TASK){
            //Do some incremental thing or something
        }
    }

    private void setLocation(){
        if(taskType == TaskType.WORK) {
            //Here we are assuming that where the NPC calls home, there's at least 1 area that they can use for work.
            //Meaning an applicable POI
            List<PointOfInterest> possible = owner.getOwner().getMetaData().getHome().getPOIsWithRole(owner.getOwner().getMetaData().getRole());
            location = possible.get((new Random()).nextInt(possible.size()));
        } else if(taskType == TaskType.ASPIRATION){

        } else if(taskType == TaskType.SOCIAL){

        } else if(taskType == TaskType.LAZY){

        }
    }

    public DailySchedule getOwner() {
        return owner;
    }

    public PointOfInterest getLocation() {
        return location;
    }

    public boolean isFinished(){
        //howLongToAccomplish is in MC hours
        //there is 72 MC seconds per 1 real world second -> 1 MC hour = 50 real world seconds
        int realSecondsPassed = (int) ((System.currentTimeMillis() - whenStarted)/1000);
        int howLong = 50 * howLongToAccomplish;
        return realSecondsPassed >= howLong;
    }

    public boolean hasStarted(){return whenStarted != 0;}

    public void start(){
        this.whenStarted = System.currentTimeMillis();
        stage = TaskStage.MOVING_TO_LOCATION;
    }

    public int getHowLongToAccomplish() {
        return howLongToAccomplish;
    }

    private enum TaskStage{
        MOVING_TO_LOCATION,
        PERFORMING_TASK
    }

    public enum TaskType{
        SOCIAL,
        WORK,
        LAZY, ASPIRATION
    }
}
