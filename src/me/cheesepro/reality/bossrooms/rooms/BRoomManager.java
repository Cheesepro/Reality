package me.cheesepro.reality.bossrooms.rooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.bossrooms.BossesPathFinding;
import me.cheesepro.reality.eventhandlers.BRoomUpdateEvent;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-07-28.
 */
public class BRoomManager {

    private DataManager dataManager;
    private static List<BRoom> bRooms = new ArrayList<BRoom>();
    private Reality plugin;
    private Messenger msg;
    private BossesAPI bossesAPI;
    private BossesPathFinding bossesPathFinding;
    private static boolean isCDTaskRunning;
    private static Map<BRoom, List<Integer>> bRoomCountDown = new HashMap<BRoom, List<Integer>>();
    private static Map<BRoom, String> bRoomMessage = new HashMap<BRoom, String>();
    private static Map<BRoom, Integer> bRoomTimer = new HashMap<BRoom, Integer>();
    private static List<BRoom> toBeRemoved = new ArrayList<BRoom>();


    public BRoomManager(Reality plugin) {
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        msg = new Messenger(plugin);
        bossesAPI = new BossesAPI(plugin);
        bossesPathFinding = new BossesPathFinding(plugin);
    }

    public void setupBRooms() {
        for (String key : dataManager.getBRooms()) {
            bRooms.add(new BRoom(key));
        }
    }

    public void addBRoom(String key) {
        bRooms.add(new BRoom(key));
    }

    public void reloadBRoom(String key) {
        for (String room : dataManager.getBRooms()) {
            if (key.equalsIgnoreCase(room)) {
                getBRoom(room).reloadInfo();
            }
        }
    }

    public List<BRoom> getBRooms() {
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

    public void addStartCountDown(int start, String message, BRoom inputBRoom, int... countingNums) {
        List<Integer> numsList = new ArrayList<Integer>();
        for (int i : countingNums) numsList.add(i);
        bRoomCountDown.put(inputBRoom, numsList);
        bRoomMessage.put(inputBRoom, message);
        bRoomTimer.put(inputBRoom, start);
        if (!isCDTaskRunning) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    isCDTaskRunning = true;
                    if (isQueueEmpty()) {
                        isCDTaskRunning = false;
                        cancel();
                    }
                    for (BRoom bRoom : bRoomCountDown.keySet()) {
                        if (!bRoom.getStop()) {
                            if (bRoomTimer.get(bRoom) == 0) {
                                for (UUID player : bRoom.getPlayers()) {
                                    Bukkit.getPlayer(player).teleport(bRoom.getSpawn());
                                    msg.send(Bukkit.getPlayer(player), "a", "The game has started!");
                                }
                                Bosses boss = bossesAPI.getBoss(bRoom.getBossType());
                                bRoom.setBossNPC(boss.getNPC());
                                boss.spawn(bRoom.getBossLocation());
                                bossesPathFinding.startPathFinding(bRoom.getBossNPC(), bRoom.getBRoomName());
                                bRoom.setState(BRoom.BRoomState.STARTED);
                                Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
                                toBeRemoved.add(bRoom);
                            }else{
                                if (bRoomCountDown.get(bRoom).contains(bRoomTimer.get(bRoom))) {
                                    for (UUID player : bRoom.getPlayers()){
                                        msg.send(Bukkit.getPlayer(player), "d", bRoomMessage.get(bRoom).replace("%t", bRoomTimer.get(bRoom) + ""));
                                    }
                                }
                                bRoomTimer.put(bRoom, bRoomTimer.get(bRoom)-1);
                            }
                        } else {
                            toBeRemoved.add(bRoom);
                        }
                    }
                    if(!toBeRemoved.isEmpty() || !toBeRemoved.toString().equalsIgnoreCase("[]")){
                        for(BRoom remove : toBeRemoved){
                            removeBRoom(remove);
                        }
                        toBeRemoved.clear();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
            //20 ticks = 1 sec;
        }
    }

    private boolean isQueueEmpty() {
        if (bRoomCountDown.isEmpty() && bRoomTimer.isEmpty() && bRoomMessage.isEmpty() || bRoomCountDown.toString().equalsIgnoreCase("{}") && bRoomTimer.toString().equalsIgnoreCase("{}") && bRoomMessage.toString().equalsIgnoreCase("{}")) {
            return true;
        }
        return false;
    }

    private void removeBRoom(BRoom bRoom) {
        if(bRoomCountDown.containsKey(bRoom)){
            bRoomCountDown.remove(bRoom);
        }
        if(bRoomTimer.containsKey(bRoom)){
            bRoomTimer.remove(bRoom);
        }
        if(bRoomMessage.containsKey(bRoom)){
            bRoomMessage.remove(bRoom);
        }
    }
}