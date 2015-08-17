package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-05-24.
 */
public class AbilityCobweb implements Abilities, Listener{

    @Override
    public String getName() {
        return "COBWEB";
    }

    @Override
    public String getDesc() {
        return "Set the closest player in to cobweb";
    }

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, Map<String, List<String>>> ranks;
    Map<String, Map<String, String>> abilitiesOptions;
    Map<String, String> messages;
    private HashMap<Player, Integer> cooldownTime;
    private HashMap<Player, BukkitRunnable> cooldownTask;
    Messenger msg;
    Tools tools;

    public AbilityCobweb(Reality plugin){
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

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null){
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("COBWEB")) {
                if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get("COBWEB").get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get("COBWEB").get("item"))) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (cooldownTime.containsKey(p)) {
                            msg.send(p, "c", "You must wait for " + ChatColor.GREEN + cooldownTime.get(p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + "COBWEB" + ChatColor.RED + " again!");
                            return;
                        }

                        Boolean anyone = false;
                        for (Entity e : p.getNearbyEntities(10, 10, 10)) {
                            if (e instanceof Player) {
                                Player closestP = (Player) e;
                                Location loc = closestP.getLocation();
                                loc.setY(loc.getY()+1);
                                Block block = closestP.getWorld().getBlockAt(loc);
                                if(block.getType()==Material.AIR){
                                    block.setType(Material.WEB);
                                    anyone = true;
                                    msg.send(closestP, "6", "Some one got you with their ability!");
                                }
                            }
                        }

                        if(anyone){
                            msg.send(p, "2", "Got someone!");
                        }else{
                            msg.send(p, "5", "Cobweb cannot be placed with in 10 blocks of radius.");
                        }

                        cooldownTime.put(p, Integer.parseInt(abilitiesOptions.get("COBWEB").get("cooldown")));
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

    @EventHandler
    public void noPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null) {
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("COBWEB")) {
                if(tools.isHoldingCorrectItem(p, abilitiesOptions.get("COBWEB").get("item"))){
                    e.setCancelled(true);
                }
            }
        }
    }
}
