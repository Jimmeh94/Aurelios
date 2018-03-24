package com.aurelios.server.runnable;

import com.aurelios.Aurelios;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public abstract class AbstractTimer implements Runnable {

    protected Task task;
    private int intervalsPassed = 0, cancelAt = -1;
    private long interval, delay;

    protected abstract void runTask();

    public AbstractTimer(long interval){
        this(interval, 0L);
    }

    public AbstractTimer(long interval, long delay){
        this(interval, delay, -1);
    }

    public AbstractTimer(long interval, long delay, int cancelAt){
        this.interval = interval;
        this.delay = delay;
        this.cancelAt = cancelAt;
    }

    public void start(){
        Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
        task = taskBuilder.delayTicks(delay).intervalTicks(interval).execute(this).submit(Aurelios.INSTANCE);
    }

    public void stop(){
        if (task == null)
            return;
        try {
            task.cancel();
            task = null;
        } catch (IllegalStateException exc) {
        }
    }

    @Override
    public void run() {
        intervalsPassed++;
        if(cancelAt != -1 && intervalsPassed >= cancelAt / interval){
            stop();
        }
        runTask();
    }

    public Task getTask() {
        return task;
    }
}
