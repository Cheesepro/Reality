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
import org.bukkit.inventory.ItemStack;


/**
 * Created by Mark on 2015-07-20.
 */
public class BossSkeleton implements Bosses {

    Reality plugin;
    BossesAPI bossesAPI;
    String name = ChatColor.GRAY.toString() + "Skeleton";
    String skill = "Can shoot flame arrows that ignites on impact";
    Integer health = 400;
    Integer damage = 6;
    Integer rewardXP = 9000;
    Integer rewardKey = 5;
    Double rewardMoney = 9000.0;
    NPC npc;

    public BossSkeleton(Reality plugin){
        this.plugin = plugin;
        bossesAPI = new BossesAPI(plugin);
    }

    @Override
    public String getType(){
        return "skeleton";
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
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SKELETON, name);
        return npc;
    }

    @Override
    public void spawn(String w, double x, double y, double z, float pitch, float yaw){
        Location loc = new Location(Bukkit.getWorld(w), x, y, z, pitch, yaw);
        npc.spawn(loc);
        npc.setProtected(false);
        Creature creature = (Creature) npc.getEntity();
        creature.setFireTicks(0);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack hat = new ItemStack(Material.IRON_HELMET);
        ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack legging = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack sword = new ItemStack(Material.IRON_AXE);
        creature.getEquipment().setHelmet(hat);
        creature.getEquipment().setItemInHand(sword);
        bossesAPI.basicSetup(creature, health);
    }

    public void spawn(Location loc){
        spawn(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }
}