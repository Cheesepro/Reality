package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Zombie;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossEnderman implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.LIGHT_PURPLE.toString() + "Enderman";
    String skill = "Once the boss is at 50% health, he will clone himself so that there are 2 bossrooms to kill with 25% of health each.";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 5000;
    Integer rewardKey = 2;
    Double rewardMoney = 5000.0;

    public BossEnderman(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "enderman";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSkills() {
        return skill;
    }

    @Override
    public Integer getHealth() {
        return health;
    }

    @Override
    public Integer getDamage() {
        return damage;
    }

    @Override
    public Integer getRewardXP() {
        return rewardXP;
    }

    @Override
    public Integer getRewardKey() {
        return rewardKey;
    }

    @Override
    public Double getRewardMoney() {
        return rewardMoney;
    }

    @Override
    public void spawn(String w, double x, double y, double z, float pitch, float yaw){
        Location loc = new Location(Bukkit.getWorld(w), x, y, z, pitch, yaw);
        Enderman enderman = loc.getWorld().spawn(loc, Enderman.class);
        bossesAPI.basicSetup(enderman, name, health);
    }

    public void spawn(Location loc){
        Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);
        bossesAPI.basicSetup(zombie, name, health);
    }
}