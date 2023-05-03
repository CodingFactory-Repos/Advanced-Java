package me.loule.hipopothalous.controller;
import java.lang.Thread;


public class Chronometry {

    private Thread ChronoThread;
    public int seconds=60;
    public int minutes=25;
    public boolean isRunning;
    protected void initializeChronometry(boolean isServiceChrono) {
        isRunning=true;
        if (isServiceChrono){
        }
        else{
            ChronoThread = new Thread(() -> {
                while (isRunning) {
                    try {
                        if (minutes==0 && seconds==0){
                            isRunning=false;
                        }
                        else if (minutes == 0){
                            seconds = seconds-1;
                        }
                        else if (seconds==0){
                            seconds =60;
                            minutes = minutes - 1;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                        return;
                    }
                }
            });
        }

    }
}

