package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-10.
 */
public class BRoomIdle {
    private Reality plugin;
    private DataManager dataManager;
    private BRoomManager bRoomManager;
    private static Map<UUID, Integer> idleCount = new HashMap<UUID, Integer>();
    private static boolean isIdleTaskRunning;
    private Messenger msg;

    public BRoomIdle(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        bRoomManager = new BRoomManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    public void addIdleCountDown(final UUID id){
        idleCount.put(id, 120);
        if(!isIdleTaskRunning){
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    isIdleTaskRunning=true;
                    if(idleCount.containsKey(id) && idleCount.get(id)==0){
                        bRoomManager.getBRoom(Bukkit.getPlayer(id)).removePlayer(Bukkit.getPlayer(id));
                        msg.send(Bukkit.getPlayer(id), "4", "You are kicked from the game for idling too long!");
                        idleCount.remove(id);
                    }
                    if(idleCount.isEmpty() || idleCount.toString().equalsIgnoreCase("{}"))
                    {
                        isIdleTaskRunning=false;
                        cancel();
                    }
                    for(UUID uuid : idleCount.keySet()){
                        idleCount.put(uuid, idleCount.get(uuid)-1);
                        if(idleCount.get(uuid)<=10){
                            msg.send(Bukkit.getPlayer(id), "4", "You will be kicked from the room for idling too long in"+idleCount.get(uuid)+" !");
                        }
                    }
                }
            }.runTaskTimer(plugin, 20, 20);
            //20 ticks = 1 sec;
        }
    }

    private void setIdle(Player p, int value){
        idleCount.put(p.getUniqueId(), value);
    }

    public void removePlayer(Player p){
        idleCount.remove(p.getUniqueId());
    }

}
