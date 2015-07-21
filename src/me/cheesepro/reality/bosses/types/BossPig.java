package me.cheesepro.reality.bosses.types;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.Bosses;
import me.cheesepro.reality.bosses.BossesSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Pig;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossPig implements Bosses {

    Reality plugin;
    BossesSetup bossesSetup;
    String name = ChatColor.RED.toString() + "Pig";
    String skill = "Spawns a zombie once every 15 seconds to help him fight";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 2500;
    Integer rewardKey = 1;
    Integer rewardMoney = 2500;

    public BossPig(Reality plugin){
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
        Pig pig = loc.getWorld().spawn(loc, Pig.class);
        pig.setBreed(false);
        pig.setAgeLock(true);
        bossesSetup.basicSetup(pig, name, health);
    }
}
