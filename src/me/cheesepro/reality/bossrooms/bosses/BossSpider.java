package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.utils.EffectsAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossSpider implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.DARK_GRAY.toString() + "Spider";
    String skill = "Give target 2 seconds of slowness after hit.";
    Integer health = 250;
    Integer damage = 6;
    Integer rewardXP = 7000;
    Integer rewardKey = 3;
    Double rewardMoney = 7000.0;
    NPC npc;

    public BossSpider(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "spider";
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
    public NPC getNPC() {
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SPIDER, name);
        return npc;
    }

    @Override
    public void spawn(String w, double x, double y, double z, float pitch, float yaw){
        Location loc = new Location(Bukkit.getWorld(w), x, y, z, pitch, yaw);
        npc.spawn(loc);
        npc.setProtected(false);
        Creature creature = (Creature) npc.getEntity();
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20*1000, 3);
        creature.addPotionEffect(speed);
        bossesAPI.basicSetup(creature, health);
    }

    public void spawn(Location loc){
        spawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public void useAbility(Player p){
        PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, 2*20, 5);
        p.addPotionEffect(slowEffect);
        EffectsAPI.effect(p.getLocation(), EffectsAPI.PlayEffect.CLOUD);
    }
}