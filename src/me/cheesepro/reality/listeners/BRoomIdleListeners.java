package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BRoomIdle;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * Created by Mark on 2015-08-10.
 */
public class BRoomIdleListeners implements Listener{

    private Reality plugin;
    private DataManager dataManager;
    private BRoomManager bRoomManager;
    private BRoomIdle bRoomIdle;


    public BRoomIdleListeners(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        bRoomManager = new BRoomManager(plugin);
        bRoomIdle = new BRoomIdle(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            bRoomIdle.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            int movX = e.getFrom().getBlockX() - e.getTo().getBlockX();
            int movZ = e.getFrom().getBlockZ() - e.getTo().getBlockZ();
            if ((Math.abs(movX) > 0) || (Math.abs(movZ) > 0)) {
                bRoomIdle.setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
            }
        }
    }

}
