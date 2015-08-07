package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoom;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mark on 2015-07-30.
 */
public class BRoomBossesDieListener implements Listener {

    private Reality plugin;
    private BRoomManager bRoomManager;
    private BossesAPI bossesAPI;
    private DataManager dataManager;
    private List<EntityType> entities = new ArrayList<EntityType>();

    private Messenger msg = new Messenger(plugin);

    public BRoomBossesDieListener(Reality plugin){
        this.plugin = plugin;
        bRoomManager = new BRoomManager(plugin);
        bossesAPI = new BossesAPI(plugin);
        dataManager = new DataManager(plugin);
        entities.add(EntityType.BLAZE);
        entities.add(EntityType.CHICKEN);
        entities.add(EntityType.COW);
        entities.add(EntityType.CREEPER);
        entities.add(EntityType.ENDERMAN);
        entities.add(EntityType.PIG);
        entities.add(EntityType.SKELETON);
        entities.add(EntityType.SPIDER);
        entities.add(EntityType.ZOMBIE);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBossDie(EntityDeathEvent e){
        for(BRoom bRoom : bRoomManager.getBRooms()) {
            if (isEntityBoss(e, bossesAPI.getBoss(bRoom.getBossType()).getName())) {
                dataManager.addBRoomWinCount(bRoom.getBRoomName(), 1);
                bRoom.bossDie();
                for(UUID id : bRoom.getPlayers()){
                    msg.send(Bukkit.getPlayer(id), "a", "Boss " + bRoom.getBossType() + " got beaten " + dataManager.getBRoomWinCount(bRoom.getBRoomName()) + " times!");
                }
            }
        }
    }

    private Boolean isEntityBoss(EntityDeathEvent e, String name){
        if(entities.contains(e.getEntityType())){
            if(e.getEntity().getCustomName().equalsIgnoreCase(ChatColor.RED.toString() + ChatColor.BOLD + "BOSS " + ChatColor.BOLD + name)){
                return true;
            }
        }
        return false;
    }

}
