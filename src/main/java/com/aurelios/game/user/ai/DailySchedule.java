package com.aurelios.game.user.ai;

import com.aurelios.game.user.ai.tasks.*;
import com.aurelios.game.user.ai.traits.Ambition;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class DailySchedule {

    /**
     * This should have tasks for the npc to complete throughout the day/night
     * Perhaps instead of sleeping, they hangout somewhere. In their home, tavern, public area, etc.
     */

    private NPC owner;
    private List<Task> tasks;

    public DailySchedule(NPC owner) {
        this.owner = owner;
        tasks = new CopyOnWriteArrayList<>();
    }

    public Task getCurrentTask(){
        return tasks.size() > 0 ? tasks.get(0) : null;
    }

    public void generateSchedule(){
        /**
         * Ambition level should affect the duration in which they spend pursuing aspirations.
         * Use all the enums to affect these schedules, plus day of week if needed
         */

        tasks.clear();

        int hoursTotal = 24;
        int hoursWork, hoursSocial, hoursLazy, hoursAspiration = 0, hoursSocial2;
        Random random = new Random();

        hoursWork = random.nextInt(8) + 1; // 1 - 8 hours
        hoursTotal -= hoursWork;

        hoursSocial = random.nextInt(3) + 1; // 1 - 3
        hoursTotal -= hoursSocial;

        hoursLazy = random.nextInt(2) + 1; //1 - 2 hours
        hoursTotal -= hoursLazy;

        // === Definite Tasks ===
        tasks.add(new TaskWork(hoursWork, this));
        tasks.add(new TaskSocial(hoursSocial, this));
        //include lazy because they need some down time and need to be randomly accessible for players
        tasks.add(new TaskLazy(hoursLazy, this));

        // *** Definites are done here. Anywhere from 3 - 13 hours ***

        // === Dynamic Tasks ===
        if(owner.getMetaData().getAmbition() == Ambition.NORMAL){
            hoursAspiration = random.nextInt(5) + 1; //1 - 5
        } else if(owner.getMetaData().getAmbition() == Ambition.MAX){
            hoursAspiration = 5 + random.nextInt(4); // 5 - 8
        }
        hoursTotal -= hoursAspiration;
        if(hoursAspiration > 0){
            tasks.add(new TaskAspiration(hoursAspiration, this));
        }
        //hours total is now at somewhere between 3 - 20 left
        Collections.shuffle(tasks);

        if(hoursTotal <= 8){
            int temp = hoursTotal / 2;
            tasks.add(new TaskSocial(temp, this));
            tasks.add(new TaskLazy(hoursTotal - temp, this));
        } else {
            int third = hoursTotal/3;
            tasks.add(new TaskWork(third, this));
            hoursTotal -= third;
            tasks.add(new TaskSocial(third, this));
            hoursTotal -= third;
            tasks.add(new TaskLazy(hoursTotal, this));
        }

        Collections.shuffle(tasks);
        Collections.shuffle(tasks);

        for(Task task: tasks){
            System.out.println(task.getClass().getCanonicalName() + ": " + task.getHowLongToAccomplish());
        }
    }

    public void tick(){
        if(tasks.size() == 0){
            return;
        }

        if(!tasks.get(0).hasStarted()){
            tasks.get(0).start();
            System.out.println("Started: " + tasks.get(0).getClass().getCanonicalName());
        }
        if(tasks.get(0).isFinished()){
            tasks.remove(0);
        } else tasks.get(0).performTask();
    }

    public NPC getOwner() {
        return owner;
    }
}
