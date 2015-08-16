package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.abilities.AbilitiesINFO;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

/**
 * Created by Mark on 2015-05-10.
 */
public class RankGiver {

    Reality plugin;
    Map<String, List<String>> settings;
    Map<String, Map<String, List<String>>> ranks;
    Map<String, String> messages;
    Map<Integer, String> randomranks = new HashMap<Integer, String>();
    List<String> allowranks = new ArrayList<String>();
    Map<UUID, Map<String, String>> playersINFO;
    Messenger msg;
    AbilitiesINFO abilitiesINFO;
    Config storageConfig;
    Tools tools;

    public RankGiver(Reality plugin){
        this.plugin = plugin;
        settings = plugin.getSettings();
        ranks = plugin.getRanks();
        messages = plugin.getMessages();
        msg = new Messenger(plugin);
        abilitiesINFO = new AbilitiesINFO(plugin);
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        tools = new Tools(plugin);
    }

    public void giveRank(Player p){
        allowranks.clear();
        randomranks.clear();
        for (PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("reality.rank.")) {
                allowranks.add(perm.getPermission().replace("reality.rank.", ""));
            }
        }
        int randomrankscount = 0;
        for(String rank : allowranks){
            randomrankscount++;
            randomranks.put(randomrankscount, rank);
        }
        String rank = randomranks.get(tools.randInt(1, randomrankscount));
        List<String> usedatabase = settings.get("use-database");
        if(Boolean.parseBoolean(usedatabase.get(0))){
            //TODO add database storage feature
        }else{
            storageConfig.set("players."+p.getUniqueId().toString()+".rank", rank);
            storageConfig.saveConfig();
            Map<String, String> cache = playersINFO.get(p.getUniqueId());
            cache.put("rank", rank);
            playersINFO.put(p.getUniqueId(), cache);
        }
        Map<String, List<String>> mapCache = ranks.get(rank);
        List<String> listCache;
        if(mapCache.containsKey("health")){
            listCache = mapCache.get("health");
            p.setMaxHealth(Double.parseDouble(listCache.get(0)));
        }
        if(mapCache.containsKey("starting-kit")){
            listCache = mapCache.get("starting-kit");
            for(String itemInfoCache : listCache){
                String[] splits = itemInfoCache.split("#");
                String itemCache = splits[0];
                int itemAmountCache = Integer.parseInt(splits[1]);
                ItemStack item = new ItemStack(Material.getMaterial(itemCache.toUpperCase()), itemAmountCache);
                ItemMeta im = item.getItemMeta();
                String itemName = im.getDisplayName();
                if(splits[2]!=null){
                    itemName = splits[2];
                }
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&',itemName));
                item.setItemMeta(im);
                p.getInventory().addItem(item);
            }
        }
        if(mapCache.containsKey("abilities")){
            List<String> items = abilitiesINFO.getRequiredItems(rank);
            List<String> names = abilitiesINFO.getNames(rank);
            List<String> descs = abilitiesINFO.getDescs(rank);
            msg.send(p, "d", "You now have the following abilities");
            for(int i = 0; i<names.size(); i++){
                msg.send(p, "*", ChatColor.GREEN + names.get(i) + ChatColor.LIGHT_PURPLE + ": " + ChatColor.YELLOW + descs.get(i));
            }
            for(String itemInfo : items){
                String[] splits = itemInfo.split("#");
                String itemCache = splits[0];
                ItemStack item = new ItemStack(Material.getMaterial(itemCache.toUpperCase()), 1);
                ItemMeta im = item.getItemMeta();
                String itemName = im.getDisplayName();
                if(splits[1]!=null){
                    itemName = splits[1];
                }
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&',itemName));
                item.setItemMeta(im);
                p.getInventory().addItem(item);
            }
        }
        randomranks.clear();
        allowranks.clear();
    }

}
