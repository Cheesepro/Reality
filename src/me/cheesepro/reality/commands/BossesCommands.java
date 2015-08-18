package me.cheesepro.reality.commands;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.bossrooms.rooms.BRoom;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.eventhandlers.BRoomUpdateEvent;
import me.cheesepro.reality.listeners.BRoomUpdateListener;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 2015-07-22.
 */
public class BossesCommands {

    private Reality plugin;
    private Messenger msg;
    private Config bossRoomsConfig;
    private DataManager dataManager;
    private String bossesWorld;
    private Tools tools;
    private BRoomManager bRoomManager;
    private BossesAPI bossesAPI;

    private WorldEditPlugin worldEdit;
    private WorldGuardPlugin worldGuard;

    private BRoomUpdateListener bRoomUpdateListener;

    public BossesCommands(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        worldEdit = plugin.getWorldEdit();
        worldGuard = plugin.getWorldGuard();
        bossesWorld = plugin.getBossesWorld();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
        bossRoomsConfig = plugin.getBossRoomsConfig();
        bRoomManager = new BRoomManager(plugin);
        bossesAPI = new BossesAPI(plugin);
        bRoomUpdateListener = new BRoomUpdateListener();
    }

    public void commandSet(Player p, String bRoom, String option){
        if(dataManager.isBRoomValid(bRoom)) {
            Location loc = p.getLocation();
            Double x = loc.getX();
            Double y = loc.getY();
            Double z = loc.getZ();
            Double pitch = Double.parseDouble(String.valueOf(loc.getPitch()));
            Double yaw = Double.parseDouble(String.valueOf(loc.getYaw()));
            Map<String, Map<String, Double>> locations;
            if(dataManager.getBRoomsLocations(bRoom)!=null){
                locations = dataManager.getBRoomsLocations(bRoom);
            }else{
                locations = new HashMap<String, Map<String, Double>>();
            }
            Map<String, Double> tempLoc = new HashMap<String, Double>();
            if(option.equalsIgnoreCase("lobby")){
                bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.x", x);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.y", y);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.z", z);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.pitch", pitch);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.yaw", yaw);
                tempLoc.put("x", x);
                tempLoc.put("y", y);
                tempLoc.put("z", z);
                tempLoc.put("pitch", pitch);
                tempLoc.put("yaw", yaw);
                locations.put("lobby", tempLoc);
                msg.send(p, "a", "Lobby location set for room " +bRoom);
            }else if(option.equalsIgnoreCase("end")){
                bossRoomsConfig.set("rooms." + bRoom + ".locations.end.x", x);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.end.y", y);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.end.z", z);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.end.pitch", pitch);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.end.yaw", yaw);
                tempLoc.put("x", x);
                tempLoc.put("y", y);
                tempLoc.put("z", z);
                tempLoc.put("pitch", pitch);
                tempLoc.put("yaw", yaw);
                locations.put("end", tempLoc);
                msg.send(p, "a", "End location set for room " + bRoom);
            }else if(option.equalsIgnoreCase("spawn")){
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spawn.x", x);
                bossRoomsConfig.set("rooms."+bRoom+".locations.spawn.y", y);
                bossRoomsConfig.set("rooms."+bRoom+".locations.spawn.z", z);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spawn.pitch", pitch);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spawn.yaw", yaw);
                tempLoc.put("x", x);
                tempLoc.put("y", y);
                tempLoc.put("z", z);
                tempLoc.put("pitch", pitch);
                tempLoc.put("yaw", yaw);
                locations.put("spawn", tempLoc);
                msg.send(p, "a", "Spawn location set for room " + bRoom);
            }else if(option.equalsIgnoreCase("spectate")){
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spectate.x", x);
                bossRoomsConfig.set("rooms."+bRoom+".locations.spectate.y", y);
                bossRoomsConfig.set("rooms."+bRoom+".locations.spectate.z", z);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spectate.pitch", pitch);
                bossRoomsConfig.set("rooms." + bRoom + ".locations.spectate.yaw", yaw);
                tempLoc.put("x", x);
                tempLoc.put("y", y);
                tempLoc.put("z", z);
                tempLoc.put("pitch", pitch);
                tempLoc.put("yaw", yaw);
                locations.put("spectate", tempLoc);
                msg.send(p, "a", "Spectate location set for room " + bRoom);
            }else if(option.equalsIgnoreCase("bosslocation")){
                bossRoomsConfig.set("rooms." + bRoom + ".boss.spawnlocation.x", x);
                bossRoomsConfig.set("rooms."+bRoom+".boss.spawnlocation.y", y);
                bossRoomsConfig.set("rooms."+bRoom+".boss.spawnlocation.z", z);
                bossRoomsConfig.set("rooms." + bRoom + ".boss.spawnlocation.pitch", pitch);
                bossRoomsConfig.set("rooms." + bRoom + ".boss.spawnlocation.yaw", yaw);
                tempLoc.put("x", x);
                tempLoc.put("y", y);
                tempLoc.put("z", z);
                tempLoc.put("pitch", pitch);
                tempLoc.put("yaw", yaw);
                dataManager.setbRoomsBossesLocations(bRoom, tempLoc);
                msg.send(p, "a", "Boss spawn location set for room " + bRoom);
            }
            dataManager.setBRoomsLocations(bRoom, locations);
            bossRoomsConfig.saveConfig();
        }
    }

    public void commandSet(Player p, String bRoom, String option, String value){
        if(dataManager.isBRoomValid(bRoom)) {
            Map<String, String> settingsCache;
            if (dataManager.isBRoomsSettingsValid(bRoom)) {
                settingsCache = dataManager.getBRoomsSettings(bRoom);
            } else {
                settingsCache = new HashMap<String, String>();
            }
            if (option.equalsIgnoreCase("boss")) {
                if (dataManager.isBossValid(value)) {
                    bossRoomsConfig.set("rooms." + bRoom + ".boss.type", value);
                    bossRoomsConfig.saveConfig();
                    dataManager.setBRoomsBosse(bRoom, value);
                    msg.send(p, "a", "Boss type successfully set for room " + bRoom);
                } else {
                    msg.send(p, "5", "Boss " + value + " is not valid!");
                }
            } else if (option.equalsIgnoreCase("idletimeout")) {
                if (!tools.isInteger(value)) {
                    msg.send(p, "5", "Input must be an integer!");
                    return;
                }
                bossRoomsConfig.set("rooms." + bRoom + ".settings.idletimeout", Integer.parseInt(value));
                settingsCache.put("idletimeout", value);
                msg.send(p, "a", "Idle timeout set for room " + bRoom);
            } else if (option.equalsIgnoreCase("maxplayers")) {
                if (!tools.isInteger(value)) {
                    msg.send(p, "5", "Input must be an integer!");
                    return;
                }
                bossRoomsConfig.set("rooms." + bRoom + ".settings.maxplayers", Integer.parseInt(value));
                settingsCache.put("maxplayers", value);
                msg.send(p, "a", "Max players limit set for room " + bRoom);
            } else if (option.equalsIgnoreCase("minplayers")) {
                if (!tools.isInteger(value)) {
                    msg.send(p, "5", "Input must be an integer!");
                    return;
                }
                bossRoomsConfig.set("rooms." + bRoom + ".settings.minplayers", Integer.parseInt(value));
                settingsCache.put("minplayers", value);
                msg.send(p, "a", "Min players limit set for room " + bRoom);
            }
            bossRoomsConfig.saveConfig();
            dataManager.setBRoomsSettings(bRoom, settingsCache);
            System.out.print(dataManager.getBRoomsSettings(bRoom));
        }else{
            msg.send(p, "4", "Boss Room " + bRoom + " does not exist! Type /reality bossroom create " + bRoom + " to create it!");
        }
    }

    public void commandCreate(Player p, String bRoom) {
        if (!worldGuard.getRegionManager(Bukkit.getWorld(bossesWorld)).hasRegion("reality_bossroom_" + bRoom)) {
            if (bossesWorld.equalsIgnoreCase(p.getWorld().getName())) {
                Selection selection = worldEdit.getSelection(p);
                if (selection != null) {
                    ProtectedCuboidRegion region = new ProtectedCuboidRegion(
                            "reality_bossroom_" + bRoom,
                            new BlockVector(selection.getNativeMinimumPoint()),
                            new BlockVector(selection.getNativeMaximumPoint())
                    );
                    DefaultDomain owners = new DefaultDomain();
                    owners.addPlayer(worldGuard.wrapPlayer(p));
                    region.setOwners(owners);
                    region.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.ENDERPEARL, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.GAME_MODE, GameMode.SURVIVAL);
                    region.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.TNT, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.SLEEP, StateFlag.State.DENY);
                    region.setFlag(DefaultFlag.USE, StateFlag.State.ALLOW);
                    worldGuard.getRegionManager(p.getWorld()).addRegion(region);

                    msg.send(p, "a", "Boss Room " + bRoom + " creation success");
                    msg.send(p, "e", "In order to make the boss room functional, you need to do the following setup commands:");
                    msg.send(p, "d", "/reality bossroom <name> set boss <BossName>");
                    msg.send(p, "d", "/reality bossroom <name> set lobby");
                    msg.send(p, "d", "/reality bossroom <name> set end");
                    msg.send(p, "d", "/reality bossroom <name> set spawn");
                    msg.send(p, "d", "/reality bossroom <name> set spectate");
                    msg.send(p, "d", "/reality bossroom <name> set bosslocation");
                    msg.send(p, "d", "/reality bossroom <name> set maxplayers <amount>");
                    msg.send(p, "d", "/reality bossroom <name> set minplayers <amount>");
                    msg.send(p, "d", "/reality bossroom <name> set idletimeout <amount>");
                } else {
                    msg.send(p, "e", "Please make a worldedit selection of the boss room first!");
                }
            } else {
                msg.send(p, "4", "You must be in world [" + bossesWorld + "] in order to create a boss room!");
            }
        } else {
            msg.send(p, "c", "Boss room " + bRoom + " already exist!");
        }
    }

    public void commandEnable(Player p, String bRoom) {
        if (dataManager.isBRoomValid(bRoom)) {
            if(!dataManager.isBRoomEnabled(bRoom)){
                int check = 0;
                msg.send(p, "a", "Before-Enable Checklist of boss room " + bRoom +":");
                if(dataManager.isBRoomValid(bRoom)){
                    check++;
                    msg.send(p, "e", "Boss type: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Boss type: " + ChatColor.RED + "NULL");
                }
                if(dataManager.isBRoomsBossesLocationsValid(bRoom)){
                    check++;
                    msg.send(p, "e", "Boss spawn location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Boss spawn location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.isBRoomsLocationsValid(bRoom)){
                    if(dataManager.getBRoomsLocations(bRoom).containsKey("lobby")){
                        check++;
                        msg.send(p, "e", "Lobby location: " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Lobby location: " + ChatColor.RED + "NULL");
                    }
                    if(dataManager.getBRoomsLocations(bRoom).containsKey("spectate")){
                        check++;
                        msg.send(p, "e", "Spectate location: " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Spectate location: " + ChatColor.RED + "NULL");
                    }
                    if(dataManager.getBRoomsLocations(bRoom).containsKey("end")){
                        check++;
                        msg.send(p, "e", "End location: " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "End location: " + ChatColor.RED + "NULL");
                    }
                    if(dataManager.getBRoomsLocations(bRoom).containsKey("spawn")){
                        check++;
                        msg.send(p, "e", "Spawn location: " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Spawn location: " + ChatColor.RED + "NULL");
                    }
                }else{
                    msg.send(p, "e", "Lobby location: " + ChatColor.RED + "NULL");
                    msg.send(p, "e", "Spectate location: " + ChatColor.RED + "NULL");
                    msg.send(p, "e", "End location: " + ChatColor.RED + "NULL");
                    msg.send(p, "e", "Spawn location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.isBRoomsSettingsValid(bRoom)){
                    if(dataManager.getBRoomsSettings(bRoom).containsKey("maxplayers")){
                        check++;
                        msg.send(p, "e", "Maximum player(s): " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Maximum player(s): " + ChatColor.RED + "NULL");
                    }
                    if(dataManager.getBRoomsSettings(bRoom).containsKey("minplayers")){
                        check++;
                        msg.send(p, "e", "Minimum player(s): " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Minimum player(s): " + ChatColor.RED + "NULL");
                    }
                    if(dataManager.getBRoomsSettings(bRoom).containsKey("idletimeout")){
                        check++;
                        msg.send(p, "e", "Player idle timeout: " + ChatColor.GREEN + "SET");
                    }else{
                        msg.send(p, "e", "Player idle timeout: " + ChatColor.RED + "NULL");
                    }
                }else{
                    msg.send(p, "e", "Maximum player(s): " + ChatColor.RED + "NULL");
                    msg.send(p, "e", "Minimum player(s): " + ChatColor.RED + "NULL");
                    msg.send(p, "e", "Player idle timeout: " + ChatColor.RED + "NULL");
                }

                if(check==9){
                    bossRoomsConfig.set("rooms." + bRoom + ".enabled", true);
                    bossRoomsConfig.saveConfig();
                    dataManager.setBRoomEnabled(bRoom, true);
                    bRoomManager.addBRoom(bRoom);
                    msg.send(p, "a", "Room " + bRoom + " enabled!");
                    Bukkit.getServer().getPluginManager().callEvent(new BRoomUpdateEvent());
                }
            }else{
                msg.send(p, "a", "Room " + bRoom + " is already enabled.");
            }
        }else{
            msg.send(p, "4", "Boss Room " + bRoom + " does not exist! Type /reality bossroom create " + bRoom + " to create it!");
        }
    }

    public void commandRemove(Player p, String bRoom){
        dataManager.removeBRoom(bRoom);
        worldGuard.getRegionManager(Bukkit.getWorld(bossesWorld)).removeRegion("reality_bossroom_"+bRoom);
        msg.send(p, "d", "Room " + bRoom + " removing action complete.");
    }

    public void commandBuy(Player p, String bRoom){
        if(bRoom.equalsIgnoreCase("menu")){
            p.openInventory(bRoomUpdateListener.getInv());
        }else{
            if(dataManager.isBRoomValid(bRoom)){
                BRoom room = bRoomManager.getBRoom(bRoom);
                if(room.getState() == BRoom.BRoomState.READY){
                    Double cost = bossesAPI.getBoss(room.getBossType()).getRewardMoney() * 3;
                    if(dataManager.playerHasEnoughMoney(p, cost)){
                        if(p.getInventory().firstEmpty()==-1){
                            msg.send(p, "c", "Please leave at least ONE empty inventory slot for the reward you'll get, to prevent other players from picking your reward up on the ground!");
                            return;
                        }
                        dataManager.chargePlayer(p, cost);
                        room.addPlayer(p);
                        msg.send(p, "a", "We have charged you $" + cost + " for the boss room that you have selected");
                        msg.send(p, "e", "We hope you enjoy playing the boss room!");
                        msg.send(p, "d", "Here are some commands that you might find useful:");
                        msg.send(p, "d", "/start " + ChatColor.GOLD + "- Start the count down timer");
                        msg.send(p, "d", "/quit " + ChatColor.GOLD + "- Stop the game immediately (Only applies to the host of the game)");
                        msg.send(p, "d", "/invite <username> " + ChatColor.GOLD + "- Invite your friends to play with you");
                    }else{
                        msg.send(p, "c"  ,"Sorry but you do not have enough money to purchase that boss room!");
                    }
                }else{
                    msg.send(p, "e", "Boss room " +bRoom+ " is currently not available to join, because its status is: " + ChatColor.RED + room.getState());
                    if(room.getState()== BRoom.BRoomState.LOBBY){
                        msg.send(p, "a", "It is still possible to join currently if the host of the game /invite you");
                    }
                }
            }else{
                msg.send(p, "e", bRoom + " is not a valid boss room!");
            }
        }
    }
}
