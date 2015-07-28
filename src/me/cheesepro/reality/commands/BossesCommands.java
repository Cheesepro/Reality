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
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
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

    Reality plugin;
    Messenger msg;
    private Config bossRoomsConfig;
    private DataManager dataManager;
    String bossesWorld;
    Tools tools;

    WorldEditPlugin worldEdit;
    WorldGuardPlugin worldGuard;

    public BossesCommands(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        worldEdit = plugin.getWorldEdit();
        worldGuard = plugin.getWorldGuard();
        bossesWorld = plugin.getBossesWorld();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
        bossRoomsConfig = plugin.getBossRoomsConfig();
    }

    public void commandSet(Player p, String bRoom, String option, String value){
        if(dataManager.isBRoomValid(bRoom)){
            if(value!=null){
                Map<String, String> settingsCache;
                if(dataManager.getBRoomsSettings(bRoom)!=null){
                    settingsCache = dataManager.getBRoomsSettings(bRoom);
                }else{
                    settingsCache = new HashMap<String, String>();
                }
                if(option.equalsIgnoreCase("boss")){
                    if(dataManager.isBossValid(value)){
                        bossRoomsConfig.set("rooms."+bRoom+".boss.type", value);
                        bossRoomsConfig.saveConfig();
                        dataManager.setBRoomsBosses(bRoom, value);
                    }else{
                        msg.send(p, "5", "Boss " + value + " is not valid!");
                    }
                }else if(option.equalsIgnoreCase("idletimeout")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms." + bRoom + ".settings.idletimeout", value);
                    settingsCache.put("idletimeout", value);
                }else if(option.equalsIgnoreCase("maxplayers")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms."+bRoom+".settings.maxplayers", value);
                    settingsCache.put("maxplayers", value);
                }else if(option.equalsIgnoreCase("minplayers")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms."+bRoom+".settings.minplayers", value);
                    settingsCache.put("minplayers", value);
                }
                bossRoomsConfig.saveConfig();
                dataManager.setBRoomsSettings(bRoom, settingsCache);
            }else{
                Location loc = p.getLocation();
                Double x = loc.getX();
                Double y = loc.getY();
                Double z = loc.getZ();
                Map<String, Map<String, Double>> locations = new HashMap<String, Map<String, Double>>();
                Map<String, Double> tempLoc = new HashMap<String, Double>();
                if(option.equalsIgnoreCase("lobby")){
                    tempLoc.clear();
                    bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.x", x);
                    bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.y", y);
                    bossRoomsConfig.set("rooms." + bRoom + ".locations.lobby.z", z);
                    tempLoc.put("x", x);
                    tempLoc.put("y", y);
                    tempLoc.put("z", z);
                    locations.put("lobby", tempLoc);
                }else if(option.equalsIgnoreCase("end")){
                    tempLoc.clear();
                    bossRoomsConfig.set("rooms." + bRoom + ".locations.end.x", x);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.end.y", y);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.end.z", z);
                    tempLoc.put("x", x);
                    tempLoc.put("y", y);
                    tempLoc.put("z", z);
                    locations.put("end", tempLoc);
                }else if(option.equalsIgnoreCase("spawn")){
                    tempLoc.clear();
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spawn.x", x);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spawn.y", y);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spawn.z", z);
                    tempLoc.put("x", x);
                    tempLoc.put("y", y);
                    tempLoc.put("z", z);
                    locations.put("spawn", tempLoc);
                }else if(option.equalsIgnoreCase("spectate")){
                    tempLoc.clear();
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spectate.x", x);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spectate.y", y);
                    bossRoomsConfig.set("rooms."+bRoom+".locations.spectate.z", z);
                    tempLoc.put("x", x);
                    tempLoc.put("y", y);
                    tempLoc.put("z", z);
                    locations.put("spectate", tempLoc);
                }else if(option.equalsIgnoreCase("bosslocation")){
                    tempLoc.clear();
                    bossRoomsConfig.set("rooms."+bRoom+".boss.spawnlocation.x", x);
                    bossRoomsConfig.set("rooms."+bRoom+".boss.spawnlocation.y", y);
                    bossRoomsConfig.set("rooms."+bRoom+".boss.spawnlocation.z", z);
                    tempLoc.put("x", x);
                    tempLoc.put("y", y);
                    tempLoc.put("z", z);
                    locations.put("bosslocation", tempLoc);
                }
                dataManager.setBRoomsLocations(bRoom, locations);
                bossRoomsConfig.saveConfig();
            }
        }else{
            msg.send(p, "4", "Boss Room " + bRoom + " does not exist! Type /reality bossroom create " + bRoom + " to create it!");
        }
    }

    public void commandCreate(Player p, String bRoom){
        if(bossesWorld.equalsIgnoreCase(p.getWorld().getName())){
            Selection selection = worldEdit.getSelection(p);
            if(selection!=null){
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
                region.setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
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

            }else{
                msg.send(p, "e", "Please make a worldedit selection of the boss room first!");
            }
        }else{
            msg.send(p, "4", "You must be in world ["+bossesWorld+"] in order to create a boss room!");
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
                if(dataManager.getBRoomsBossesLocations(bRoom)!=null){
                    check++;
                    msg.send(p, "e", "Boss spawn location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Boss spawn location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsLocations(bRoom).get("lobby")!=null){
                    check++;
                    msg.send(p, "e", "Lobby location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Lobby location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsLocations(bRoom).get("spectate")!=null){
                    check++;
                    msg.send(p, "e", "Spectate location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Spectate location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsLocations(bRoom).get("end")!=null){
                    check++;
                    msg.send(p, "e", "End location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "End location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsLocations(bRoom).get("spawn")!=null){
                    check++;
                    msg.send(p, "e", "Spawn location: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Spawn location: " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsSettings(bRoom).get("maxplayers")!=null){
                    check++;
                    msg.send(p, "e", "Maximum player(s): " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Maximum player(s): " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsSettings(bRoom).get("minplayers")!=null){
                    check++;
                    msg.send(p, "e", "Minimum player(s): " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Minimum player(s): " + ChatColor.RED + "NULL");
                }
                if(dataManager.getBRoomsSettings(bRoom).get("idletimeout")!=null){
                    check++;
                    msg.send(p, "e", "Player idle timeout: " + ChatColor.GREEN + "SET");
                }else{
                    msg.send(p, "e", "Player idle timeout: " + ChatColor.RED + "NULL");
                }
                if(check==9){
                    dataManager.setBRoomEnabled(bRoom, true);
                }
            }else{
                msg.send(p, "a", "Room " + bRoom + " is already enabled.");
            }
        }else{
            msg.send(p, "4", "Boss Room " + bRoom + " does not exist! Type /reality bossroom create " + bRoom + " to create it!");
        }
    }

    public void commandRemove(Player p, String bRoom){

    }

    public void commandJoin(Player p, String bRoom){

    }

    public void commandQuit(Player p, String bRoom){

    }


}
