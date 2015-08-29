package me.cheesepro.reality.bossrooms.bosses;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossZombie implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.DARK_GREEN.toString() + "Zombie";
    String skill = "Spawns 5 zombies upon spawn";
    Integer health = 400;
    Integer damage = 6;
    Integer rewardXP = 9000;
    Integer rewardKey = 5;
    Double rewardMoney = 9000.0;
    NPC npc;

    public BossZombie(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "zombie";
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
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, name);
        return npc;
    }

    @Override
    public void spawn(String w, double x, double y, double z, float pitch, float yaw){
        Location loc = new Location(Bukkit.getWorld(w), x, y, z, pitch, yaw);
        npc.spawn(loc);
        npc.setProtected(false);
        Creature creature = (Creature) npc.getEntity();
        ItemStack hat = new ItemStack(Material.DIAMOND_HELMET);
        creature.getEquipment().setHelmet(hat);
        bossesAPI.basicSetup(creature, health);
        useAbility(loc);
    }

    public void spawn(Location loc){
        spawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public void useAbility(Location loc){
        for(int i = 0; i<5; i++){
            Zombie mob = loc.getWorld().spawn(loc, Zombie.class);
            ItemStack hat = new ItemStack(Material.GOLD_HELMET);
            mob.getEquipment().setHelmet(hat);
            mob.setCustomName(ChatColor.RED + "!!!");
            mob.setCustomNameVisible(true);
        }
    }

}