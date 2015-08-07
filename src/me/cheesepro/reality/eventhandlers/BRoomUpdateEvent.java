package me.cheesepro.reality.eventhandlers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Mark on 2015-08-04.
 */
public class BRoomUpdateEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
