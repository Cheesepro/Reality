package me.cheesepro.reality.bossrooms.rooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2015-07-28.
 */
public class BRoomManager {

    private DataManager dataManager;
    private static List<BRoom> bRooms = new ArrayList<BRoom>();

    public BRoomManager(Reality plugin){
        dataManager = new DataManager(plugin);
    }

    public void setupBRooms() {
        for (String key : dataManager.getBRooms()) {
            bRooms.add(new BRoom(key));
        }
    }

    public List<BRoom> getBRooms(){
        return bRooms;
    }

    public BRoom getBRoom(String name) {
        for (BRoom a : bRooms) {
            if (a.getBRoomName().equalsIgnoreCase(name)) return a;
        }
        return null;
    }

    public BRoom getBRoom(Player p) {
        for (BRoom a : bRooms) {
            if (a.containsPlayer(p)) return a;
        }
        return null;
    }
}