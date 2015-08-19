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
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-19.
 */
public class AbilityHyperJump implements Abilities, Listener {

    @Override
    public String getName() {
        return "HYPERJUMP";
    }

    @Override
    public String getDesc() {
        return "Hyper jump you to where you looking at";
    }

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private Messenger msg;
    private Tools tools;
    private CoolDownManager coolDownManager;

    public AbilityHyperJump(Reality plugin) {
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
            if (p.getItemInHand().getType() == Material.getMaterial(abilitiesOptions.get("HYPERJUMP").get("item")) && tools.isHoldingCorrectItem(p, abilitiesOptions.get("HYPERJUMP").get("item"))) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (coolDownManager.containsPlayer(getName(), p)) {
                        msg.send(p, "c", "You must wait for " + ChatColor.GREEN + coolDownManager.getCooldown(getName(), p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + "HYPERJUMP" + ChatColor.RED + " again!");
                        return;
                    }

                    Location pLocationoc = p.getLocation();
                    double pitch = ((pLocationoc.getPitch() + 90) * Math.PI) / 180;
                    double yaw = ((pLocationoc.getYaw() + 90) * Math.PI) / 180;
                    Double x = Math.sin(pitch) * Math.cos(yaw);
                    Double y = Math.sin(pitch) * Math.sin(yaw);
                    Double z = Math.cos(pitch);
                    Vector vector = new Vector(x, z, y);
                    p.setVelocity(vector.multiply(5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4 + 10, 8));

                    coolDownManager.addCooldown(getName(), p, Integer.parseInt(abilitiesOptions.get(getName()).get("cooldown")));
                }
            }
        }
    }
}
