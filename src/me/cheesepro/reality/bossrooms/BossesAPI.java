package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import org.bukkit.ChatColor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

/**
 * Created by Mark on 2015-07-19.
 */
public class BossesAPI {

    private Reality plugin;
    private Bosses[] bosses;

    public BossesAPI(Reality plugin){
        this.plugin = plugin;
        bosses = plugin.getBosses();
    }

    public void basicSetup(Creature animals, String name, int health) {
        animals.setMaxHealth(health * 2.0);
        animals.setHealth(health * 2.0);
        animals.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS " + ChatColor.BOLD + name);
        animals.setCustomNameVisible(true);
    }

    public Bosses getBoss(String type){
        for(Bosses boss : bosses){
            if(boss.getType().equalsIgnoreCase(type)){
                return boss;
            }
        }
        return null;
    }

    private void setMaxHealth(Entity entity, int maxhealth){
        ((Damageable) entity).setMaxHealth(maxhealth);
    }

}
