package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-06-13.
 */
public class AbilityLightning implements Abilities, Listener {

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, Map<String, List<String>>> ranks;
    Map<String, Map<String, String>> abilitiesOptions;
    Map<String, String> messages;
    private HashMap<Player, Integer> cooldownTime;
    private HashMap<Player, BukkitRunnable> cooldownTask;
    Messenger msg;
    Tools tools;

    public AbilityLightning(Reality plugin){
        this.plugin = plugin;
        ranks = plugin.getRanks();
        abilitiesOptions = plugin.getAbilitiesOptions();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        cooldownTime = new HashMap<Player, Integer>();
        cooldownTask = new HashMap<Player, BukkitRunnable>();
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
    }

    @Override
    public String getDesc() {
        return "Right click the assigned item to strike a lightning bolt";
    }

    @Override
    public String getName() {
        return "LIGHTNING";
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null){
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("LIGHTNING")) {
                if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get("LIGHTNING").get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get("LIGHTNING").get("item"))) {
                    if (event.getAction()== Action.RIGHT_CLICK_AIR || event.getAction()== Action.RIGHT_CLICK_BLOCK) {
                        if (cooldownTime.containsKey(p)) {
                            msg.send(p, "c", "You must wait for " + ChatColor.GREEN + cooldownTime.get(p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + "Lightning" + ChatColor.RED + " again!");
                            return;
                        }
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 2 + 10, 8));
                        World world = p.getWorld();
                        Set<Material> nullSet = null;
                        Block point = p.getTargetBlock(nullSet, 50);
                        Location loc = point.getLocation();
                        world.strikeLightning(loc);

                        cooldownTime.put(p, Integer.parseInt(abilitiesOptions.get("LIGHTNING").get("cooldown")));
                        cooldownTask.put(p, new BukkitRunnable() {
                            public void run() {
                                cooldownTime.put(p, cooldownTime.get(p) - 1);
                                if (cooldownTime.get(p) == 0) {
                                    cooldownTime.remove(p);
                                    cooldownTask.remove(p);
                                    cancel();
                                }
                            }
                        });
                        cooldownTask.get(p).runTaskTimer(plugin, 20, 20);
                    }
                }
            }
        }
    }

}
