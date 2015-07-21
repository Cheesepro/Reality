package me.cheesepro.reality.level;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.eventhandlers.PlayerKillMobEvent;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mark on 2015-05-01.
 */
public class XPGainListener implements Listener{

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Config storageConfig;
    PlayerManager pManager;
    Messenger msg;

    public XPGainListener(Reality plugin){
        this.plugin = plugin;
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        pManager = new PlayerManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onKillMob(EntityDeathEvent event)
    {
        Entity e = event.getEntity();
        if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getLastDamageCause();
            if(nEvent.getDamager() instanceof Player)
            {
                Player p = (Player)nEvent.getDamager();
                UUID id = p.getUniqueId();
                if(!(e instanceof Player)){
                    if (e instanceof Bat){
                        pManager.addXP(id, 2);
                        levelUP(id, p);
                    }else if (e instanceof Chicken){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Cow){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Pig){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Rabbit){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Sheep){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Squid){
                        pManager.addXP(id, 3);
                        levelUP(id, p);
                    }else if (e instanceof Villager){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof CaveSpider){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof Enderman){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof Spider){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof PigZombie){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof Blaze){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof Creeper){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof Guardian){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof Endermite){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof Ghast){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof MagmaCube){
                        pManager.addXP(id, 8);
                        levelUP(id, p);
                    }else if (e instanceof Silverfish){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Skeleton){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof Slime){
                        pManager.addXP(id, 4);
                        levelUP(id, p);
                    }else if (e instanceof Witch){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }else if (e instanceof Zombie){
                        pManager.addXP(id, 16);
                        levelUP(id, p);
                    }
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerKillMobEvent(p, e));
                }else{
                    Player dPlayer = (Player) e;
                    pManager.addXP(id, Integer.parseInt(String.valueOf(Math.round(Integer.parseInt(playersINFO.get(dPlayer.getUniqueId()).get("xp"))/0.25))));
                }

            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        Random rand = new Random();
        int rNum = rand.nextInt((3 - 1) + 1) + 1;
        if(e.getBlock().getType()== Material.COAL_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.COAL, rNum));
            pManager.addXP(id, 1);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.LAPIS_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.INK_SACK, 1, (short) 4));
            pManager.addXP(id, 3);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.IRON_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
            pManager.addXP(id, 3);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.REDSTONE_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.REDSTONE, rNum));
            pManager.addXP(id, 5);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.GOLD_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
            pManager.addXP(id, 8);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.DIAMOND_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.DIAMOND, rNum));
            pManager.addXP(id, 10);
            levelUP(id, p);
        }else if(e.getBlock().getType()== Material.EMERALD_ORE){
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.EMERALD, rNum));
            pManager.addXP(id, 15);
            levelUP(id, p);
        }
    }

    private void levelUP(UUID id, Player p){
        if(pManager.levelUp(id)!=null){
            String returnResult = pManager.levelUp(id);
            if(returnResult.startsWith("no")){
                String[] splits = returnResult.split("#");
                String next = splits[1];
                msg.send(p, "a", splits[0].replace("no", "")+" more XP to level " + next);
            }
        }
    }

}
