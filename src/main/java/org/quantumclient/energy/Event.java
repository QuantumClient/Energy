package org.quantumclient.energy;

/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
public abstract class Event {

    private boolean cancelled;

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
    
    public boolean isPre() {
        return era.equals(Era.PRE);
    }

}
