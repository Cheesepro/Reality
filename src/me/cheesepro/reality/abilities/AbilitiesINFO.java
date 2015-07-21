package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.abilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-04-05.
 */
public class AbilitiesINFO {

    Reality plugin;
    Map<String, Map<String, List<String>>> ranks;
    Map<String, Map<String, String>> abilitiesOptions;
    Abilities[] abilities;

    public AbilitiesINFO(Reality plugin) {
        this.plugin = plugin;
        ranks = plugin.getRanks();
        abilities = plugin.getAbilities();
        abilitiesOptions = plugin.getAbilitiesOptions();
    }

    public List<String> getNames(String rank) {
        List<String> names = new ArrayList<String>();
        Map<String, List<String>> mapCache = ranks.get(rank);
        List<String> listCache = mapCache.get("abilities");
        for (Abilities abilitiesCache : abilities) {
            if (listCache.contains(abilitiesCache.getName().toUpperCase())) {
                names.add(abilitiesCache.getName());
            }
        }
        return names;
    }

    public List<String> getDescs(String rank) {
        List<String> descs = new ArrayList<String>();
        Map<String, List<String>> mapCache = ranks.get(rank);
        List<String> listCache = mapCache.get("abilities");
        for (Abilities abilitiesCache : abilities) {
            if (listCache.contains(abilitiesCache.getName().toUpperCase())) {
                descs.add(abilitiesCache.getDesc());
            }
        }
        return descs;
    }

    public List<String> getRequiredItems(String rank) {
        List<String> items = new ArrayList<String>();
        List<String> abilities = ranks.get(rank).get("abilities");
        for(String ability : abilities){
            if(abilitiesOptions.get(ability).get("item_name")!=null){
                items.add(abilitiesOptions.get(ability).get("item")+
                        "#"+ abilitiesOptions.get(ability).get("item_name"));
            }
        }
        return items;
    }


}
