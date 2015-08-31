package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2015-08-01.
 */
public class PlayerDeathListener implements Listener{

    private Reality plugin;
    private BRoomManager bRoomManager;
    private DataManager dataManager;

    public PlayerDeathListener(Reality plugin){
        this.plugin = plugin;
        bRoomManager = new BRoomManager(plugin);
        dataManager = new DataManager(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e){
        if(bRoomManager.getBRoom(e.getEntity())!=null){
            e.setKeepInventory(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getEntity().spigot().respawn();
                    e.getEntity().setCanPickupItems(false);
                    if(!dataManager.getBRoomPlayersRole(e.getEntity().getUniqueId())){
                        e.getEntity().teleport(bRoomManager.getBRoom(e.getEntity()).getSpectate());
                    }
                    bRoomManager.getBRoom(e.getEntity()).playerDie(e.getEntity());
                }
            }.runTaskLater(plugin, 5);
        }else{
            List<String> abilitiesItemNames = dataManager.getAbilitiesItemNames();
            List<ItemStack> removeItems = new ArrayList<ItemStack>();
            for(ItemStack item : e.getDrops()){
                if(abilitiesItemNames.contains(item.getItemMeta().getDisplayName())){
                    removeItems.add(item);
                }
            }
            for(ItemStack remove : removeItems){
                e.getDrops().remove(remove);
            }
        }
    }

}
