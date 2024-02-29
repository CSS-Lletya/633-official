package com.rs.utilities;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Represents a timer in seconds.
 *
 * @author Professor Oak
 */
public class SecondsTimer {

    /**
     * The actual timer.
     */
    private final Stopwatch stopwatch = Stopwatch.createUnstarted();
    /**
     * The amount of seconds to count down.
     */
    private int seconds;

    /**
     * Constructs a new timer.
     */
    public SecondsTimer() {
    }

    /**
     * Constructs a new timer and
     * starts it immediately.
     *
     * @param seconds The amount of seconds to
     */
    public SecondsTimer(int seconds) {
        start(seconds);
    }

    /**
     * Starts this timer.
     *
     * @param seconds The amount of seconds.
     */
    public void start(int seconds) {
        this.seconds = seconds;

        //Reset and then start the stopwatch.
        stopwatch.reset();
        stopwatch.start();
    }

    /**
     * Stops this timer
     */
    public void stop() {
        seconds = 0;
        stopwatch.reset();
    }

    /**
     * Checks if this timer has finished
     * counting down, basically reaching 0.
     *
     * @return true if finished, false otherwise.
     */
    public boolean finished() {
        if (seconds == 0) {
            return true;
        }
        return stopwatch.elapsed(TimeUnit.MILLISECONDS) >=
                seconds * 1000;
    }

    /**
     * Gets the amount of seconds remaining
     * before this timer has reached 0.
     *
     * @return The seconds remaining.
     */
    public int secondsRemaining() {
        return seconds - secondsElapsed();
    }
    
    /**
     * Gets the amount of ticks remaining
     * before this timer has reached 0.
     * @return The ticks remaining.
     */
    public int ticksRemaining() {
    	return seconds - ticksElapsed();
    }
    
    public int getMinutes() {
    	return secondsRemaining() / 60;
    }

    /**
     * Gets the amount of seconds that have elapsed
     * since the timer was started.
     *
     * @return The seconds elapsed.
     */
    public int secondsElapsed() {
        return (int) stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
    }
    
    /**
     * Gets the amount of ticks that have elapsed
     * since the timer was started.
     *
     * @return The ticks elapsed.
     */
    public int ticksElapsed() {
        return (int) stopwatch.elapsed(TimeUnit.MILLISECONDS) / 600;
    }
}