package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by Mark on 2015-08-01.
 */
public class BRoomPlayerDieListener implements Listener{

    private Reality plugin = Reality.getPlugin();
    private BRoomManager bRoomManager = new BRoomManager(plugin);

    @EventHandler
    public void onPlayerQuit(PlayerDeathEvent e){
        if(bRoomManager.getBRoom(e.getEntity())==null) return;
        bRoomManager.getBRoom(e.getEntity()).playerDie(e.getEntity());
    }

}
