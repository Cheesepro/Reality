package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.bossrooms.BossesAPI;
import me.cheesepro.reality.bossrooms.rooms.BRoomManager;
import me.cheesepro.reality.eventhandlers.BRoomUpdateEvent;
import me.cheesepro.reality.utils.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2015-08-04.
 */
public class BRoomUpdateListener implements Listener{

    private Reality plugin = Reality.getPlugin();
    private static Inventory inv;
    private DataManager dataManager = new DataManager(plugin);
    private BRoomManager bRoomManager = new BRoomManager(plugin);
    private BossesAPI bossesAPI = new BossesAPI(plugin);

    public BRoomUpdateListener(){
        if(inv==null){
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            inv = Bukkit.getServer().createInventory(null, 54, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Boss room shop!");
            int slot = 0;
            for (String bRoom : dataManager.getBRooms()) {
                String bossType = bRoomManager.getBRoom(bRoom).getBossType();
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner("MHF_" + bRoomManager.getBRoom(bRoom).getBossType());
                meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "---------" + ChatColor.YELLOW + "[" + ChatColor.BLUE + "INFO" + ChatColor.YELLOW.toString() + "]" + ChatColor.STRIKETHROUGH + "---------");
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD.toString() + "Room name: " + ChatColor.LIGHT_PURPLE + bRoom);
                lore.add(ChatColor.GOLD.toString() + "Boss: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getName());
                lore.add(ChatColor.GOLD.toString() + "Boss Health: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getHealth());
                lore.add(ChatColor.GOLD.toString() + "Boss Damage: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getDamage());
                lore.add(ChatColor.GOLD.toString() + "Room Status: " + ChatColor.LIGHT_PURPLE + bRoomManager.getBRoom(bRoom).getState());
                lore.add(ChatColor.GOLD.toString() + "Slots: " + ChatColor.LIGHT_PURPLE + bRoomManager.getBRoom(bRoom).getCurrentPlayers() + "/" + bRoomManager.getBRoom(bRoom).getMaxPlayer());
                lore.add(ChatColor.GOLD.toString() + "Cost: " + ChatColor.LIGHT_PURPLE + "$" + bossesAPI.getBoss(bossType).getRewardMoney() * 3);
                lore.add(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "--------" + ChatColor.YELLOW + "[" + ChatColor.RED + "Rewards" + ChatColor.YELLOW.toString() + "]" + ChatColor.STRIKETHROUGH + "-------");
                lore.add(ChatColor.GREEN + "Lucky Crate Key(s): " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardKey());
                lore.add(ChatColor.GREEN + "Money: " + ChatColor.AQUA + "$" + bossesAPI.getBoss(bossType).getRewardMoney());
                lore.add(ChatColor.GREEN + "XP: " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardXP());
                lore.add(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "------------------------");
                meta.setLore(lore);
                skull.setItemMeta(meta);
                inv.setItem(slot, skull);
                slot++;
            }
        }
    }

    @EventHandler
    public void onBRoomUpdate(BRoomUpdateEvent e) {
        inv.clear();
        int slot = 0;
        for (String bRoom : dataManager.getBRooms()) {
            if(bRoomManager.getBRoom(bRoom)!=null && bRoomManager.getBRoom(bRoom).getBossType()!=null){
                String bossType = bRoomManager.getBRoom(bRoom).getBossType();
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner("MHF_" + bRoomManager.getBRoom(bRoom).getBossType());
                meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "---------" + ChatColor.YELLOW + "[" + ChatColor.BLUE + "INFO" + ChatColor.YELLOW.toString() + "]" + ChatColor.STRIKETHROUGH + "---------");
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD.toString() + "Room name: " + ChatColor.LIGHT_PURPLE + bRoom);
                lore.add(ChatColor.GOLD.toString() + "Boss: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getName());
                lore.add(ChatColor.GOLD.toString() + "Boss Health: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getHealth());
                lore.add(ChatColor.GOLD.toString() + "Boss Damage: " + ChatColor.LIGHT_PURPLE + bossesAPI.getBoss(bossType).getDamage());
                lore.add(ChatColor.GOLD.toString() + "Room Status: " + ChatColor.LIGHT_PURPLE + bRoomManager.getBRoom(bRoom).getState());
                lore.add(ChatColor.GOLD.toString() + "Slots: " + ChatColor.LIGHT_PURPLE + bRoomManager.getBRoom(bRoom).getCurrentPlayers() + "/" + bRoomManager.getBRoom(bRoom).getMaxPlayer());
                lore.add(ChatColor.GOLD.toString() + "Cost: " + ChatColor.LIGHT_PURPLE + "$" + bossesAPI.getBoss(bossType).getRewardMoney() * 3);
                lore.add(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "--------" + ChatColor.YELLOW + "[" + ChatColor.RED + "Rewards" + ChatColor.YELLOW.toString() + "]" + ChatColor.STRIKETHROUGH + "-------");
                lore.add(ChatColor.GREEN + "Lucky Crate Key(s): " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardKey());
                lore.add(ChatColor.GREEN + "Money: " + ChatColor.AQUA + "$" + bossesAPI.getBoss(bossType).getRewardMoney());
                lore.add(ChatColor.GREEN + "XP: " + ChatColor.AQUA + bossesAPI.getBoss(bossType).getRewardXP());
                lore.add(ChatColor.YELLOW.toString() + ChatColor.STRIKETHROUGH + "------------------------");
                meta.setLore(lore);
                skull.setItemMeta(meta);
                inv.setItem(slot, skull);
                slot++;
            }
        }
    }

    public Inventory getInv(){
        return inv;
    }
}
