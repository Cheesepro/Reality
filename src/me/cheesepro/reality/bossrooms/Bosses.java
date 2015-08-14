package me.cheesepro.reality.bossrooms;


import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;

/**
 * Created by Mark on 2015-07-19.
 */
public interface Bosses {

    String getType();

    String getName();

    String getSkills();

    Integer getHealth();

    Integer getDamage();

    Integer getRewardXP();

    Integer getRewardKey();

    Double getRewardMoney();

    void spawn(String w, double x, double y, double z, float pitch, float yaw);

    void spawn(Location loc);

    NPC getNPC();

}
