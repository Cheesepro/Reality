package me.cheesepro.reality.bosses.types;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.Bosses;
import me.cheesepro.reality.bosses.BossesSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossCreeper implements Bosses {

    Reality plugin;
    BossesSetup bossesSetup;
    String name = ChatColor.GREEN.toString() + "Creeper";
    String skill = "Throw tnt at the player";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 9000;
    Integer rewardKey = 5;
    Integer rewardMoney = 9000;

    public BossCreeper(Reality plugin){
        this.plugin = plugin;
        bossesSetup = new BossesSetup(plugin);
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
    public Integer getRewardMoney() {
        return rewardMoney;
    }

    @Override
    public void spawn(String w, double x, double y, double z){
        Location loc = new Location(Bukkit.getWorld(w), x, y, z);
        Creeper creeper = loc.getWorld().spawn(loc, Creeper.class);
        bossesSetup.basicSetup(creeper, name, health);
    }
}