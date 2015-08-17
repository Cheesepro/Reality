package me.cheesepro.reality.bossrooms.rooms;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.Bosses;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.bossrooms.BossesPathFinding;
import me.cheesepro.reality.eventhandlers.BRoomUpdateEvent;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-07-28.
 */
public class BRoom {
    public enum BRoomState {DISABLED, READY, LOBBY, STARTED, COUNTING_DOWN}
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
    private WorldGuardPlugin worldGuard = plugin.getWorldGuard();
    private Boolean stop = false;
    private BossesPathFinding bossesPathFinding = new BossesPathFinding(plugin);
    private NPC bossNPC;

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
        dataManager.getbRoomIdle().addIdleCountDown(p.getUniqueId());
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
        //TODO add Sign support
    }

    public void disconnectPlayer(Player p){
        dataManager.getbRoomIdle().removePlayer(p);
        players.remove(p.getUniqueId());
        currentPlayers--;
        dataManager.removePlayersRoom(p.getUniqueId());
        if(dataManager.getBRoomPlayersRole(p.getUniqueId())){
            stop();
        }else{
            dataManager.removePlayerRole(p.getUniqueId());
        }
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
    }

    public void removePlayer(Player p){
        if(dataManager.getBRoomPlayersRole(p.getUniqueId())){
            dataManager.getbRoomIdle().removePlayer(p);
            stop();
        }else{
            p.teleport(end);
            if(dataManager.getBRoomWinCount(getBRoomName())!=null && dataManager.getBRoomWinCount(getBRoomName())!=0){
                double rewardMoney = (int) Math.round(bossesAPI.getBoss(getBossType()).getRewardMoney()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
                int rewardXP = Math.round(bossesAPI.getBoss(getBossType()).getRewardXP()/currentPlayers * dataManager.getBRoomWinCount(getBRoomName()));
                dataManager.depositPlayer(p, rewardMoney);
                pManager.addXP(p.getUniqueId(), rewardXP);
                msg.send(p, "d", "You have left the room, and you are rewarded with $" + rewardMoney + " dollars and " + rewardXP + "XP!");
            }else{
                msg.send(p, "d", "You have left the room, and you are rewarded with $0 dollars and no XP!");
            }
            players.remove(p.getUniqueId());
            currentPlayers--;
            dataManager.removePlayerRole(p.getUniqueId());
            dataManager.removePlayersRoom(p.getUniqueId());
            dataManager.getbRoomIdle().removePlayer(p);
        }
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());

        //TODO add Sign support
    }

    public void start(){
        stop=false;
        this.state = BRoomState.COUNTING_DOWN;
        //TODO add Sign support
        Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());

        new Countdown(
                false,
                15,
                "Boss Room " + getBRoomName() + " is starting in %t seconds!",
                this,
                15,
                10,
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
            msg.send(p, "e", "You are currently spectating");
        }
    }

    public void bossDie(Entity entity){
        bossesPathFinding.stopPathFinding(bossNPC);
        CitizensAPI.getNPCRegistry().getNPC(entity).destroy();
        new Countdown(
                false,
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
        stop = true;
        bossesPathFinding.stopPathFinding(bossNPC);
        ProtectedRegion rg = worldGuard.getRegionManager(Bukkit.getWorld(dataManager.getBossesWorld())).getRegion("reality_bossroom_" + getBRoomName());
        if(rg!=null){
            Region region = new CuboidRegion(rg.getMaximumPoint(), rg.getMinimumPoint());
            Location centerLoc = new Location(Bukkit.getWorld(dataManager.getBossesWorld()), region.getCenter().getX(), region.getCenter().getY(),region.getCenter().getZ());
            Collection<Entity> entities = Bukkit.getWorld(dataManager.getBossesWorld()).getNearbyEntities(centerLoc, region.getWidth() / 2, region.getHeight() / 2, region.getLength() / 2);
            for(Entity entity : entities){
                if(entity!=null){
                    if(entity.hasMetadata("NPC")) {
                        CitizensAPI.getNPCRegistry().getNPC(entity).destroy();
                    }
                    if(entity instanceof Item){
                        entity.remove();
                    }
                }
            }
        }
        if(dataManager.getBRoomWinCount(getBRoomName())!=null && dataManager.getBRoomWinCount(getBRoomName())!=0 && currentPlayers!=0){
            Set<String> pNames = new HashSet<String>();
            for(UUID id : players){
                pNames.add(Bukkit.getPlayer(id).getName());
            }
            msg.broadcast(ChatColor.GREEN.toString() + "Player(s) " + ChatColor.GOLD + pNames.toString().replace("[", "").replace("]", "") + ChatColor.GREEN + " have killed boss " + ChatColor.RED + getBossType() + " " + ChatColor.GREEN + ChatColor.YELLOW + dataManager.getBRoomWinCount(getBRoomName()) + ChatColor.GREEN + " times!");
            double rewardMoney = (int) Math.round(bossesAPI.getBoss(getBossType()).getRewardMoney()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
            int rewardXP = Math.round(bossesAPI.getBoss(getBossType()).getRewardXP()/currentPlayers*dataManager.getBRoomWinCount(getBRoomName()));
            for(UUID id : players){
                if(Bukkit.getPlayer(id)!=null){
                    dataManager.getbRoomIdle().removePlayer(Bukkit.getPlayer(id));
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
                    dataManager.removePlayersRoom(p.getUniqueId());
                    dataManager.depositPlayer(p, rewardMoney);
                    pManager.addXP(id, rewardXP);
                    msg.send(p, "d", "The room is closed, and you are rewarded with $"+rewardMoney + " dollars and " + rewardXP + "XP!");
                }
            }
        }else{
            for(UUID id : players){
                if(Bukkit.getPlayer(id)!=null){
                    dataManager.getbRoomIdle().removePlayer(Bukkit.getPlayer(id));
                    msg.send(Bukkit.getPlayer(id), "d", "The boss was killed 0 times!");
                    Player p = Bukkit.getPlayer(id);
                    p.teleport(end);
                    dataManager.removePlayerRole(p.getUniqueId());
                    dataManager.removePlayersRoom(p.getUniqueId());
                    msg.send(p, "d", "The room is closed, and you are rewarded with $0 dollars and no XP!");
                }
            }
        }
        dataManager.resetBRoomWinCount(getBRoomName());
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

    public Boolean getStop(){
        return stop;
    }

    public void setBossNPC(NPC npc){
        if(npc!=null){
            bossNPC = npc;
        }
    }

    public NPC getBossNPC(){
        if(bossNPC!=null){
            return bossNPC;
        }
        return null;
    }

    private class Countdown extends BukkitRunnable {

        private int timer;
        private String message;
        private BRoom bRoom;
        private List<Integer> countingNums;

        public Countdown(boolean stop, int start, String message, BRoom bRoom, int... countingNums) {
            if(stop){
                stop();
            }else{
                this.timer = start;
                this.message = message;
                this.bRoom = bRoom;
                this.countingNums = new ArrayList<Integer>();
                for (int i : countingNums) this.countingNums.add(i);
            }
        }

        public void run() {
            if(!bRoom.getStop()){
                if (timer == 0) {
                    for (UUID player : bRoom.getPlayers()) {
                        Bukkit.getPlayer(player).teleport(bRoom.getSpawn());
                        msg.send(Bukkit.getPlayer(player), "a", "The game has started!");
                    }
                    Bosses boss = bossesAPI.getBoss(bRoom.getBossType());
                    bRoom.setBossNPC(boss.getNPC());
                    boss.spawn(bRoom.getBossLocation());
                    bossesPathFinding.startPathFinding(bRoom.getBossNPC(), bRoom.getBRoomName());
                    //TODO FIX BOSS SPAWN TWICE WHEN TWO BOSS ROOM HAVE THE SAME BOSS
                    //TODO Fix player do no damage to boss due to worldguard flags
                    bRoom.state = BRoomState.STARTED;
                    Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
                    cancel();
                }

                if (countingNums.contains(timer)) {
                    for (UUID player : bRoom.getPlayers()){
                        msg.send(Bukkit.getPlayer(player), "d", message.replace("%t", timer + ""));
                    }
                }
                timer--;
            }else{
                cancel();
            }
        }
    }

}
