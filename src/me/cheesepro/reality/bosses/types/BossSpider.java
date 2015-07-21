package me.cheesepro.reality.bosses.types;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.Bosses;
import me.cheesepro.reality.bosses.BossesSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Spider;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossSpider implements Bosses {

    Reality plugin;
    BossesSetup bossesSetup;
    String name = ChatColor.DARK_GRAY.toString() + "Spider";
    String skill = "Can freeze the player once in a while";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 7000;
    Integer rewardKey = 3;
    Integer rewardMoney = 7000;

    public BossSpider(Reality plugin){
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
        Spider spider = loc.getWorld().spawn(loc, Spider.class);
        bossesSetup.basicSetup(spider, name, health);
    }
}