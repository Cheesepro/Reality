package me.cheesepro.reality.level;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.*;

/**
 * Created by Mark on 2015-05-02.
 */
public class LevelLimiter implements Listener{

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Config storageConfig;
    PlayerManager pManager;
    Messenger msg;
    Map<String, NavigableSet<String>> levelLimits;
    NavigableSet<Integer> levelsSet;
    NavigableSet<String> blockedItems;

    public LevelLimiter(Reality plugin){
        this.plugin = plugin;
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        pManager = new PlayerManager(plugin);
        msg = new Messenger(plugin);
        levelLimits = plugin.getLevelLimits();
        blockedItems = plugin.getBlockedItems();
        levelsSet = plugin.getLevelsSet();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);


    }

    @EventHandler
    public void onItemPickUp(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        if (blockedItems.contains(e.getItem().getItemStack().getType().name())){
            if (!(levelLimits.get(String.valueOf(pManager.getLevel(id))).contains(e.getItem().getItemStack().getType().name()))){
                e.setCancelled(true);
            }
        }

    }

}
