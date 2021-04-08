package org.quantumclient.energy;

/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
public abstract class Event {

    private boolean cancelled;

    // Some things (rendering) might need to run on a native thread if so change this to true
    protected boolean multiThreaded = true;

    private Era era;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public Era getEra() {
        return era;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public boolean shouldMuliThread() {
        return multiThreaded;
    }
}
