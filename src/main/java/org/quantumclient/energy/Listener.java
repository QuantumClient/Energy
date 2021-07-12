package org.quantumclient.energy;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * @author ChiquitaV2
 * @since 28/4/2021
 */
public final class Listener {

    private final Object listenerClass;
    private final Method method;
    private final Consumer<Event> lambda;

    public Listener(final Object listenerClass, final Method method, final Consumer<Event> lambda) {
        this.listenerClass = listenerClass;
        this.method = method;
        this.lambda = lambda;
    }

    public Listener(final Object listenerClass, final  Method method) {
        this(listenerClass, method, null);
    }

    public Method getMethod() {
        return method;
    }

    public Consumer<Event> getLambda() {
        return lambda;
    }

    public Object getListenerClass() {
        return listenerClass;
    }

}
