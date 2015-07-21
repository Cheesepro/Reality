package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-06.
 */
public class AbilityDoubleJump implements Abilities, Listener{

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, Map<String, List<String>>> ranks;
    Map<String, Map<String, String>> abilitiesOptions;
    Map<String, String> messages;
    private HashMap<Player, Integer> cooldownTime;
    private HashMap<Player, BukkitRunnable> cooldownTask;
    Messenger msg;

    public AbilityDoubleJump(Reality plugin){
        this.plugin = plugin;
        ranks = plugin.getRanks();
        abilitiesOptions = plugin.getAbilitiesOptions();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        cooldownTime = new HashMap<Player, Integer>();
        cooldownTask = new HashMap<Player, BukkitRunnable>();
        msg = new Messenger(plugin);
    }

    @Override
    public String getName() {
        return "DOUBLEJUMP";
    }

    @Override
    public String getDesc() {
        return "Click jump key twice to jump higher";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent e)
    {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Player p = e.getPlayer();
                Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
                if(mapCache.get("abilities")!=null) {
                    List<String> listCache = mapCache.get("abilities");
                    if (listCache.contains("DOUBLEJUMP")) {
                        p.setAllowFlight(true);
                        p.setFlying(false);
                    }
                }
            }
        }, 10);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Player p = e.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null) {
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("DOUBLEJUMP")) {
                p.setAllowFlight(true);
                p.setFlying(false);
            }else{
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        }else{
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        final Player p = e.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null) {
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("DOUBLEJUMP")) {
                if (p.getGameMode() != GameMode.CREATIVE) {
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    if (cooldownTime.containsKey(p)) {
                        msg.send(p, "c", "You must wait for " + ChatColor.GREEN + cooldownTime.get(p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + "DoubleJump" + ChatColor.RED + " again!");
                        return;
                    }
                    p.setVelocity(p.getLocation().getDirection().multiply(1.20D).setY(0.8D));

                    cooldownTime.put(p, Integer.parseInt(abilitiesOptions.get("DOUBLEJUMP").get("cooldown")));
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null) {
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("DOUBLEJUMP")) {
                if ((e.getPlayer().getGameMode() != GameMode.CREATIVE) &&
                        (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
                    p.setAllowFlight(true);
                }
            }else{
                p.setAllowFlight(false);
                p.setFlying(false);
            }
        }else{
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }
}
