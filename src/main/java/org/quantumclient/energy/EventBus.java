package org.quantumclient.energy;


import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;


/**
 * @author ChiquitaV2
 * @since 19/3/2021
 */
public class EventBus implements IEventBus {

    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    @Override
    public void register(Object registerClass) {
        Arrays.stream(registerClass.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Subscribe.class))
                .filter(method -> method.getParameterCount() == 1)
                .forEach(method -> {
            if (!method.isAccessible()) method.setAccessible(true);

            @SuppressWarnings("unchecked") Class<? extends Event> event =
                    (Class<? extends Event>) method.getParameterTypes()[0];

            Consumer<Event> lambda = null;
            if (method.getDeclaredAnnotation(Subscribe.class).lambda()) {
                lambda = getLambda(registerClass, method, event);
            }
            if (!listeners.containsKey(event)) listeners.put(event, new CopyOnWriteArrayList<>());

            listeners.get(event).add(new Listener(registerClass, method, lambda));
        });
    }

    @Override
    public void unregister(Object registerClass) {
        listeners.values().forEach(arrayList -> {
            arrayList.removeIf(listener -> listener.getListenerClass().equals(registerClass));
        });
    }

    @Override
    public void post(Event event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            if (listener.getLambda() != null) {
                listener.getLambda().accept(event);
            } else {
                try {
                    listener.getMethod().invoke(listener.getListenerClass(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void mutliThreadPost(Event event) {
        List<Listener> listenersList = listeners.get(event.getClass());
        if (listenersList != null) for (Listener listener : listenersList) {
            if (event.isCancelled()) return;
            ForkJoinPool.commonPool().submit(() -> {
                if (listener.getLambda() != null) {
                    listener.getLambda().accept(event);
                } else {
                    try {
                        listener.getMethod().invoke(listener.getListenerClass(), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected Consumer<Event> getLambda(Object o, Method method, Class<? extends Event> event) {
        Consumer<Event> eventLambda = null;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodType subscription = MethodType.methodType(void.class, event);
            MethodHandle target = lookup.findVirtual(o.getClass(), method.getName(), subscription);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, o.getClass()),
                    subscription.changeParameterType(0, Object.class),
                    target,
                    subscription);

            MethodHandle factory = site.getTarget();
            eventLambda = (Consumer<Event>) factory.bindTo(o).invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return eventLambda;
    }

}