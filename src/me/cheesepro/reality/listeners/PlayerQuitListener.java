package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoom;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mark on 2015-08-01.
 */
public class PlayerQuitListener implements Listener{

    private Reality plugin;
    private BRoomManager bRoomManager;
    private DataManager dataManager;

    public PlayerQuitListener(Reality plugin){
        this.plugin = plugin;
        bRoomManager = new BRoomManager(plugin);
        dataManager = new DataManager(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        BRoom bRoom = bRoomManager.getBRoom(e.getPlayer());
        if(bRoom != null){
            bRoom.disconnectPlayer(e.getPlayer());
            dataManager.setQuitBRoom(e.getPlayer(), bRoom.getBRoomName());
        }
    }

}
