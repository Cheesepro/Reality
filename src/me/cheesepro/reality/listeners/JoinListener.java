package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.RankGiver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-05.
 */
public class JoinListener implements Listener{

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private Map<String, List<String>> settings;
    private Config storageConfig;
    private RankGiver rankGiver;
    private DataManager dataManager;
    private BRoomManager bRoomManager;
    private Messenger msg;

    public JoinListener(Reality plugin){
        this.plugin = plugin;
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        settings = plugin.getSettings();
        rankGiver = new RankGiver(plugin);
        dataManager = new DataManager(plugin);
        bRoomManager = new BRoomManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void NewPlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
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
        }else{
            if(dataManager.containsQuitBRoom(p)){
                p.teleport(bRoomManager.getBRoom(dataManager.getQuitBRoom(p)).getEnd());
                dataManager.removeQuitBRoom(p);
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        msg.important(p, "We noticed that your left our server while in a boss room, therefore all the rewards that you earned from the boss room (if any) will be ignored and deleted. Thank you for your cooperation!");
                    }
                }.runTaskLater(plugin, 20);
            }
        }
    }

}
