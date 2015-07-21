package me.cheesepro.reality.bosses.types;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bosses.Bosses;
import me.cheesepro.reality.bosses.BossesSetup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Cow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossCow implements Bosses {

    Reality plugin;
    BossesSetup bossesSetup;
    String name = ChatColor.GRAY.toString() + "Cow";
    String skill = "Potion of regeneration level 1 for the first 60 secs";
    Integer health = 50;
    Integer damage = 4;
    Integer rewardXP = 1500;
    Integer rewardKey = 1;
    Integer rewardMoney = 1500;

    public BossCow(Reality plugin) {
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
        Location loc = new Location(Bukkit.getWorld(w), x, y ,z);
        Cow cow = loc.getWorld().spawn(loc, Cow.class);
        cow.setBreed(false);
        cow.setAgeLock(true);
        cow.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 70, 1));
        bossesSetup.basicSetup(cow, name, health);
    }
}