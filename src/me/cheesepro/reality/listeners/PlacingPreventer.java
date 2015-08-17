package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-06-28.
 */
public class PlacingPreventer implements Listener{

    private Reality plugin;
    private Map<String, Map<String, List<String>>> ranks;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Tools tools;
    private Map<UUID, Map<String, String>> playersINFO;
    private DataManager dataManager;
    private Messenger msg;

    public PlacingPreventer(Reality plugin){
        this.plugin = plugin;
        ranks = plugin.getRanks();
        abilitiesOptions = plugin.getAbilitiesOptions();
        playersINFO = plugin.getPlayersINFO();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void noPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Map<String, List<String>> mapCache = ranks.get(playersINFO.get(p.getUniqueId()).get("rank"));
        if(mapCache.get("abilities")!=null) {
            List<String> listCache = mapCache.get("abilities");
            if (listCache.contains("FIREBALL")) {
                if(tools.isHoldingCorrectItem(p, abilitiesOptions.get("FIREBALL").get("item"))){
                    e.setCancelled(true);
                }
            }
            if (listCache.contains("CLIMB")) {
                if(tools.isHoldingCorrectItem(p, abilitiesOptions.get("CLIMB").get("item"))){
                    e.setCancelled(true);
                }
            }
            if (listCache.contains("WITHERSKULL")) {
                if(tools.isHoldingCorrectItem(p, abilitiesOptions.get("WITHERSKULL").get("item"))){
                    e.setCancelled(true);
                }
            }
            if (listCache.contains("COBWEB")) {
                if(tools.isHoldingCorrectItem(p, abilitiesOptions.get("COBWEB").get("item"))){
                    e.setCancelled(true);
                }
            }
        }
        if(p.getItemInHand().getItemMeta().toString().equalsIgnoreCase(dataManager.getCrateKey().getItemMeta().toString())){
            e.setCancelled(true);
            msg.send(p, ChatColor.GOLD, "Trust me, you don't want to place your precious lucky crate key(s) down, and never get it back...");
        }
    }

}
