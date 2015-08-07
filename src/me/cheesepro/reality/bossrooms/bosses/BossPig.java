package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Pig;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossPig implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.RED.toString() + "Pig";
    String skill = "Spawns a zombie once every 15 seconds to help him fight";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 2500;
    Integer rewardKey = 1;
    Double rewardMoney = 2500.0;

    public BossPig(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "pig";
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
        Pig pig = loc.getWorld().spawn(loc, Pig.class);
        pig.setBreed(false);
        pig.setAgeLock(true);
        bossesAPI.basicSetup(pig, name, health);
    }

    public void spawn(Location loc){
        spawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

}
