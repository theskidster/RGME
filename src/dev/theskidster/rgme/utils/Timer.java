package dev.theskidster.rgme.utils;

import dev.theskidster.rgme.main.App;

/**
 * @author J Hoffman
 * Created: Mar 3, 2021
 */

public final class Timer {

    public int time;
    public int speed;
    private final int initialTime;
    
    private boolean finished;
    private boolean start;
    
    public Timer(int time, int speed) {
        this.time   = time;
        this.speed  = speed;
        initialTime = time;
    }
    
    public void start() { start = true; }
    
    public boolean finished() { return finished; }
    
    public void update() {
        if(start) {
            if(time != 0) {
                if(App.tick(speed)) time--;
            } else {
                finished = true;
            }
        }
    }
    
    public void restart() {
        finished = false;
        start    = true;
        time     = initialTime;
    }
    
}