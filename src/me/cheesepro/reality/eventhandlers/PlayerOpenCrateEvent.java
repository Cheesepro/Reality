package me.cheesepro.reality.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Mark on 2015-07-07.
 */
public class PlayerOpenCrateEvent extends Event {

    Player p;

    public PlayerOpenCrateEvent(Player p){
        this.p = p;
    }

    public Player getPlayer(){
        return p;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
