package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.abilities.AbilitiesINFO;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mark on 2015-04-11.
 */
public class Tools {

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, Map<String, List<String>>> ranks;
    AbilitiesINFO abilitiesINFO;
    Map<String, Map<String, Integer>> cratesLocations;

    public Tools(Reality plugin){
        this.plugin = plugin;
        ranks = plugin.getRanks();
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
                if(p.getItemInHand().getItemMeta().getDisplayName()!=null && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
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

}
