package org.quantumclient.energy;

import java.lang.reflect.Method;

/**
 * @author ChiquitaV2
 * @since 28/4/2021
 */
public class Listener {

    private final Object listenerClass;
    private final Method method;

    public Listener(final Object listenerClass, final Method method) {
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public Object getListenerClass() {
        return listenerClass;
    }

}
