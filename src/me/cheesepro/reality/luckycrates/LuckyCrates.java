package me.cheesepro.reality.luckycrates;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.eventhandlers.PlayerOpenCrateEvent;
import me.cheesepro.reality.utils.DataManager;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.PlayerManager;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-07-07.
 */
public class LuckyCrates implements Listener {

    private Reality plugin;
    private Messenger msg;
    private Tools tools;
    private List<String> cratesItems;
    private Map<String, Map<String, Integer>> cratesLocations;
    private String cratesWorld;
    private ItemStack crateKey;
    private Inventory inv;
    private DataManager dataManager;
    private PlayerManager pManager;

    public LuckyCrates(Reality plugin) {
        this.plugin = plugin;
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        cratesItems = plugin.getCratesItems();
        cratesLocations = plugin.getCratesLocations();
        cratesWorld = plugin.getCratesWorld();
        crateKey = plugin.getCrateKey();
        dataManager = new DataManager(plugin);
        pManager = new PlayerManager(plugin);
        inv = Bukkit.getServer().createInventory(null, 54, ChatColor.GOLD.toString() + ChatColor.BOLD + "Lucky Crate");
        int value = 0;
        for (String itemInfo : cratesItems) {
            value++;
            String[] itemInfoList = itemInfo.split(":");
            String material = itemInfoList[0];
            Integer percentage = Integer.parseInt(itemInfoList[1]);
            Integer amount = Integer.parseInt(itemInfoList[2]);
            ItemStack item = new ItemStack(Material.getMaterial(material), amount);
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.GREEN + "Percentage: " + percentage + "%");
            item.setItemMeta(im);
            inv.setItem(8 + value, item);
            for (int i = 0; i < 9; i++) {
                ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData());
                ItemMeta glassim = glass.getItemMeta();
                glassim.setDisplayName("-");
                glass.setItemMeta(glassim);
                inv.setItem(i, glass);
            }
            for (int i = 36; i < 45; i++) {
                ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData());
                ItemMeta glassim = glass.getItemMeta();
                glassim.setDisplayName("-");
                glass.setItemMeta(glassim);
                inv.setItem(i, glass);
            }
            ItemStack spin = new ItemStack(Material.DOUBLE_PLANT);
            ItemMeta spinim = spin.getItemMeta();
            spinim.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "SPIN");
            spin.setItemMeta(spinim);
            inv.setItem(49, spin);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClickCrates(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_AIR) {
            int x = e.getClickedBlock().getLocation().getBlockX();
            int y = e.getClickedBlock().getLocation().getBlockY();
            int z = e.getClickedBlock().getLocation().getBlockZ();
            if (e.getClickedBlock().getType() == Material.BEACON && tools.validateCrate(x, y, z)) {
                if (e.getMaterial() == crateKey.getType() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(crateKey.getItemMeta().getDisplayName())) {
                    Player p = e.getPlayer();
                    e.setCancelled(true);
                    p.openInventory(inv);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerOpenCrateEvent(p));
                } else {
                    msg.send(e.getPlayer(), "d", "You are too weak to open this crate. Come back to me when you acquire the Key.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().getName().equalsIgnoreCase(inv.getName())) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;
        if (e.getClick() == ClickType.UNKNOWN) return;
        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "SPIN")) {
            e.setCancelled(true);
            Collections.shuffle(cratesItems);
            int got = 0;
            for (String itemInfo : cratesItems) {
                String[] itemInfoList = itemInfo.split(":");
                Integer percentage = Integer.parseInt(itemInfoList[1]);
                if (tools.randInt(1, 100) < percentage) {
                    String material = itemInfoList[0];
                    Integer amount = Integer.parseInt(itemInfoList[2]);
                    ItemStack item = new ItemStack(Material.getMaterial(material), amount);
                    e.getWhoClicked().getInventory().addItem(item);
                    HashMap<Integer, ItemStack> nope = e.getWhoClicked().getInventory().addItem(item);
                    for(Map.Entry<Integer, ItemStack> entry : nope.entrySet())
                    {
                        msg.send((Player)e.getWhoClicked(), "d", "Your inventory is full, placing your reward(s) on the ground!");
                        e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), entry.getValue());
                    }
                    if (got == 1) {
                        msg.send((Player) e.getWhoClicked(), "e", "and " + amount + " " + material.toLowerCase().replace("_", " ") + "!");
                    } else {
                        msg.send((Player) e.getWhoClicked(), "e", "You got " + amount + " " + material.toLowerCase().replace("_", " ") + "!");
                    }
                    got = 1;
                }
            }
            if (got == 0) {
                dataManager.depositPlayer((Player) e.getWhoClicked(), 5000.0);
                pManager.addXP(e.getWhoClicked().getUniqueId(), 5000);
                msg.send((Player) e.getWhoClicked(), "e", "You got $5000 and 5000XP");
            }
            if (e.getWhoClicked().getInventory().getItemInHand().getAmount() == 1) {
                e.getWhoClicked().getInventory().remove(e.getWhoClicked().getInventory().getItemInHand());
            } else {
                e.getWhoClicked().getInventory().getItemInHand().setAmount(e.getWhoClicked().getInventory().getItemInHand().getAmount() - 1);
            }
            e.getWhoClicked().closeInventory();
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void KeyClonePreventer(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(p.getGameMode() == GameMode.CREATIVE){
            if (e.getCursor().getType() == crateKey.getType() && e.getCursor().getItemMeta().getDisplayName().equalsIgnoreCase(crateKey.getItemMeta().getDisplayName())) {
                if (e.getCursor().getAmount() == e.getCursor().getMaxStackSize()) {
                    e.setCancelled(true);
                    msg.send(p, "4", "HEY! What are you doing?! Please do not attempt to clone crate keys!!!");
                }
            }
        }
    }

}
