package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.GraphicalAPI;
import me.cheesepro.reality.utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-04-03.
 */
public class RespawnListener implements Listener{

    private Reality plugin;
    private Map<String, List<String>> settings;
    private RankManager rankManager;
    private DataManager dataManager;

    public RespawnListener(Reality plugin){
        this.plugin = plugin;
        settings = plugin.getSettings();
        rankManager = new RankManager(plugin);
        dataManager = new DataManager(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void respawnListener(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        new BukkitRunnable(){
            @Override
            public void run(){
                p.setGameMode(GameMode.CREATIVE);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }.runTaskLater(plugin, 20);
        p.setCanPickupItems(true);
        if (!dataManager.getInGamePlayersList().contains(p.getUniqueId())) {
            Location loc = new Location(Bukkit.getWorld(settings.get("respawnlocation").get(0)),
                    Double.parseDouble(settings.get("respawnlocation").get(1)),
                    Double.parseDouble(settings.get("respawnlocation").get(2)),
                    Double.parseDouble(settings.get("respawnlocation").get(3)),
                    Float.parseFloat(settings.get("respawnlocation").get(4)),
                    Float.parseFloat(settings.get("respawnlocation").get(5)));
            e.setRespawnLocation(loc);
            rankManager.clearRank(p);
            p.setMaxHealth(2.0);
            GraphicalAPI.sendTitleToPlayer(p, 1, 4, 0, p.getName(), "You just lost your body");
            new BukkitRunnable(){
                @Override
                public void run(){
                    GraphicalAPI.sendTitleToPlayer(p, 0, 5, 1, ChatColor.YELLOW + "Please", "go to the door of rebirth");
                }
            }.runTaskLater(plugin, 5 * 20);
        }
    }

}