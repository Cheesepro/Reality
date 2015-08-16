package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-11.
 */
public class AbilityTeleport implements Abilities, Listener{

    @Override
    public String getName() {
        return "TELEPORT";
    }

    @Override
    public String getDesc() {
        return "Teleports you to where you looking at";
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

    public AbilityTeleport(Reality plugin){
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
            if (listCache.contains("TELEPORT")) {
                if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get("TELEPORT").get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get("TELEPORT").get("item"))) {
                    if (event.getAction()== Action.RIGHT_CLICK_AIR || event.getAction()== Action.RIGHT_CLICK_BLOCK) {
                        if (cooldownTime.containsKey(p)) {
                            msg.send(p, "c", "You must wait for " + ChatColor.GREEN + cooldownTime.get(p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + "TELEPORT" + ChatColor.RED + " again!");
                            return;
                        }
                        Location pLocationoc = p.getLocation();
                        double pitch = ((pLocationoc.getPitch() + 90) * Math.PI) / 180;
                        double yaw = ((pLocationoc.getYaw() + 90) * Math.PI) / 180;
                        Double x = Math.sin(pitch) * Math.cos(yaw);
                        Double y = Math.sin(pitch) * Math.sin(yaw);
                        Double z = Math.cos(pitch);
                        Vector vector = new Vector(x, z, y);
                        p.setVelocity(vector.multiply(3.5));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4 + 10, 8));
                        cooldownTime.put(p, Integer.parseInt(abilitiesOptions.get("TELEPORT").get("cooldown")));
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
