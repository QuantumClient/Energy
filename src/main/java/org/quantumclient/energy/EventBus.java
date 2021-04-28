package org.quantumclient.energy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;


/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
public class EventBus implements IEventBus {

    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    @Override
    public void register(Object registerClass) {
        for (Method method : registerClass.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) continue;

            if (method.getParameterCount() != 1) continue;

            if (!method.isAccessible()) method.setAccessible(true);

            @SuppressWarnings("unchecked") Class<? extends Event> event = (Class<? extends Event>) method.getParameterTypes()[0];

            if (!listeners.containsKey(event)) listeners.put(event, new CopyOnWriteArrayList<>());

            listeners.get(event).add(new Listener(registerClass, method));
        }
    }

    @Override
    public void unregister(Object registerClass) {
        for (CopyOnWriteArrayList<Listener> arrayList : listeners.values()) {
            arrayList.removeIf(listener -> listener.getListenerClass().equals(registerClass));
        }
    }

    @Override
    public void post(Event event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            try {
                listener.getMethod().invoke(listener.getListenerClass(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void mutliThreadPost(Event event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            ForkJoinPool.commonPool().submit(() -> {
                try {
                    listener.getMethod().invoke(listener.getListenerClass(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}