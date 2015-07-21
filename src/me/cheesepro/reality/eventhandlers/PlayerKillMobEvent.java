package me.cheesepro.reality.eventhandlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Mark on 2015-07-05.
 */
public class PlayerKillMobEvent extends Event{

    Player p;
    Entity mob;

    public PlayerKillMobEvent(Player p, Entity mob){
        this.p = p;
        this.mob = mob;
    }

    public Player getPlayer(){
        return p;
    }

    public Entity getMob(){
        return mob;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
