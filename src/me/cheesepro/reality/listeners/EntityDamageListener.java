package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BossesAPI;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Mark on 2015-08-16.
 */
public class EntityDamageListener implements Listener{

    private Reality plugin;
    private BossesAPI bossesAPI;

    public EntityDamageListener(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager().isCustomNameVisible()){
            if(e.getDamager().getCustomName().startsWith(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS ")){
                if(CitizensAPI.getNPCRegistry().isNPC(e.getDamager())){
                    if(bossesAPI.getBoss(e.getDamager().getType().name())!=null){
                        e.setDamage(bossesAPI.getBoss(e.getDamager().getType().name()).getDamage());
                    }
                }
            }
        }
    }
}
