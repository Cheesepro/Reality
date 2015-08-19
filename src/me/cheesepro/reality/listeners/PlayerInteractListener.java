package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Mark on 2015-08-18.
 */
public class PlayerInteractListener implements Listener{

    private Reality plugin;

    public PlayerInteractListener(Reality plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        if(CitizensAPI.getNPCRegistry().isNPC(e.getRightClicked())){
            if(e.getRightClicked().getCustomName().startsWith(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS")){
               e.setCancelled(true);
            }
        }
    }
}
