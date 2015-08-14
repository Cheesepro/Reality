package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
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
    private BRoomManager bRoomManager;
    private static Map<UUID, Integer> idleCount = new HashMap<UUID, Integer>();
    private static boolean isIdleTaskRunning;
    private Messenger msg;

    public BRoomIdle(Reality plugin){
        this.plugin = plugin;
        bRoomManager = new BRoomManager(plugin);
        msg = new Messenger(plugin);
    }

    public void addIdleCountDown(final UUID id){
        setIdle(Bukkit.getPlayer(id), bRoomManager.getBRoom(Bukkit.getPlayer(id)).getIdleTimeout());
        if(!isIdleTaskRunning){
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    isIdleTaskRunning=true;
                    System.out.print(idleCount);
                    if(idleCount.isEmpty() || idleCount.toString().equalsIgnoreCase("{}"))
                    {
                        isIdleTaskRunning=false;
                        cancel();
                    }
                    if(idleCount.containsKey(id)){
                        if(bRoomManager.getBRoom(Bukkit.getPlayer(id))!=null){
                            if(idleCount.get(id)==1){
                                bRoomManager.getBRoom(Bukkit.getPlayer(id)).removePlayer(Bukkit.getPlayer(id));
                                msg.send(Bukkit.getPlayer(id), "4", "You are kicked from the game for idling too long!");
                            }
                        }else{
                            idleCount.remove(id);
                        }
                    }
                    for(UUID uuid : idleCount.keySet()){
                        idleCount.put(uuid, idleCount.get(uuid)-1);
                        if(idleCount.get(uuid)<=10){
                            msg.send(Bukkit.getPlayer(id), "4", "You will be kicked from the room for idling too long in "+idleCount.get(uuid)+" second(s)!");
                        }
                    }
                }
            }.runTaskTimer(plugin, 20, 20);
            //20 ticks = 1 sec;
        }
    }

    public void setIdle(Player p, int value){
        idleCount.put(p.getUniqueId(), value);
    }

    public void removePlayer(Player p){
        idleCount.remove(p.getUniqueId());
    }

}
