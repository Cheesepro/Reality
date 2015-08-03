package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Zombie;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossChicken implements Bosses{

    Reality plugin = Reality.getPlugin();
    BossesAPI bossesAPI = new BossesAPI(plugin);
    String name = ChatColor.WHITE.toString() + "Chicken";
    String skill = "None!";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 1000;
    Integer rewardKey = 1;
    Double rewardMoney = 1000.0;

    public BossChicken(Reality plugin){
        this.plugin = plugin;
    }

    @Override
    public String getType(){
        return "chicken";
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
        Chicken chicken = loc.getWorld().spawn(loc, Chicken.class);
        chicken.setBreed(false);
        chicken.setAgeLock(true);
        bossesAPI.basicSetup(chicken, name, health);
    }

    public void spawn(Location loc){
        Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);
        bossesAPI.basicSetup(zombie, name, health);
    }
}
