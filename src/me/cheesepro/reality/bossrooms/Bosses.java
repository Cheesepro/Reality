package me.cheesepro.reality.bossrooms;


import org.bukkit.Location;

/**
 * Created by Mark on 2015-07-19.
 */
public interface Bosses {

    public String getType();

    public String getName();

    public String getSkills();

    public Integer getHealth();

    public Integer getDamage();

    public Integer getRewardXP();

    public Integer getRewardKey();

    public Double getRewardMoney();

    public void spawn(String w, double x, double y, double z, float pitch, float yaw);

    public void spawn(Location loc);

}
