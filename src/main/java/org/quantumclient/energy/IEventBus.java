package org.quantumclient.energy;

/**
 * @author ChiquitaV2
 * @since 28/4/2021
 */
public interface IEventBus {

    void register(Object registerClass);

    void unregister(Object registerClass);

    void post(Event event);

}
