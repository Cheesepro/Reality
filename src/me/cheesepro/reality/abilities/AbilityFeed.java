package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-06-13.
 */
public class AbilityFeed  implements Abilities, Listener {

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private Messenger msg;
    private Tools tools;
    private CoolDownManager coolDownManager;

    public AbilityFeed(Reality plugin){
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
    public String getDesc() {
        return "Right click the assigned item to feed yourself";
    }

    @Override
    public String getName() {
        return "FEED";
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

                    p.setFoodLevel(20);

                    coolDownManager.addCooldown(getName(), p, Integer.parseInt(abilitiesOptions.get(getName()).get("cooldown")));
                }
            }
        }
    }
}
