package me.cheesepro.reality.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BRoomIdle;
import me.cheesepro.reality.bossrooms.Bosses;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Mark on 2015-07-27.
 */
public class DataManager {

    private Reality plugin = Reality.getPlugin();
    private Map<String, List<String>> settings;
    private Map<String, Map<String, List<String>>> ranks;
    private Map<String, Integer> levels;
    private Map<String, NavigableSet<String>> levelLimits;
    private NavigableSet<String> blockedItems;
    private NavigableSet<Integer> levelsSet;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Map<String, String> messages;
    private Map<UUID, Map<String, String>> playersINFO;
    private List<String> cratesItems;
    private Map<String, Map<String, Integer>> cratesLocations;
    private String cratesWorld;
    private ItemStack crateKey;
    private Map<String, String> bRoomsBosses;
    private Map<String, Map<String, Double>> bRoomsBossesLocations;
    private Map<String, Map<String, Map<String, Double>>> bRoomsLocations;
    private Map<String, Map<String, String>> bRoomsSettings;
    private String bossesWorld;
    private Set<String> bossesTypes;
    private Map<String, Boolean> bRoomsEnabled;
    private Map<UUID, Boolean> bRoomPlayersRole;
    private Map<UUID, String> bRoomPlayersRoom;
    private Map<String, Integer> bRoomWinCount;
    private BRoomIdle bRoomIdle;

    private Bosses[] bosses;

    private WorldGuardPlugin worldGuard;

    private Config storageConfig;
    private Config cratesConfig;
    private Config bossRoomsConfig;

    private Economy economy = Reality.getEconomy();

    public DataManager(Reality plugin){
        this.plugin = plugin;
        abilitiesOptions = plugin.getAbilitiesOptions();
        blockedItems = plugin.getBlockedItems();
        bossesTypes = plugin.getBossesTypes();
        bossesWorld = plugin.getBossesWorld();
        bRoomsEnabled = plugin.getbRoomsEnabled();
        bRoomsBosses = plugin.getbRoomsBosses();
        bRoomsBossesLocations = plugin.getbRoomsBossesLocations();
        bRoomsLocations = plugin.getbRoomsLocations();
        bRoomsSettings = plugin.getbRoomsSettings();
        crateKey = plugin.getCrateKey();
        cratesItems = plugin.getCratesItems();
        cratesLocations = plugin.getCratesLocations();
        cratesWorld = plugin.getCratesWorld();
        levelLimits = plugin.getLevelLimits();
        levels = plugin.getLevels();
        levelsSet = plugin.getLevelsSet();
        messages = plugin.getMessages();
        playersINFO = plugin.getPlayersINFO();
        ranks = plugin.getRanks();
        settings = plugin.getSettings();
        bRoomPlayersRole = plugin.getbRoomPlayersRole();
        bRoomPlayersRoom = plugin.getbRoomPlayersRoom();
        bRoomWinCount = plugin.getbRoomWinCount();
        worldGuard = plugin.getWorldGuard();
        bosses = plugin.getBosses();

        storageConfig = plugin.getStorageConfig();
        cratesConfig = plugin.getCratesConfig();
        bossRoomsConfig = plugin.getBossRoomsConfig();
        bRoomIdle = plugin.getbRoomIdle();
    }

    public String getBossesWorld(){
        return bossesWorld;
    }

    public Boolean isBRoomEnabled(String bRoom){
        if(bRoomsEnabled!=null && !bRoomsEnabled.toString().equalsIgnoreCase("{}")){
            if(bRoomsEnabled.containsKey(bRoom)){
                if(bRoomsEnabled.get(bRoom)){
                    return true;
                }
            }
        }
        return false;
    }

    public void setBRoomEnabled(String bRoom, Boolean value){
        bRoomsEnabled.put(bRoom, value);
    }

    public void removeBRoom(String bRoom){
        bRoomsEnabled.remove(bRoom);
        bRoomsBosses.remove(bRoom);
        bRoomsBossesLocations.remove(bRoom);
        bRoomsLocations.remove(bRoom);
        bRoomsSettings.remove(bRoom);
        bossRoomsConfig.set("rooms."+bRoom, null);
        bossRoomsConfig.saveConfig();
    }

    public Boolean isBossValid(String boss) {
        for (Bosses bossType : bosses) {
            if(bossType.getType().equalsIgnoreCase(boss)){
                return true;
            }
        }
        return false;
    }

    public Set<String> getBossesTypes(){
        return bossesTypes;
    }

    public Boolean isBRoomValid(String bRoom){
        return worldGuard.getRegionManager(Bukkit.getWorld(bossesWorld)).hasRegion("reality_bossroom_"+bRoom);
    }

    public String getBRoomBoss(String key){
        return bRoomsBosses.get(key);
    }

    public void setBRoomsBosse(String key, String value){
        bRoomsBosses.put(key, value);
    }

    public Set<String> getBRooms(){
        return bRoomsEnabled.keySet();
    }

    public Map<String, String> getBRoomsSettings(String key){
        return bRoomsSettings.get(key);
    }

    public Boolean isBRoomsSettingsValid(String key){
        return bRoomsSettings.containsKey(key);
    }

    public void setBRoomsSettings(String key, Map<String, String> map){
        bRoomsSettings.put(key, map);
    }

    public Map<String, Map<String, Double>> getBRoomsLocations(String key){
        return bRoomsLocations.get(key);
    }

    public Boolean isBRoomsLocationsValid(String key){
        return bRoomsLocations.containsKey(key);
    }

    public void setBRoomsLocations(String key, Map<String, Map<String, Double>> map){
        bRoomsLocations.put(key, map);
    }

    public Map<String, Double> getBRoomsBossesLocations(String key){
        return bRoomsBossesLocations.get(key);
    }

    public Boolean isBRoomsBossesLocationsValid(String key){
        return bRoomsBossesLocations.containsKey(key);
    }

    public Boolean getBRoomPlayersRole(UUID id){
        return bRoomPlayersRole.get(id);
    }

    public void setbRoomPlayersRole(UUID id, Boolean role){
        bRoomPlayersRole.put(id, role);
    }

    public void removePlayerRole(UUID id){
        bRoomPlayersRole.remove(id);
    }

    public Set<UUID> getInGamePlayersList(){
        return bRoomPlayersRoom.keySet();
    }

    public String getBRoomPlayersRoom(UUID id){
        return bRoomPlayersRoom.get(id);
    }

    public void removePlayersRoom(UUID id){
        bRoomPlayersRoom.remove(id);
    }

    public void setbRoomPlayersRoom(UUID id, String room){
        bRoomPlayersRoom.put(id, room);
    }

    public Integer getBRoomWinCount(String bRoom){
        return bRoomWinCount.get(bRoom);
    }

    public void addBRoomWinCount(String bRoom, int value){
        if(bRoomWinCount.get(bRoom)!=null){
            bRoomWinCount.put(bRoom, bRoomWinCount.get(bRoom)+value);
        }else{
            bRoomWinCount.put(bRoom, value);
        }
    }

    public void resetBRoomWinCount(String bRoom){
        bRoomWinCount.remove(bRoom);
    }

    public ItemStack getCrateKey(){
        return crateKey;
    }

    public void depositPlayer(Player p, Double amount){
        Reality.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), amount);
    }

    public void chargePlayer(Player p, Double amount){
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), amount);
    }

    public Boolean playerHasEnoughMoney(Player p, Double amount){
        return economy.getBalance(Bukkit.getOfflinePlayer(p.getUniqueId()))>=amount;
    }

    public BRoomIdle getbRoomIdle(){
        return bRoomIdle;
    }

    public List<String> getAbilitiesItemNames(){
        List<String> temp = new ArrayList<String>();
        for(String item : abilitiesOptions.keySet()){
            if(abilitiesOptions.get(item).get("item_name")!=null){
                temp.add(abilitiesOptions.get(item).get("item_name"));
            }
        }
        return temp;
    }

}
