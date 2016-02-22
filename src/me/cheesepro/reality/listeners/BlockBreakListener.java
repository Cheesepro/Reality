package me.cheesepro.reality.listeners;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2015-07-14.
 */
public class BlockBreakListener implements Listener{

    Reality plugin;
    Map<String, Map<String, Integer>> cratesLocations;
    Tools tools;
    Messenger msg;

    public BlockBreakListener(Reality plugin){
        this.plugin = plugin;
        cratesLocations = plugin.getCratesLocations();
        tools = new Tools(plugin);
        msg = new Messenger(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        if(block.getType() == Material.BEACON){
            if(tools.validateCrate(block.getLocation().getBlockX(),block.getLocation().getBlockY(),block.getLocation().getBlockZ())){
                e.setCancelled(true);
                msg.send(e.getPlayer(), "c", "You are not allowed to break lucky crates!");
            }
        }else if (block.getType()==Material.BED_BLOCK){
            //WEIRD BED SPAWN THING THAT ESSENTIAL MESS WITH
            Location bedspawn = new Location(Bukkit.getWorld("world"), -0.5,66,0.5,(float)158.413,(float)5.324);
            e.getPlayer().setBedSpawnLocation(bedspawn, true);
            //END
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> destroyed = event.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            if(tools.validateCrate(block.getLocation().getBlockX(),block.getLocation().getBlockY(),block.getLocation().getBlockZ()))
                it.remove();
        }
    }



}
