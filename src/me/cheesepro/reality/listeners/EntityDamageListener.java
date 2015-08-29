package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.bossrooms.bosses.*;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Mark on 2015-08-16.
 */
public class EntityDamageListener implements Listener{

    private Reality plugin;
    private BossesAPI bossesAPI;
    private BossSpider bossSpider;
    private BossCreeper bossCreeper;
    private BossSkeleton bossSkeleton;
    private BossBlaze bossBlaze;
    private BossEnderman bossEnderman;
    private BossCow bossCow;
    private BossPig bossPig;

    public EntityDamageListener(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
        bossSpider = new BossSpider(plugin);
        bossCreeper = new BossCreeper(plugin);
        bossSkeleton = new BossSkeleton(plugin);
        bossBlaze = new BossBlaze(plugin);
        bossEnderman = new BossEnderman(plugin);
        bossCow = new BossCow(plugin);
        bossPig = new BossPig(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getDamager().isCustomNameVisible()){
            if(e.getDamager().getCustomName().startsWith(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS ")){
                if(CitizensAPI.getNPCRegistry().isNPC(e.getDamager())){
                    if(bossesAPI.getBoss(e.getDamager().getType().name())!=null){
                        if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("spider")){
                            bossSpider.useAbility((Player)e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("cow")){
                            bossCow.onHit((Player) e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("pig")){
                            bossPig.onHit((Player) e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("creeper")){
                            bossCreeper.useAbility((Player)e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("skeleton")){
                            bossSkeleton.useAbility((Player)e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("blaze")){
                            bossBlaze.useAbility((Player) e.getEntity());
                        }else if(bossesAPI.getBoss(e.getDamager().getType().name()).getType().equalsIgnoreCase("enderman")){
                            bossEnderman.onHit((Player)e.getEntity());
                        }
                        e.setDamage(bossesAPI.getBoss(e.getDamager().getType().name()).getDamage());
                    }
                }
            }
        }
    }
}
