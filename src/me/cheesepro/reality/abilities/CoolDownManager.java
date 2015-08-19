package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-19.
 */
public class CoolDownManager {

    private static Map<String, Map<UUID, Integer>> cooldownTime = new HashMap<String, Map<UUID, Integer>>();
    private static Map<String, UUID> toBeRemoved = new HashMap<String, UUID>();
    private static Boolean isTaskRunning = false;

    private Reality plugin;

    public CoolDownManager(Reality plugin){
        this.plugin = plugin;
    }

    public void addCooldown(final String ability, Player player, int time){
        if(cooldownTime.containsKey(ability)){
            if(!cooldownTime.get(ability).containsKey(player.getUniqueId())){
                cooldownTime.get(ability).put(player.getUniqueId(), time);
            }
        }else{
            Map<UUID, Integer> map = new HashMap<UUID, Integer>();
            map.put(player.getUniqueId(), time);
            cooldownTime.put(ability, map);
        }
        if(!isTaskRunning){
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    System.out.print(cooldownTime);
                    isTaskRunning=true;
                    if(cooldownTime.isEmpty() || cooldownTime.toString().equalsIgnoreCase("{}"))
                    {
                        isTaskRunning=false;
                        cancel();
                    }
                    for(String ability : cooldownTime.keySet()) {
                        for(UUID id : cooldownTime.get(ability).keySet()){
                            if (cooldownTime.get(ability).get(id) == 1) {
                                toBeRemoved.put(ability, id);
                            }else if (cooldownTime.containsKey(ability) && cooldownTime.get(ability).containsKey(id)) {
                                cooldownTime.get(ability).put(id, cooldownTime.get(ability).get(id) - 1);
                            }
                        }
                    }
                    if(!toBeRemoved.isEmpty() || !toBeRemoved.toString().equalsIgnoreCase("{}")){
                        for(String ability : toBeRemoved.keySet()){
                            cooldownTime.get(ability).remove(toBeRemoved.get(ability));
                            if(cooldownTime.get(ability).isEmpty() || cooldownTime.get(ability).toString().equalsIgnoreCase("{}")){
                                cooldownTime.remove(ability);
                            }
                        }
                        toBeRemoved.clear();
                    }
                }
            }.runTaskTimerAsynchronously(plugin, 20, 20);
            //20 ticks = 1 sec;
        }
    }

    public boolean containsPlayer(String ability, Player p){
        if(cooldownTime.containsKey(ability)){
            return cooldownTime.get(ability).containsKey(p.getUniqueId());
        }
        return false;
    }

    public Integer getCooldown(String ability, Player p){
        if(cooldownTime.containsKey(ability)){
            return cooldownTime.get(ability).get(p.getUniqueId());
        }
        return null;
    }

}
