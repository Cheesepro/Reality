package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Skeleton;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossSkeleton implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.GRAY.toString() + "Skeleton";
    String skill = "Can shoot flame arrows that ignites on impact";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 9000;
    Integer rewardKey = 5;
    Double rewardMoney = 9000.0;

    public BossSkeleton(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "skeleton";
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
        Skeleton skeleton = loc.getWorld().spawn(loc, Skeleton.class);
        bossesAPI.basicSetup(skeleton, name, health);
    }

    public void spawn(Location loc){
        spawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }
}