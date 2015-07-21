package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.RankGiver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;

/**
 * Created by Mark on 2015-04-05.
 */
public class JoinListener implements Listener{

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, List<String>> settings;
    Config storageConfig;
    RankGiver rankGiver;

    public JoinListener(Reality plugin){
        this.plugin = plugin;
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        settings = plugin.getSettings();
        rankGiver = new RankGiver(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void NewPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.hasPlayedBefore()){
            String uuid = p.getUniqueId().toString();
            String rank = plugin.getConfig().getString("Default_rank");
            storageConfig.set("players." + p.getUniqueId() + ".rank", rank);
            storageConfig.set("players." + p.getUniqueId() + ".level", 1);
            storageConfig.set("players." + p.getUniqueId() + ".xp", 0);
            storageConfig.saveConfig();
            Map<String, String> cache = new HashMap<String, String>();
            if(storageConfig.getString("players."+uuid+".rank")!=null){
                cache.put("rank", storageConfig.getString("players."+uuid+".rank"));
            }
            if(storageConfig.getString("players."+uuid+".level")!=null){
                cache.put("level", storageConfig.getString("players."+uuid+".level"));
            }
            if(storageConfig.getString("players."+uuid+".xp")!=null){
                cache.put("xp", storageConfig.getString("players."+uuid+".xp"));
            }
            playersINFO.put(UUID.fromString(uuid), cache);
            cache.put("rank", rank);
            cache.put("level", "1");
            cache.put("xp", "0");
            playersINFO.put(p.getUniqueId(), cache);
            PermissionUser user = PermissionsEx.getUser(p);
            for(String rankCache : settings.get("allowed_ranks")){
                user.addPermission("reality.rank."+rankCache);
            }
            rankGiver.giveRank(p);
        }
    }

}
