package me.cheesepro.reality.bossrooms;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.EffectsAPI;
import me.cheesepro.reality.utils.Messenger;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-08-12.
 */
public class BossesPathFinding {

    private Reality plugin;
    private static List<NPC> bossNPCs = new ArrayList<NPC>();
    private static boolean isPathFindingTaskRunning;
    private Messenger msg;
    private WorldGuardPlugin worldGuard;
    private DataManager dataManager;
    private Map<UUID, Integer> bossXLoc = new HashMap<UUID, Integer>();
    private Map<UUID, Integer> bossZLoc = new HashMap<UUID, Integer>();
    private Map<UUID, Double> bossStillCount = new HashMap<UUID, Double>();
    private EffectsAPI effectsAPI;

    public BossesPathFinding(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        dataManager = new DataManager(plugin);
        worldGuard = plugin.getWorldGuard();
        effectsAPI = new EffectsAPI(plugin);
    }

    public void startPathFinding(final NPC npc, final String bRoomName){
        bossNPCs.add(npc);
        if(!isPathFindingTaskRunning){
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    isPathFindingTaskRunning=true;
                    if(bossNPCs.isEmpty() || bossNPCs.toString().equalsIgnoreCase("{}"))
                    {
                        isPathFindingTaskRunning=false;
                        cancel();
                    }else{
                        for(NPC npc1 : bossNPCs){
                            ProtectedRegion rg = worldGuard.getRegionManager(Bukkit.getWorld(dataManager.getBossesWorld())).getRegion("reality_bossroom_" + bRoomName);
                            if(rg!=null){
                                Region region = new CuboidRegion(rg.getMaximumPoint(), rg.getMinimumPoint());
                                Location centerLoc = new Location(Bukkit.getWorld(dataManager.getBossesWorld()), region.getCenter().getX(), region.getCenter().getY(),region.getCenter().getZ());
                                Collection<Entity> entities = Bukkit.getWorld(dataManager.getBossesWorld()).getNearbyEntities(centerLoc, region.getWidth() / 2, region.getHeight() / 2, region.getLength() / 2);
                                double minDistance = -1;
                                Entity target = null;
                                for(Entity entity : entities){
                                    if(entity!=null){
                                        if(entity instanceof Player){
                                            if(target==null){
                                                minDistance = npc.getEntity().getLocation().distance(entity.getLocation());
                                                target = entity;
                                            }else{
                                                if(npc.getEntity().getLocation().distance(entity.getLocation())<minDistance){
                                                    minDistance = npc.getEntity().getLocation().distance(entity.getLocation());
                                                    target = entity;
                                                }
                                            }
                                        }
                                    }
                                }
                                if(npc1.isSpawned()){
                                    npc1.getNavigator().setTarget(target, true);
                                }
                                if(bossXLoc.containsKey(npc1.getUniqueId()) && bossZLoc.containsKey(npc1.getUniqueId())){
                                    if(bossXLoc.get(npc1.getUniqueId())==npc1.getEntity().getLocation().getBlockX() && bossZLoc.get(npc1.getUniqueId())==npc1.getEntity().getLocation().getBlockZ()){
                                        if(bossStillCount.containsKey(npc1.getUniqueId())){
                                            if(bossStillCount.get(npc1.getUniqueId())>=0.5*4){
                                                effectsAPI.effect(npc.getEntity().getLocation(), EffectsAPI.PlayEffect.EXPLODE);
                                                npc.teleport(centerLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                                                bossStillCount.put(npc1.getUniqueId(), 0.5);
                                            }else{
                                                bossStillCount.put(npc1.getUniqueId(), bossStillCount.get(npc1.getUniqueId())+0.5);
                                            }
                                        }else{
                                            bossStillCount.put(npc1.getUniqueId(), 0.5);
                                        }
                                    }else{
                                        bossStillCount.put(npc1.getUniqueId(), 0.5);
                                        bossXLoc.put(npc1.getUniqueId(), npc1.getEntity().getLocation().getBlockX());
                                        bossZLoc.put(npc1.getUniqueId(), npc1.getEntity().getLocation().getBlockZ());
                                    }
                                }else{
                                    bossXLoc.put(npc1.getUniqueId(), npc1.getEntity().getLocation().getBlockX());
                                    bossZLoc.put(npc1.getUniqueId(), npc1.getEntity().getLocation().getBlockZ());
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 10);
            //10 ticks = 0.5 sec;
        }
    }

    public void stopPathFinding(NPC npc){
        bossNPCs.remove(npc);
        if(bossXLoc!=null && !bossXLoc.toString().equalsIgnoreCase("{}") && bossXLoc.containsKey(npc.getUniqueId())){
            bossXLoc.remove(npc.getUniqueId());
        }
        if(bossZLoc!=null && !bossZLoc.toString().equalsIgnoreCase("{}") && bossZLoc.containsKey(npc.getUniqueId())){
            bossZLoc.remove(npc.getUniqueId());
        }
        if(bossStillCount!=null && !bossStillCount.toString().equalsIgnoreCase("{}") && bossStillCount.containsKey(npc.getUniqueId())){
            bossStillCount.remove(npc.getUniqueId());
        }
    }



}
