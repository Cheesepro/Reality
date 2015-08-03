package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.bosses.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mark on 2015-08-01.
 */
public class BossesSetup {

    Reality plugin = Reality.getPlugin();
    BossChicken bossChicken = new BossChicken(plugin);
    BossBlaze bossBlaze = new BossBlaze(plugin);
    BossCow bossCow = new BossCow(plugin);
    BossCreeper bossCreeper = new BossCreeper(plugin);
    BossEnderman bossEnderman = new BossEnderman(plugin);
    BossPig bossPig = new BossPig(plugin);
    BossSkeleton bossSkeleton = new BossSkeleton(plugin);
    BossSpider bossSpider = new BossSpider(plugin);
    BossZombie bossZombie = new BossZombie(plugin);
    Map<String, Map<String, Double>> bossesLocations =  plugin.getbRoomsBossesLocations();
    String bossesWorld = plugin.getBossesWorld();


    public BossesSetup(){}

    public void initialSetup(){
        if(bossesWorld!=null && bossesLocations!=null){
            Set<String> bosses = new HashSet<String>(bossesLocations.keySet());
            if(bosses.contains("chicken")){bossChicken.spawn(bossesWorld, bossesLocations.get("chicken").get("x"), bossesLocations.get("chicken").get("y"), bossesLocations.get("chicken").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("chicken").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("chicken").get("yaw"))));}
            if(bosses.contains("cow")){bossCow.spawn(bossesWorld, bossesLocations.get("cow").get("x"), bossesLocations.get("cow").get("y"), bossesLocations.get("cow").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("cow").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("cow").get("yaw"))));}
            if(bosses.contains("pig")){bossPig.spawn(bossesWorld,bossesLocations.get("pig").get("x"), bossesLocations.get("pig").get("y"), bossesLocations.get("pig").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("pig").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("pig").get("yaw"))));}
            if(bosses.contains("enderman")){bossEnderman.spawn(bossesWorld, bossesLocations.get("enderman").get("x"), bossesLocations.get("enderman").get("y"), bossesLocations.get("enderman").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("enderman").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("enderman").get("yaw"))));}
            if(bosses.contains("spider")){bossSpider.spawn(bossesWorld, bossesLocations.get("spider").get("x"), bossesLocations.get("spider").get("y"), bossesLocations.get("spider").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("spider").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("spider").get("yaw"))));}
            if(bosses.contains("creeper")){bossCreeper.spawn(bossesWorld, bossesLocations.get("creeper").get("x"), bossesLocations.get("creeper").get("y"), bossesLocations.get("creeper").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("creeper").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("creeper").get("yaw"))));}
            if(bosses.contains("skeleton")){bossSkeleton.spawn(bossesWorld, bossesLocations.get("skeleton").get("x"), bossesLocations.get("skeleton").get("y"), bossesLocations.get("skeleton").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("skeleton").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("skeleton").get("yaw"))));}
            if(bosses.contains("zombie")){bossZombie.spawn(bossesWorld, bossesLocations.get("zombie").get("x"), bossesLocations.get("zombie").get("y"), bossesLocations.get("zombie").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("zombie").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("zombie").get("yaw"))));}
            if(bosses.contains("blaze")){bossBlaze.spawn(bossesWorld, bossesLocations.get("blaze").get("x"), bossesLocations.get("blaze").get("y"), bossesLocations.get("blaze").get("z"), Float.parseFloat(String.valueOf(bossesLocations.get("blaze").get("pitch"))), Float.parseFloat(String.valueOf(bossesLocations.get("blaze").get("yaw"))));}
        }
    }

}
