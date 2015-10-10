package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-06.
 */
public class AbilityDoubleJump implements Abilities, Listener{

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private Messenger msg;
    private Tools tools;
    private CoolDownManager coolDownManager;

    public AbilityDoubleJump(Reality plugin){
        this.plugin = plugin;
        abilitiesOptions = plugin.getAbilitiesOptions();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        coolDownManager = new CoolDownManager(plugin);
    }

    @Override
    public String getName() {
        return "DOUBLEJUMP";
    }

    @Override
    public String getDesc() {
        return "Click jump key twice to jump higher";
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
            if (tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName())) {
                p.setAllowFlight(true);
                p.setFlying(false);
            }
        }
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        final Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
            if (tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName())) {
                e.setCancelled(true);
                p.setAllowFlight(false);
                p.setFlying(false);
                if (coolDownManager.containsPlayer(getName(), p)) {
                    msg.send(p, "c", "You must wait for " + ChatColor.GREEN + coolDownManager.getCooldown(getName(), p) + ChatColor.RED + " seconds, before using " + ChatColor.LIGHT_PURPLE + getName() + ChatColor.RED + " again!");
                    return;
                }
                p.setVelocity(p.getLocation().getDirection().multiply(1.20D).setY(0.8D));
                coolDownManager.addCooldown(getName(), p, Integer.parseInt(abilitiesOptions.get(getName()).get("cooldown")));
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
            if (tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName())) {
                if ((e.getPlayer().getGameMode() != GameMode.CREATIVE) &&
                        (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
                    p.setAllowFlight(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player p = (Player) event.getEntity();
                if (tools.canUseAbility(playersINFO.get(p.getUniqueId()).get("rank"), getName()) && coolDownManager.containsPlayer(getName(), p)) {
                    if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
//}
