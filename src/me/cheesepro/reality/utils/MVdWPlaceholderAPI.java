package me.cheesepro.reality.utils;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.cheesepro.reality.Reality;

/**
 * Created by Mark on 2015-09-14.
 */
public class MVdWPlaceholderAPI{

    private static Reality plugin = Reality.getPlugin();
    private static PlayerManager playerManager = new PlayerManager(plugin);
    private static DataManager dataManager = new DataManager(plugin);

    public static void updateAllPlaceHolders(){
        updateLevelPH();
        updateRankPH();
        updateXPPH();
    }

    public static void updateLevelPH(){
        PlaceholderAPI.registerPlaceholder(plugin, "reality_level", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                return playerManager.getLevel(e.getPlayer().getUniqueId()) + "";
            }
        });
    }

    public static void updateRankPH(){
        PlaceholderAPI.registerPlaceholder(plugin, "reality_rank", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                return dataManager.getRanksName(playerManager.getRank(e.getPlayer().getUniqueId())) + "";
            }
        });
    }

    public static void updateXPPH(){
        PlaceholderAPI.registerPlaceholder(plugin, "reality_xp", new PlaceholderReplacer() {
            @Override
            public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                return playerManager.getXP(e.getPlayer().getUniqueId()) + "";
            }
        });
    }

}
