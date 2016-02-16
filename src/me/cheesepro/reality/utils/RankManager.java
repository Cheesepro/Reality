package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.abilities.AbilitiesINFO;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by Mark on 2015-05-10.
 */
public class RankManager {

    private Reality plugin;
    private Map<String, List<String>> settings;
    private Map<String, String> messages;
    private List<String> randomranks = new ArrayList<String>();
    private List<String> allowranks = new ArrayList<String>();
    private Map<UUID, Map<String, String>> playersINFO;
    private Messenger msg;
    private AbilitiesINFO abilitiesINFO;
    private Config storageConfig;
    private Tools tools;
    private DataManager dataManager;

    public RankManager(Reality plugin){
        this.plugin = plugin;
        settings = plugin.getSettings();
        messages = plugin.getMessages();
        msg = new Messenger(plugin);
        abilitiesINFO = new AbilitiesINFO(plugin);
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
    }

    public void giveRank(final Player p){
        p.setGameMode(GameMode.SURVIVAL);
        allowranks.clear();
        randomranks.clear();
        for (PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("reality.rank.")) {
                allowranks.add(perm.getPermission().replace("reality.rank.", ""));
            }
        }
        int randomrankscount = -1;
        for(String rank : allowranks){
            randomrankscount++;
            randomranks.add(rank);
        }
        if(randomrankscount==-1){
            msg.send(p, "c", "Sorry, currently there are no ranks for you!");
            clearRank(p);
            return;
        }
        String rank = randomranks.get(tools.randInt(0, randomrankscount));
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
        if(dataManager.containsRanksHealth(rank)){
            p.setMaxHealth(dataManager.getRanksHealth(rank));
            p.setHealth(dataManager.getRanksHealth(rank));
        }
        if(dataManager.containsRanksKit(rank)){
            for(String itemInfoCache : dataManager.getRanksKit(rank)){
                String[] splits = itemInfoCache.split("#");
                String itemCache = splits[0];
                int itemAmountCache = Integer.parseInt(splits[1]);
                ItemStack item = new ItemStack(Material.getMaterial(itemCache.toUpperCase()), itemAmountCache);
                ItemMeta im = item.getItemMeta();
                String itemName = im.getDisplayName();
                if(splits.length==3){
                    itemName = splits[2];
                }else if (splits.length==4){
                    itemName = splits[2];
                    item.setDurability(Short.parseShort(splits[3]));
                }
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&',itemName));
                item.setItemMeta(im);
                p.getInventory().addItem(item);
            }
        }
        if(dataManager.containsRanksAbilities(rank)){
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
        GraphicalAPI.sendTitleToPlayer(p, 1, 5, 0, ChatColor.YELLOW + "Reborn as", dataManager.getRanksName(rank));
        new BukkitRunnable(){
            @Override
            public void run(){
                GraphicalAPI.sendTitleToPlayer(p, 0, 5, 1, ChatColor.RED + "Please", ChatColor.GOLD + "Cherish your life");
            }
        }.runTaskLater(plugin, 6 * 20);
        randomranks.clear();
        allowranks.clear();
        MVdWPlaceholderAPI.updateRankPH();
    }

    public void clearRank(Player p){
        storageConfig.set("players." + p.getUniqueId().toString() + ".rank", null);
        storageConfig.saveConfig();
        Map<String, String> cache = playersINFO.get(p.getUniqueId());
        cache.remove("rank");
        playersINFO.put(p.getUniqueId(), cache);
        MVdWPlaceholderAPI.updateRankPH();
    }
}
