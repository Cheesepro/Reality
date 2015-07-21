package me.cheesepro.reality.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.types.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;

import java.util.*;

/**
 * Created by Mark on 2015-07-19.
 */
public class BossesSetup {

    Reality plugin;
    Map<String, Map<String, Double>> bossesLocations;
    String bossesWorld;

    BossChicken bossChicken;
    BossBlaze bossBlaze;
    BossCow bossCow;
    BossCreeper bossCreeper;
    BossEnderman bossEnderman;
    BossPig bossPig;
    BossSkeleton bossSkeleton;
    BossSpider bossSpider;
    BossZombie bossZombie;

    public BossesSetup(Reality plugin){
        this.plugin = plugin;
        bossesLocations = plugin.getBossesLocations();
        bossesWorld = plugin.getBossesWorld();

        bossChicken = new BossChicken(plugin);
        bossBlaze = new BossBlaze(plugin);
        bossCow = new BossCow(plugin);
        bossCreeper = new BossCreeper(plugin);
        bossEnderman = new BossEnderman(plugin);
        bossPig = new BossPig(plugin);
        bossSkeleton = new BossSkeleton(plugin);
        bossSpider = new BossSpider(plugin);
        bossZombie = new BossZombie(plugin);
    }

    public void initialSetup(){
        if(bossesWorld!=null && bossesLocations!=null){
            Set<String> bosses = new HashSet<String>(bossesLocations.keySet());
            if(bosses.contains("chicken")){bossChicken.spawn(bossesWorld, bossesLocations.get("chicken").get("x"), bossesLocations.get("chicken").get("y"), bossesLocations.get("chicken").get("z"));}
            if(bosses.contains("cow")){bossCow.spawn(bossesWorld, bossesLocations.get("cow").get("x"), bossesLocations.get("cow").get("y"), bossesLocations.get("cow").get("z"));}
            if(bosses.contains("pig")){bossPig.spawn(bossesWorld,bossesLocations.get("pig").get("x"), bossesLocations.get("pig").get("y"), bossesLocations.get("pig").get("z"));}
            if(bosses.contains("enderman")){bossEnderman.spawn(bossesWorld, bossesLocations.get("enderman").get("x"), bossesLocations.get("enderman").get("y"), bossesLocations.get("enderman").get("z"));}
            if(bosses.contains("spider")){bossSpider.spawn(bossesWorld, bossesLocations.get("spider").get("x"), bossesLocations.get("spider").get("y"), bossesLocations.get("spider").get("z"));}
            if(bosses.contains("creeper")){bossCreeper.spawn(bossesWorld, bossesLocations.get("creeper").get("x"), bossesLocations.get("creeper").get("y"), bossesLocations.get("creeper").get("z"));}
            if(bosses.contains("skeleton")){bossSkeleton.spawn(bossesWorld, bossesLocations.get("skeleton").get("x"), bossesLocations.get("skeleton").get("y"), bossesLocations.get("skeleton").get("z"));}
            if(bosses.contains("zombie")){bossZombie.spawn(bossesWorld, bossesLocations.get("zombie").get("x"), bossesLocations.get("zombie").get("y"), bossesLocations.get("zombie").get("z"));}
            if(bosses.contains("blaze")){bossBlaze.spawn(bossesWorld, bossesLocations.get("blaze").get("x"), bossesLocations.get("blaze").get("y"), bossesLocations.get("blaze").get("z"));}
        }
    }

    public void basicSetup(Creature animals, String name, int health) {
        animals.setHealth(health * 2.0);
        animals.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS " + ChatColor.BOLD + name);
        animals.setCustomNameVisible(true);
    }










}
