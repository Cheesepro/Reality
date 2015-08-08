package me.cheesepro.reality.bossrooms.rooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.eventhandlers.BRoomUpdateEvent;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-07-28.
 */
public class BRoom {
    public enum BRoomState {DISABLED, READY, LOBBY, STARTED, COUNTING_DOWN;}
    private BRoomState state = BRoomState.DISABLED;
    private Reality plugin = Reality.getPlugin();
    private Location spawn, lobby, end, spectate, bossLocation;
    private int maxPlayer, minPlayer, idleTimeout, currentPlayers = 0;
    private String BRoomName, bossType;
    private Set<UUID> players = new HashSet<UUID>();
    private DataManager dataManager = new DataManager(plugin);
    private Messenger msg = new Messenger(plugin);
    private BossesAPI bossesAPI= new BossesAPI(plugin);
    private PlayerManager pManager = new PlayerManager(plugin);

    public BRoom(String name) {
        BRoomName = name;
        spawn = new Location(Bukkit.getServer().getWorld(dataManager.getBossesWorld()),
                dataManager.getBRoomsLocations(name).get("spawn").get("x"),
                dataManager.getBRoomsLocations(name).get("spawn").get("y"),
                dataManager.getBRoomsLocations(name).get("spawn").get("z"),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("spawn").get("pitch"))),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("spawn").get("yaw"))));
        lobby = new Location(Bukkit.getServer().getWorld(dataManager.getBossesWorld()),
                dataManager.getBRoomsLocations(name).get("lobby").get("x"),
                dataManager.getBRoomsLocations(name).get("lobby").get("y"),
                dataManager.getBRoomsLocations(name).get("lobby").get("z"),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("lobby").get("pitch"))),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("lobby").get("yaw"))));
        end = new Location(Bukkit.getServer().getWorld(dataManager.getBossesWorld()),
                dataManager.getBRoomsLocations(name).get("end").get("x"),
                dataManager.getBRoomsLocations(name).get("end").get("y"),
                dataManager.getBRoomsLocations(name).get("end").get("z"),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("end").get("pitch"))),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("end").get("yaw"))));
        spectate = new Location(Bukkit.getServer().getWorld(dataManager.getBossesWorld()),
                dataManager.getBRoomsLocations(name).get("spectate").get("x"),
                dataManager.getBRoomsLocations(name).get("spectate").get("y"),
                dataManager.getBRoomsLocations(name).get("spectate").get("z"),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("spectate").get("pitch"))),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsLocations(name).get("spectate").get("yaw"))));
        bossLocation = new Location(Bukkit.getServer().getWorld(dataManager.getBossesWorld()),
                dataManager.getBRoomsBossesLocations(name).get("x"),
                dataManager.getBRoomsBossesLocations(name).get("y"),
                dataManager.getBRoomsBossesLocations(name).get("z"),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsBossesLocations(name).get("pitch"))),
                Float.parseFloat(String.valueOf(dataManager.getBRoomsBossesLocations(name).get("yaw"))));
        maxPlayer = Integer.parseInt(dataManager.getBRoomsSettings(name).get("maxplayers"));
        minPlayer = Integer.parseInt(dataManager.getBRoomsSettings(name).get("minplayers"));
        idleTimeout = Integer.parseInt(dataManager.getBRoomsSettings(name).get("idletimeout"));
        bossType = dataManager.getBRoomBoss(name);
        state = BRoomState.READY;
    }

    public String getBRoomName(){
        return BRoomName;
    }

    public BRoomState getState(){
        return state;
    }

    public void addPlayer(Player p){
        if(currentPlayers >= maxPlayer){
            msg.send(p, "c", "Sorry this room is already FULL!");
            return;
        }

        players.add(p.getUniqueId());
        if(currentPlayers==0){
            dataManager.setbRoomPlayersRole(p.getUniqueId(), true);
        }else{
            dataManager.setbRoomPlayersRole(p.getUniqueId(), false);
        }
        dataManager.setbRoomPlayersRoom(p.getUniqueId(), getBRoomName());
        p.teleport(lobby);
        state = BRoomState.LOBBY;
        currentPlayers++;
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
        //TODO add Sign support

    }

    public void disconnectPlayer(Player p){
        players.remove(p.getUniqueId());
        currentPlayers--;
        dataManager.removePlayerRole(p.getUniqueId());
        if(dataManager.getBRoomPlayersRole(p.getUniqueId())){
            stop();
        }
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
    }

    public void removePlayer(Player p){
        if(dataManager.getBRoomPlayersRole(p.getUniqueId())){
            stop();
        }else{
            p.teleport(end);
            double rewardMoney = (int) Math.round(bossesAPI.getBoss(getBossType()).getRewardMoney()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
            int rewardXP = Math.round(bossesAPI.getBoss(getBossType()).getRewardXP()/currentPlayers * dataManager.getBRoomWinCount(getBRoomName()));
            dataManager.depositPlayer(p, rewardMoney);
            pManager.addXP(p.getUniqueId(), rewardXP);
            players.remove(p.getUniqueId());
            currentPlayers--;
            dataManager.removePlayerRole(p.getUniqueId());
            msg.send(p, "d", "You have left the room, and you are rewarded with $"+rewardMoney + " dollars and " + rewardXP + "XP!");
        }
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());

        //TODO add Sign support

    }

    public void start(){
        this.state = BRoomState.COUNTING_DOWN;
        //TODO add Sign support
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());

        new Countdown(
                15,
                "Boss Room " + getBRoomName() + " is starting in %t seconds!",
                this,
                15,
                10,
                9,
                5,
                4,
                3,
                2,
                1
        ).runTaskTimer(Reality.getPlugin(), 0, 20);
        //20 ticks == 1 second
    }

    public void playerDie(Player p){
        if(dataManager.getBRoomPlayersRole(p.getUniqueId())) {
            stop();
        }else{
            p.teleport(spectate);
        }
    }

    public void bossDie(){
        new Countdown(
                10,
                "Boss will respawn in %t seconds!",
                this,
                10,
                5,
                4,
                3,
                2,
                1
        ).runTaskTimer(Reality.getPlugin(), 0, 20);
    }

    public void stop(){
        if(dataManager.getBRoomWinCount(getBRoomName())!=null && dataManager.getBRoomWinCount(getBRoomName())!=0){
            Set<String> pNames = new HashSet<String>();
            for(UUID id : players){
                pNames.add(Bukkit.getPlayer(id).getName());
            }
            msg.broadcast(ChatColor.GREEN.toString() + "Player(s) " + ChatColor.GOLD + pNames.toString().replace("[", "").replace("]", "") + ChatColor.GREEN + " have killed boss " + ChatColor.RED + getBossType() + " " + ChatColor.GREEN + ChatColor.YELLOW + dataManager.getBRoomWinCount(getBRoomName()) + ChatColor.GREEN + " times!");
            double rewardMoney = (int) Math.round(bossesAPI.getBoss(getBossType()).getRewardMoney()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
            int rewardXP = Math.round(bossesAPI.getBoss(getBossType()).getRewardXP()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
            for(UUID id : players){
                Player p = Bukkit.getPlayer(id);
                p.teleport(end);
                if(dataManager.getBRoomPlayersRole(id)){
                    msg.send(p, "d", "You just received " + bossesAPI.getBoss(getBossType()).getRewardKey()*dataManager.getBRoomWinCount(getBRoomName()) + " lucky crate keys!");
                    ItemStack crateKeys = dataManager.getCrateKey();
                    crateKeys.setAmount(bossesAPI.getBoss(getBossType()).getRewardKey()*dataManager.getBRoomWinCount(getBRoomName()));
                    HashMap<Integer, ItemStack> nope = p.getInventory().addItem(crateKeys);
                    for(Map.Entry<Integer, ItemStack> entry : nope.entrySet())
                    {
                        msg.send(p, "d", "Your inventory is full, placing your reward(s) on the ground!");
                        p.getWorld().dropItemNaturally(p.getLocation(), entry.getValue());
                    }
                }
                dataManager.removePlayerRole(p.getUniqueId());
                dataManager.resetBRoomWinCount(getBRoomName());
                dataManager.removePlayersRoom(p.getUniqueId());
                dataManager.depositPlayer(p, rewardMoney);
                pManager.addXP(id, rewardXP);
                msg.send(p, "d", "The room is closed, and you are rewarded with $"+rewardMoney + " dollars and " + rewardXP + "XP!");
            }
        }else{
            for(UUID id : players){
                msg.send(Bukkit.getPlayer(id), "d", "The boss was killed 0 times!");
                Player p = Bukkit.getPlayer(id);
                p.teleport(end);
                dataManager.removePlayerRole(p.getUniqueId());
                dataManager.resetBRoomWinCount(getBRoomName());
                dataManager.removePlayersRoom(p.getUniqueId());
                msg.send(p, "d", "The room is closed, and you are rewarded with $0 dollars and no XP!");
            }
        }
        players.clear();
        currentPlayers = 0;
        state = BRoomState.READY;
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
    }

    public Boolean containsPlayer(Player p){
        return players.contains(p.getUniqueId());
    }

    // Getters


    public Location getSpawn() {
        return spawn;
    }

    public Location getLobby() {
        return lobby;
    }

    public Location getEnd() {
        return end;
    }

    public Location getSpectate() {
        return spectate;
    }

    public Location getBossLocation() {
        return bossLocation;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayer(Integer amount){
        currentPlayers = amount;
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
    }

    public String getBossType() {
        return bossType;
    }

    public Set<UUID> getPlayers() {
        return this.players;
    }

    private class Countdown extends BukkitRunnable {

        private int timer;
        private String message;
        private BRoom bRoom;
        private ArrayList<Integer> countingNums;

        public Countdown(int start, String message, BRoom bRoom, int... countingNums) {
            this.timer = start;
            this.message = message;
            this.bRoom = bRoom;
            this.countingNums = new ArrayList<Integer>();
            for (int i : countingNums) this.countingNums.add(i);
        }

        public void run() {
            if (timer == 0) {
                for (UUID player : players) {
                    Bukkit.getPlayer(player).teleport(spawn);
                    msg.send(Bukkit.getPlayer(player), "a", "The game has started!");
                    bossesAPI.getBoss(getBossType()).spawn(bossLocation);
                }
                bRoom.state = BRoomState.STARTED;
                Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
                cancel();
            }

            if (countingNums.contains(timer)) {
                for (UUID player : players){
                    msg.send(Bukkit.getPlayer(player), "d", message.replace("%t", timer + ""));
                }
            }
            timer--;
        }
    }

}
