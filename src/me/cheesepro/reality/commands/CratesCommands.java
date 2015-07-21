package me.cheesepro.reality.commands;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Config;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-07-07.
 */
public class CratesCommands{

    Reality plugin;
    Messenger msg;
    Tools tools;
    List<String> cratesItems;
    Map<String, Map<String, Integer>> cratesLocations;
    Config cratesConfig;
    String cratesWorld;

    public CratesCommands(Reality plugin) {
        this.plugin = plugin;
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        cratesItems = plugin.getCratesItems();
        cratesLocations = plugin.getCratesLocations();
        cratesConfig = plugin.getCratesConfig();
        cratesWorld = plugin.getCratesWorld();
    }


    public void commandSetCrate(Player p, String crateName){
        if(!cratesLocations.keySet().contains(crateName)){
            if(p.getWorld().getName().equalsIgnoreCase(cratesWorld)){
                Location loc = p.getLocation();
                loc.getBlock().setType(Material.BEACON);
                cratesConfig.set("locations." + crateName + ".x", loc.getBlockX());
                cratesConfig.set("locations." + crateName + ".y", loc.getBlockY());
                cratesConfig.set("locations."+ crateName +".z", loc.getBlockZ());
                cratesConfig.saveConfig();
                Map<String, Integer> xyzCache = new HashMap<String, Integer>();
                xyzCache.put("x", loc.getBlockX());
                xyzCache.put("y", loc.getBlockY());
                xyzCache.put("z", loc.getBlockZ());
                cratesLocations.put(crateName, xyzCache);
                msg.send(p, "e", "Crate " + crateName + " successfully created");
            }else{
                msg.send(p, "4", "Crates are only allowed to be created in world " + cratesWorld);
            }
        }else{
            msg.send(p, "c", "Crate " + crateName + " already exist!");
        }
    }

    public void commandRemoveCrate(Player p, String crateName){
        if(cratesLocations.keySet().contains(crateName)){
            World world = Bukkit.getWorld(cratesWorld);
            Double x = Double.parseDouble(String.valueOf(cratesLocations.get(crateName).get("x")));
            Double y = Double.parseDouble(String.valueOf(cratesLocations.get(crateName).get("y")));
            Double z = Double.parseDouble(String.valueOf(cratesLocations.get(crateName).get("z")));
            Location loc = new Location(world, x, y, z);
            loc.getBlock().setType(Material.AIR);
            cratesConfig.set("locations." + crateName, null);
            cratesConfig.saveConfig();
            cratesLocations.remove(crateName);
            msg.send(p, "e", "Crate " + crateName + " successfully removed");
        }else{
            msg.send(p, "c", "Crate " + crateName + " does not exist!");
        }
    }

    public void commandCratesList(Player p){
        p.sendMessage("---------------");
        msg.send(p, "d", "Crates list (locations included):");
        for(String crateName : cratesLocations.keySet()){
            msg.send(p, "e", crateName + ": " + ChatColor.GREEN + "X: " + cratesLocations.get(crateName).get("x") + "  Y: " + cratesLocations.get(crateName).get("y") + "  Z: " + cratesLocations.get(crateName).get("z"));
        }
        p.sendMessage("---------------");
    }
}
