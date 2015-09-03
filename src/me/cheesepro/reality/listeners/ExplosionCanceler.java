package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Created by Mark on 2015-06-28.
 */
public class ExplosionCanceler implements Listener{

    private Reality plugin;

    public ExplosionCanceler(Reality plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.LIGHTNING) {
            e.blockList().clear();
        }else if (e.getEntityType() == EntityType.PRIMED_TNT) {
            e.blockList().clear();
        }else if (e.getEntityType() == EntityType.WITHER_SKULL) {
            e.blockList().clear();
        }else if (e.getEntityType() == EntityType.FIREBALL) {
            e.blockList().clear();
        }
    }

}
