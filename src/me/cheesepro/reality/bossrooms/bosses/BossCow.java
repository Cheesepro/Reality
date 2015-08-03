package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossCow implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.GRAY.toString() + "Cow";
    String skill = "Potion of regeneration level 1 for the first 60 secs";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 1500;
    Integer rewardKey = 1;
    Double rewardMoney = 1500.0;

    public BossCow(Reality plugin) {
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "cow";
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
        Cow cow = loc.getWorld().spawn(loc, Cow.class);
        cow.setBreed(false);
        cow.setAgeLock(true);
        cow.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 70, 1));
        bossesAPI.basicSetup(cow, name, health);
    }

    public void spawn(Location loc){
        Zombie zombie = loc.getWorld().spawn(loc, Zombie.class);
        bossesAPI.basicSetup(zombie, name, health);
    }
}