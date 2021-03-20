package org.quantumclient.energy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
public final class EventBus {

    //I will write a readme and java docs later
    private static final HashMap<Class<? extends Event>, CopyOnWriteArrayList<Listener>> registeredListeners = new HashMap<>();

    private EventBus() {

    }

    public static void register(final Object registerClass) {
        for (Method method : registerClass.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            if (method.getParameterCount() != 1) {
                continue;
            }

            if (!Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                continue;
            }

            @SuppressWarnings("unchecked") Class<? extends Event> eventType =
                    (Class<? extends Event>) method.getParameterTypes()[0];

            Listener listener = new Listener(registerClass, method);
            if (!registeredListeners.containsKey(eventType))
                registeredListeners.put(eventType, new CopyOnWriteArrayList<>());

            registeredListeners.get(eventType).add(listener);
        }
    }

    public static void unregister(final Object listenerClass) {
        for (CopyOnWriteArrayList<Listener> listenerList : registeredListeners.values()) {
            for (int i = 0; i < listenerList.size(); i++) {
                if (listenerList.get(i).listenerClass == listenerClass) {
                    listenerList.remove(i);
                    i -= 1;
                }
            }
        }
    }

    public static void post(final Event event) {
        CopyOnWriteArrayList<Listener> listeners = registeredListeners.get(event.getClass());
        if (listeners != null) {
            for (Listener listener : listeners) {
                listener.method.setAccessible(true);
                try {
                    listener.method.invoke(listener.listenerClass, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Listener {
        private final Object listenerClass;
        private final Method method;

        private Listener(final Object listenerClass, final Method method) {
            this.listenerClass = listenerClass;
            this.method = method;
        }
    }

}
