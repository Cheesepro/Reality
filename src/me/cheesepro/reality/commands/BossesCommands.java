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
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Inventory invBuyMain;

    private WorldEditPlugin worldEdit;
    private WorldGuardPlugin worldGuard;

    public BossesCommands(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        worldEdit = plugin.getWorldEdit();
        worldGuard = plugin.getWorldGuard();
        bossesWorld = plugin.getBossesWorld();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
        bossRoomsConfig = plugin.getBossRoomsConfig();
        BossesAPI bossesAPI = new BossesAPI(plugin);
        BRoomManager bRoomManager = new BRoomManager(plugin);

        invBuyMain = Bukkit.getServer().createInventory(null, 54, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Boss room shop!");
        int slot = 0;
        for(String bRoom : dataManager.getBRooms()){
            String bossType = bRoomManager.getBRoom(bRoom).getBossType();
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner("MHF_" +bRoomManager.getBRoom(bRoom).getBossType());
            meta.setDisplayName(ChatColor.YELLOW.toString()+ChatColor.STRIKETHROUGH+"---------"+ChatColor.YELLOW+"["+ChatColor.BLUE+"INFO"+ChatColor.YELLOW.toString()+"]"+ChatColor.STRIKETHROUGH+"---------");
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD.toString() + "Room name: " + ChatColor.LIGHT_PURPLE + bRoom);
            lore.add(ChatColor.GOLD.toString() + "Boss: "+ ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getName());
            lore.add(ChatColor.GOLD.toString() + "Boss Health: "+ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getHealth());
            lore.add(ChatColor.GOLD.toString() + "Boss Damage: "+ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getDamage());
            lore.add(ChatColor.GOLD.toString() + "Room Status: "+ChatColor.LIGHT_PURPLE + bRoomManager.getBRoom(bRoom).getState());
            lore.add(ChatColor.GOLD.toString() + "Slots: " +ChatColor.LIGHT_PURPLE +  bRoomManager.getBRoom(bRoom).getCurrentPlayers()+"/" + bRoomManager.getBRoom(bRoom).getMaxPlayer());
            lore.add(ChatColor.GOLD.toString() + "Cost: "+ChatColor.LIGHT_PURPLE+"$"+bossesAPI.getBoss(bossType).getRewardMoney()*3);
            lore.add(ChatColor.YELLOW.toString()+ChatColor.STRIKETHROUGH+"--------"+ ChatColor.YELLOW +"["+ChatColor.RED+"Rewards"+ChatColor.YELLOW.toString()+"]"+ChatColor.STRIKETHROUGH+"-------");
            lore.add(ChatColor.GREEN + "Lucky Crate Key(s): " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardKey());
            lore.add(ChatColor.GREEN + "Money: " + ChatColor.AQUA +"$"+bossesAPI.getBoss(bossType).getRewardMoney());
            lore.add(ChatColor.GREEN + "XP: " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardXP());
            lore.add(ChatColor.YELLOW.toString()+ChatColor.STRIKETHROUGH+"------------------------");
            meta.setLore(lore);
            skull.setItemMeta(meta);
            invBuyMain.setItem(slot, skull);
        }
    }

    //TODO CREATE a boss room information change eventhandlers and place the activator in the BRoom.java and trigger the event to update the inventory above

    public void commandSet(Player p, String bRoom, String option){
        if(dataManager.isBRoomValid(bRoom)) {
            Location loc = p.getLocation();
            Double x = loc.getX();
            Double y = loc.getY();
            Double z = loc.getZ();
            Double pitch = Double.parseDouble(String.valueOf(loc.getPitch()));
            Double yaw = Double.parseDouble(String.valueOf(loc.getYaw()));
            Map<String, Map<String, Double>> locations = new HashMap<String, Map<String, Double>>();
            Map<String, Double> tempLoc = new HashMap<String, Double>();
            if(option.equalsIgnoreCase("lobby")){
                tempLoc.clear();
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
                tempLoc.clear();
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
                tempLoc.clear();
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
                tempLoc.clear();
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
                tempLoc.clear();
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
                locations.put("bosslocation", tempLoc);
                msg.send(p, "a", "Boss spawn location set for room " + bRoom);
            }
            dataManager.setBRoomsLocations(bRoom, locations);
            bossRoomsConfig.saveConfig();
        }
    }

    public void commandSet(Player p, String bRoom, String option, String value){
        if(dataManager.isBRoomValid(bRoom)){
                Map<String, String> settingsCache;
                if(dataManager.isBRoomsSettingsValid(bRoom)){
                    settingsCache = dataManager.getBRoomsSettings(bRoom);
                }else{
                    settingsCache = new HashMap<String, String>();
                }
                if(option.equalsIgnoreCase("boss")){
                    if(dataManager.isBossValid(value)){
                        bossRoomsConfig.set("rooms."+bRoom+".boss.type", value);
                        bossRoomsConfig.saveConfig();
                        dataManager.setBRoomsBosse(bRoom, value);
                        msg.send(p, "a", "Boss type successfully set for room " + bRoom);
                    }else{
                        msg.send(p, "5", "Boss " + value + " is not valid!");
                    }
                }else if(option.equalsIgnoreCase("idletimeout")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms." + bRoom + ".settings.idletimeout", Integer.parseInt(value));
                    settingsCache.put("idletimeout", value);
                    msg.send(p, "a", "Idle timeout set for room " + bRoom);
                }else if(option.equalsIgnoreCase("maxplayers")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms." + bRoom + ".settings.maxplayers", Integer.parseInt(value));
                    settingsCache.put("maxplayers", value);
                    msg.send(p, "a", "Max players limit set for room " + bRoom);
                }else if(option.equalsIgnoreCase("minplayers")){
                    if(!tools.isInteger(value)){ msg.send(p, "5", "Input must be an integer!"); return;}
                    bossRoomsConfig.set("rooms." + bRoom + ".settings.minplayers", Integer.parseInt(value));
                    settingsCache.put("minplayers", value);
                    msg.send(p, "a", "Min players limit set for room " + bRoom);
                }
                bossRoomsConfig.saveConfig();
                dataManager.setBRoomsSettings(bRoom, settingsCache);
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
                    msg.send(p, "a", "Room " + bRoom + " enabled!");
                    //TODO fix locations enable error
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
            p.openInventory(invBuyMain);
        }else{
            //TODO add boss room purchase features both command and gui
        }
    }

//    public void commandJoin(Player p, String bRoom){
//        if(bRoomManager.getBRoom(p)==null){
//
//        }else{
//            msg.send(p, "c", "You are already in a Boss Room!");
//        }
//    }
//
//    public void commandQuit(Player p, String bRoom){
//
//    }


}
