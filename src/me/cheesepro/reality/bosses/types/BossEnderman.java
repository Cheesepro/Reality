package me.cheesepro.reality.bosses.types;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.Bosses;
import me.cheesepro.reality.bosses.BossesSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossEnderman implements Bosses {

    Reality plugin;
    BossesSetup bossesSetup;
    String name = ChatColor.LIGHT_PURPLE.toString() + "Enderman";
    String skill = "Once the boss is at 50% health, he will clone himself so that there are 2 bosses to kill with 25% of health each.";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 5000;
    Integer rewardKey = 2;
    Integer rewardMoney = 5000;

    public BossEnderman(Reality plugin){
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
        Enderman enderman = loc.getWorld().spawn(loc, Enderman.class);
        bossesSetup.basicSetup(enderman, name, health);
    }
}