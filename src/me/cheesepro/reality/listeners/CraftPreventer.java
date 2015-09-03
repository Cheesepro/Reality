package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-09-02.
 */
public class CraftPreventer implements Listener {

    private Reality plugin;
    private Map<String, Map<String, String>> abilitiesOptions;
    private Tools tools;
    private Map<UUID, Map<String, String>> playersINFO;
    private DataManager dataManager;
    private Messenger msg;

    public CraftPreventer(Reality plugin){
        this.plugin = plugin;
        abilitiesOptions = plugin.getAbilitiesOptions();
        playersINFO = plugin.getPlayersINFO();
        tools = new Tools(plugin);
        dataManager = new DataManager(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void noRename(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();
        if (!(e.getInventory() instanceof AnvilInventory) && !(e.getInventory() instanceof CraftingInventory)) {
            return;
        }
        if (e.getSlotType() != InventoryType.SlotType.CRAFTING) {
            return;
        }
        if(dataManager.containsRanksAbilities(playersINFO.get(p.getUniqueId()).get("rank"))) {
            List<String> listCache = dataManager.getRanksAbilities(playersINFO.get(p.getUniqueId()).get("rank"));
            for(String ability : listCache){
                if(abilitiesOptions.get(ability).get("item")!=null){
                    if(e.getCursor().getItemMeta()!=null){
                        if(dataManager.getAbilitiesItemNames().contains(e.getCursor().getItemMeta().getDisplayName())){
                            e.setCancelled(true);
                            msg.send(p, "c", "Hey! You don't want to loose your ability right?");
                            break;
                        }
                    }
                }
            }
        }
        if(e.getCursor().getItemMeta()!=null){
            if(e.getCursor().getItemMeta().toString().equalsIgnoreCase(dataManager.getCrateKey().getItemMeta().toString())){
                e.setCancelled(true);
                msg.send(p, ChatColor.GOLD, "Trust me, you don't want to loose your crate key....");
            }
        }
    }
}
