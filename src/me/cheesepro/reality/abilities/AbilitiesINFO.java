package me.cheesepro.reality.abilities;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-04-05.
 */
public class AbilitiesINFO {

    private Reality plugin;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Abilities[] abilities;
    private DataManager dataManager;

    public AbilitiesINFO(Reality plugin) {
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        abilities = plugin.getAbilities();
        abilitiesOptions = plugin.getAbilitiesOptions();
    }

    public List<String> getNames(String rank) {
//        Map<String, List<String>> mapCache = ranks.get(rank);
//        List<String> listCache = mapCache.get("abilities");
        if (dataManager.containsRanksAbilities(rank)) {
            List<String> names = new ArrayList<String>();
            for (Abilities abilitiesCache : abilities) {
                if (dataManager.getRanksAbilities(rank).contains(abilitiesCache.getName().toUpperCase())) {
                    names.add(abilitiesCache.getName());
                }
            }
            return names;
        }
        return null;
    }

    public List<String> getDescs(String rank) {
//        Map<String, List<String>> mapCache = ranks.get(rank);
//        List<String> listCache = mapCache.get("abilities");
        if (dataManager.containsRanksAbilities(rank)) {
            List<String> descs = new ArrayList<String>();
            for (Abilities abilitiesCache : abilities) {
                if (dataManager.getRanksAbilities(rank).contains(abilitiesCache.getName().toUpperCase())){
                    descs.add(abilitiesCache.getDesc());
                }
            }
            return descs;
        }
        return null;
    }

    public List<String> getRequiredItems(String rank) {
//        List<String> abilities = ranks.get(rank).get("abilities");
        if(dataManager.containsRanksAbilities(rank)){
            List<String> items = new ArrayList<String>();
            for(String ability : dataManager.getRanksAbilities(rank)){
                if(abilitiesOptions.get(ability).get("item_name")!=null){
                    items.add(abilitiesOptions.get(ability).get("item")+
                            "#"+ abilitiesOptions.get(ability).get("item_name"));
                }
            }
            return items;
        }
        return null;
    }


}
