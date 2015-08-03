package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.rooms.BRoom;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mark on 2015-08-01.
 */
public class PlayerQuitListener implements Listener{

    private Reality plugin = Reality.getPlugin();
    private BRoomManager bRoomManager = new BRoomManager(plugin);

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        BRoom bRoom = bRoomManager.getBRoom(e.getPlayer());
        if(bRoom==null) return;
        bRoom.disconnectPlayer(e.getPlayer());
    }

}
