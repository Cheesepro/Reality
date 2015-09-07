package me.cheesepro.reality.bossrooms;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.bosses.BossSkeleton;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.EffectsAPI;
import me.cheesepro.reality.utils.Messenger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-08-12.
 */
public class BossesPathFinding {

    private Reality plugin;
    private static Map<UUID, String> bossNPCsINFO = new HashMap<UUID, String>();
    private static boolean isPathFindingTaskRunning;
    private Messenger msg;
    private WorldGuardPlugin worldGuard;
    private DataManager dataManager;
    private Map<UUID, Integer> bossXLoc = new HashMap<UUID, Integer>();
    private Map<UUID, Integer> bossZLoc = new HashMap<UUID, Integer>();
    private Map<UUID, Integer> bossStillCount = new HashMap<UUID, Integer>();
    private BossSkeleton bossSkeleton;

    public BossesPathFinding(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        dataManager = new DataManager(plugin);
        worldGuard = plugin.getWorldGuard();
        bossSkeleton = new BossSkeleton(plugin);
    }

    public void startPathFinding(NPC npc, String bRoomName){
        if(npc!=null){
            bossNPCsINFO.put(npc.getUniqueId(), bRoomName);
            if(!isPathFindingTaskRunning){
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        isPathFindingTaskRunning=true;
                        if(bossNPCsINFO.isEmpty() || bossNPCsINFO.toString().equalsIgnoreCase("{}"))
                        {
                            isPathFindingTaskRunning=false;
                            cancel();
                        }else{
                            for(UUID npcID : bossNPCsINFO.keySet()){
                                NPC npc1 = CitizensAPI.getNPCRegistry().getByUniqueId(npcID);
                                if(npc1!=null){
                                    ProtectedRegion rg = worldGuard.getRegionManager(Bukkit.getWorld(dataManager.getBossesWorld())).getRegion("reality_bossroom_" + bossNPCsINFO.get(npcID));
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
                                                        minDistance = npc1.getEntity().getLocation().distance(entity.getLocation());
                                                        target = entity;
                                                    }else{
                                                        if(npc1.getEntity().getLocation().distance(entity.getLocation())<minDistance){
                                                            minDistance = npc1.getEntity().getLocation().distance(entity.getLocation());
                                                            target = entity;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if(target!=null){
                                            if(npc1.isSpawned()){
                                                npc1.getNavigator().setTarget(target, true);
                                            }
                                        }
                                        if(npc1.isSpawned() && npc1.getEntity().isValid()){
                                            if(bossXLoc.containsKey(npc1.getUniqueId()) && bossZLoc.containsKey(npc1.getUniqueId())){
                                                if(bossXLoc.get(npc1.getUniqueId())==npc1.getEntity().getLocation().getBlockX() && bossZLoc.get(npc1.getUniqueId())==npc1.getEntity().getLocation().getBlockZ()){
                                                    if(bossStillCount.containsKey(npc1.getUniqueId())){
                                                        if(bossStillCount.get(npc1.getUniqueId())>=3){
                                                            EffectsAPI.effect(npc1.getEntity().getLocation(), EffectsAPI.PlayEffect.EXPLODE);
                                                            npc1.teleport(centerLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                                                            bossStillCount.put(npc1.getUniqueId(), 1);
                                                        }else{
                                                            bossStillCount.put(npc1.getUniqueId(), bossStillCount.get(npc1.getUniqueId())+1);
                                                        }
                                                    }else{
                                                        bossStillCount.put(npc1.getUniqueId(), 1);
                                                    }
                                                }else{
                                                    bossStillCount.put(npc1.getUniqueId(), 1);
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
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
                //TODO Make thread async
                //20 ticks = 1 sec;
            }
        }
    }

    public void stopPathFinding(NPC npc){
        if(npc!=null){
            if(bossNPCsINFO!=null && !bossNPCsINFO.toString().equalsIgnoreCase("{}") && bossNPCsINFO.containsKey(npc.getUniqueId())){
                bossNPCsINFO.remove(npc.getUniqueId());
            }
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



}
