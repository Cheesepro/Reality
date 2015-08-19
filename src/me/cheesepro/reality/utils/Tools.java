package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.abilities.AbilitiesINFO;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-11.
 */
public class Tools {

    private Reality plugin;
    private Map<UUID, Map<String, String>> playersINFO;
    private AbilitiesINFO abilitiesINFO;
    private Map<String, Map<String, Integer>> cratesLocations;
    private DataManager dataManager;

    public Tools(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        playersINFO = plugin.getPlayersINFO();
        abilitiesINFO = new AbilitiesINFO(plugin);
        cratesLocations = plugin.getCratesLocations();
    }

    public boolean isHoldingCorrectItem(Player p, String itemName){
        List<String> kitsCache = abilitiesINFO.getRequiredItems(playersINFO.get(p.getUniqueId()).get("rank"));
        for(String kitCache : kitsCache){
            if(kitCache.startsWith(itemName)){
                String[] splits = kitCache.split("#");
                String name = splits[1];
                if(p.getItemInHand().getItemMeta().getDisplayName()!=null && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
                    return true;
                }
            }
        }
        return false;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public boolean validateCrate(int x, int y, int z){
        for(String crateName : cratesLocations.keySet()){
            if(cratesLocations.get(crateName).get("x")==x){
                if(cratesLocations.get(crateName).get("y")==y){
                    if(cratesLocations.get(crateName).get("z")==z){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInteger(String in){
        try{
            int test = Integer.parseInt(in);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public boolean canUseAbility(String rank, String ability){
        if(dataManager.containsRanksAbilities(rank)){
            if(dataManager.getRanksAbilities(rank).contains(ability)){
                return true;
            }
        }
        return false;
    }

}
