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
import org.bukkit.event.player.PlayerInteractEvent;

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

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private Messenger msg;
    private Tools tools;
    private CoolDownManager coolDownManager;

    public AbilityCobweb(Reality plugin){
        this.plugin = plugin;
        abilitiesOptions = plugin.getAbilitiesOptions();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        coolDownManager = new CoolDownManager(plugin);
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if (tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName())) {
            if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get(getName()).get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get(getName()).get("item"))) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (coolDownManager.containsPlayer(getName(), p)) {
                        msg.send(p, "c", "You must wait for " + ChatColor.GREEN + coolDownManager.getCooldown(getName(), p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + getName() + ChatColor.RED + " again!");
                        return;
                    }

                    Boolean anyone = false;
                    for (Entity e : p.getNearbyEntities(10, 10, 10)) {
                        if (e instanceof Player) {
                            Player closestP = (Player) e;
                            Location loc = closestP.getLocation();
                            loc.setY(loc.getY() + 1);
                            Block block = closestP.getWorld().getBlockAt(loc);
                            if (block.getType() == Material.AIR) {
                                block.setType(Material.WEB);
                                anyone = true;
                                msg.send(closestP, "6", "Some one got you with their ability!");
                            }
                        }
                    }

                    if (anyone) {
                        msg.send(p, "2", "Got someone!");
                    } else {
                        msg.send(p, "5", "Cobweb cannot be placed with in 10 blocks of radius.");
                    }

                    coolDownManager.addCooldown(getName(), p, Integer.parseInt(abilitiesOptions.get(getName()).get("cooldown")));
                }
            }
        }
    }
}
