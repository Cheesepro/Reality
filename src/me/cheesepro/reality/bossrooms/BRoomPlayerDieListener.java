package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mark on 2015-08-01.
 */
public class BRoomPlayerDieListener implements Listener{

    private Reality plugin;
    private BRoomManager bRoomManager;
    private DataManager dataManager;

    public BRoomPlayerDieListener(Reality plugin){
        this.plugin = plugin;
        bRoomManager = new BRoomManager(plugin);
        dataManager = new DataManager(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDie(final PlayerDeathEvent e){
        if(bRoomManager.getBRoom(e.getEntity())==null) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getEntity().spigot().respawn();
                e.getEntity().setCanPickupItems(false);
                if(!dataManager.getBRoomPlayersRole(e.getEntity().getUniqueId())){
                    e.getEntity().teleport(bRoomManager.getBRoom(e.getEntity()).getSpectate());
                }
                bRoomManager.getBRoom(e.getEntity()).playerDie(e.getEntity());
            }
        }.runTaskLater(plugin, 5);
    }

}
