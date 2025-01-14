package com.craftmend.openaudiomc.api.impl.event;

import com.craftmend.openaudiomc.generic.logging.OpenAudioLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a stupidly simple event driver, replacing the Bukkit event system
 * for internal and api purposes.
 *
 * Key feature is the fact that it doesn't depend on any bukkit/spigot services, making it save
 * and functional on other platforms as well (bungee, velocity, testing)
 */
public class ApiEventDriver {

    /**
     * This maps raw event implementation classes to a set of handler holders of the same type
     * note that this isn't a concurrent hashmap, which shouldn't really be a problem since I don't
     * expect that there'll be events registered during normal application flow, but is important
     * since most (if not all) events are async for networking and platform reasons.
     */
    private HashMap<Class<? extends AudioEvent>, Set<HandlerHolder<? extends AudioEvent>>> handlers = new HashMap<>();


    /**
     * Internal method to find listeners for a specific event.
     * @param eventClass Specific event implementation to match
     * @return A collection of callable event listeners, or an empty set if there are none.
     */
    private Set<HandlerHolder<? extends AudioEvent>> getHandlersFor(Class<? extends AudioEvent> eventClass) {
        return  handlers.getOrDefault(eventClass, new HashSet<>());
    }

    /**
     * Public API method to create an event listener for a specific event.
     * This returns a new event holder, which will store your event callback.
     * @param eventType The event type you want to listen for
     * @return Your new event handler, which you can fill using the `setHandler` method
     */
    public <T extends AudioEvent> HandlerHolder<T> on(Class<T> eventType) {
        HandlerHolder<T> holder = new HandlerHolder(eventType);
        Set<HandlerHolder<? extends AudioEvent>> subscribers = getHandlersFor(eventType);
        subscribers.add(holder);
        handlers.put(eventType, subscribers);
        return holder;
    }

    /**
     * Fires an event for all listeners of the same type, and shares the same instance between all of them.
     * Individual listeners will be caught if they throw an exception, as to not disturb or break the flow.
     * @param event instance to fire
     * @return instance that got fired, possibly mutated if the event was cancellable or had any other setters
     */
    public <T extends AudioEvent> T fire(T event) {
        // get handlers
        Set<HandlerHolder<? extends AudioEvent>> subscribers = getHandlersFor(event.getClass());
        for (HandlerHolder<? extends AudioEvent> subscriber : subscribers) {
            if (subscriber.getHandler() == null) {
                throw new IllegalStateException("There was a subscriber for " + event.getClass().getSimpleName() + " that doesn't have an executor! moving on.");
            } else {
                try {
                    subscriber.call(event);
                } catch (Exception e) {
                    OpenAudioLogger.toConsole("Failed to handle an event handler");
                    e.printStackTrace();
                }
            }
        }
        return event;
    }

}
