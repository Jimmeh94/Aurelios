package com.aurelios.util.misc;

import com.aurelios.managers.Managers;
import org.spongepowered.api.Sponge;

import java.text.DecimalFormat;

public class Calendar {

    private DayOfWeek currentDayOfWeek;
    private Time time;

    public Calendar(){
        time = new Time(this);
        tick();
        setCurrentDayOfWeek();
    }

    public void tick(){
        time.tick();
    }

    public DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

    public Time getCurrentTime() {
        return time;
    }

    private void setCurrentDayOfWeek(){
        if(currentDayOfWeek == null){
            //total ticks / 20 = amount of real world seconds
            // / 1200 = amount of in-game days (1200 real world seconds = 1 in-game day)
            int totalDays = (int) (((time.totalTicks)/20)/1200);

            if(totalDays % 7 != 0){
                totalDays %= 7;
            }
            currentDayOfWeek = DayOfWeek.values()[totalDays];
        } else currentDayOfWeek = DayOfWeek.getNextDay(currentDayOfWeek);
    }

    public enum DayOfWeek{
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;

        public static DayOfWeek getNextDay(DayOfWeek d){
            switch (d){
                case MONDAY: return TUESDAY;
                case TUESDAY: return WEDNESDAY;
                case WEDNESDAY: return THURSDAY;
                case THURSDAY: return FRIDAY;
                case FRIDAY: return SATURDAY;
                case SATURDAY: return SUNDAY;
                case SUNDAY: return MONDAY;
            }
            return d;
        }
    }

    public static class Time{
        private static final DecimalFormat format = new DecimalFormat("#00");
        //24,000 (0) ticks = 6 am in MC
        private int hours = 6, minutes, seconds = -1;
        private long totalTicks;
        private Calendar calendar;

        public Time(Calendar calendar) {
            this.calendar = calendar;
        }

        public void tick(){
            if(seconds == -1){
                //how many ticks have passed in the current day
                totalTicks = Sponge.getServer().getWorld("world").get().getProperties().getWorldTime() % 24000;
                //real time seconds
                seconds = (int) (totalTicks/20);
                //roughly 72 MC seconds = 1 real second
                seconds *= 72;
            } else {
                //this is ticking every real world second, so we're adding the MC equivalent of 1 real world second
                seconds += 72;
            }

            if(seconds >= 60){
                minutes += seconds/60;
                seconds %= 60;
            }
            if(minutes >= 60){
                hours += minutes/60;
                minutes %= 60;
            }
            if(hours >= 24){
                hours -= 24;
                if(hours == 0){
                    hours = 1;
                    calendar.setCurrentDayOfWeek();
                    Managers.AI.regenerateDailySchedules();
                }
            }
        }

        public String getFormattedTime(boolean formatTime){
            int tempHours = hours;
            if(formatTime && hours > 12){
                tempHours -= 12;
            }
            return "" + tempHours + ":" + format.format(minutes);
        }

        public long getTotalTicks() {
            return totalTicks;
        }
    }

}
