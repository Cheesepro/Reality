package me.cheesepro.reality.bosses;


/**
 * Created by Mark on 2015-07-19.
 */
public interface Bosses {

    public String getName();

    public String getSkills();

    public Integer getHealth();

    public Integer getDamage();

    public Integer getRewardXP();

    public Integer getRewardKey();

    public Integer getRewardMoney();

    public void spawn(String w, double x, double y, double z);

}
