package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.bosses.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;

import java.util.*;

/**
 * Created by Mark on 2015-07-19.
 */
public class BossesAPI {

    Reality plugin;
    Bosses[] bosses;

    public BossesAPI(Reality plugin){
        this.plugin = plugin;
        bosses = plugin.getBosses();
    }

    public void basicSetup(Creature animals, String name, int health) {
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

}
