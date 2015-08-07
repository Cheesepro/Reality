package me.cheesepro.reality.bossrooms;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Mark on 2015-08-06.
 */
public class BRoomShopInvListener implements Listener{

    private Reality plugin;
    private DataManager dataManager;

    public BRoomShopInvListener(Reality plugin){
        this.plugin = plugin;
        dataManager = new DataManager(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onShopInvClick(InventoryClickEvent e){
        if (!e.getInventory().getName().equalsIgnoreCase(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Boss room shop!")) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;
        if (e.getClick() == ClickType.UNKNOWN) return;
        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "---------" + ChatColor.YELLOW + "[" + ChatColor.BLUE + "INFO" + ChatColor.YELLOW.toString() + "]" + ChatColor.STRIKETHROUGH + "---------")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            p.performCommand("reality bossroom buy " + e.getCurrentItem().getItemMeta().getLore().get(0).replace(ChatColor.GOLD.toString() + "Room name: " + ChatColor.LIGHT_PURPLE, ""));
            p.closeInventory();
        }
    }

}
