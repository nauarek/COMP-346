package Assignment3;

import java.util.ArrayDeque;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor {
    /*
     * ------------
     * Data members
     * ------------
     */
    private final int NUM_OF_CHOPSTICKS;
    private boolean[] chopstickState;
    private final ArrayDeque<Integer> talkingQueue;




    /**
     * Constructor
     */
    public Monitor(int piNumberOfPhilosophers) {
        // TODO: set appropriate number of chopsticks based on the # of philosophers
        NUM_OF_CHOPSTICKS = piNumberOfPhilosophers;
        chopstickState = new boolean[piNumberOfPhilosophers];
        talkingQueue = new ArrayDeque<>(NUM_OF_CHOPSTICKS);

    }

    /*
     * -------------------------------
     * User-defined monitor procedures
     * -------------------------------
     */

    /**
     * Grants request (returns) to eat when both chopsticks/forks are available.
     * Else forces the philosopher to wait()
     */
    public synchronized void pickUp(final int piTID) {
        // ...
    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int piTID) {
        // ...
    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk() {
        // ...
    }

    /**
     * When one philosopher is done talking stuff, others
     * can feel free to start talking.
     */
    public synchronized void endTalk() {
        // ...
    }
}

// EOF
