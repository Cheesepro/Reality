package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Mark on 2015-07-27.
 */
public class DataManager {

    private Reality plugin;
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
    private List<String> bossesTypes;
    private Map<String, Boolean> bRoomsEnabled;

    private Config storageConfig;
    private Config cratesConfig;
    private Config bossRoomsConfig;


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

        storageConfig = plugin.getStorageConfig();
        cratesConfig = plugin.getCratesConfig();
        bossRoomsConfig = plugin.getBossRoomsConfig();
    }

    public Boolean isBRoomEnabled(String bRoom){
        if(bRoomsEnabled.get(bRoom)){
            return true;
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
        bossRoomsConfig.set(bRoom, null);
        bossRoomsConfig.saveConfig();
    }

    public Boolean isBossValid(String boss){
        return bossesTypes.contains(boss);
    }

    public Boolean isBRoomValid(String bRoom){
        return bRoomsBosses.keySet().contains(bRoom);
    }

    public void setBRoomsBosses(String key, String value){
        bRoomsBosses.put(key, value);
    }

    public Map<String, String> getBRoomsSettings(String key){
        return bRoomsSettings.get(key);
    }

    public void setBRoomsSettings(String key, Map<String, String> map){
        bRoomsSettings.put(key, map);
    }

    public Map<String, Map<String, Double>> getBRoomsLocations(String key){
        return bRoomsLocations.get(key);
    }

    public void setBRoomsLocations(String key, Map<String, Map<String, Double>> map){
        bRoomsLocations.put(key, map);
    }

    public Map<String, Double> getBRoomsBossesLocations(String key){
        return bRoomsBossesLocations.get(key);
    }

    public void setbRoomsBossesLocations(String key, Map<String, Double> map){
        bRoomsBossesLocations.put(key, map);
    }

}
