package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-10.
 */
public class BRoomIdleListeners implements Listener{

    private Reality plugin;
    private DataManager dataManager;
    private BRoomManager bRoomManager;
    private static Map<UUID, Integer> idleCount = new HashMap<UUID, Integer>();
    private static boolean isIdleTaskRunning;
    private Messenger msg;

    public BRoomIdleListeners(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        bRoomManager = new BRoomManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
            setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (!e.isCancelled()) {
            if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
                setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        if (!e.isCancelled()) {
            if(dataManager.getInGamePlayersList().contains(e.getPlayer().getUniqueId())){
                setIdle(e.getPlayer(), bRoomManager.getBRoom(e.getPlayer()).getIdleTimeout());
            }
        }
    }

}
