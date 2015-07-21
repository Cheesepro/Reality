package me.cheesepro.reality.luckycrates;

import me.cheesepro.reality.Reality;
import me.cheesepro.reality.eventhandlers.PlayerKillMobEvent;
import me.cheesepro.reality.utils.Messenger;
import me.cheesepro.reality.utils.Tools;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Mark on 2015-07-05.
 */
public class KeysGiver implements Listener {

    Reality plugin;
    Messenger msg;
    Tools tools;
    ItemStack crateKey;

    public KeysGiver(Reality plugin){
        this.plugin = plugin;
        msg = new Messenger(plugin);
        tools = new Tools(plugin);
        crateKey = plugin.getCrateKey();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onKillMob(PlayerKillMobEvent event){
        Player p = event.getPlayer();
        Entity e = event.getMob();
        if (e instanceof Bat){
            if(tools.randInt(1, 100)<3){giveKey(p);}
        }else if (e instanceof Rabbit){
            if(((Rabbit)e).getRabbitType()== Rabbit.Type.THE_KILLER_BUNNY){
                if(tools.randInt(1, 100)<80){giveKey(p);}
            }
            if(tools.randInt(1, 100)<1){giveKey(p);}
        }else if (e instanceof Squid){
            if(tools.randInt(1, 100)<1){giveKey(p);}
        }else if (e instanceof Villager){
            if(tools.randInt(1, 100)<5){giveKey(p);}
        }else if (e instanceof CaveSpider){
            if(tools.randInt(1, 100)<10){giveKey(p);}
        }else if (e instanceof Enderman){
            if(tools.randInt(1, 100)<3){giveKey(p);}
        }else if (e instanceof PigZombie){
            if(tools.randInt(1, 100)<2){giveKey(p);}
        }else if (e instanceof Blaze){
            if(tools.randInt(1, 100)<8){giveKey(p);}
        }else if (e instanceof Creeper) {
            if (((Creeper)e).isPowered()) {
                if (tools.randInt(1, 100) < 99){giveKey(p);}
            }else{
                if (tools.randInt(1, 100) < 1) {
                    giveKey(p);
                }
            }
        }else if (e instanceof Guardian){
            if(((Guardian)e).isElder()){
                if(tools.randInt(1, 100)<80){giveKey(p);}
            }else{
                if(tools.randInt(1, 100)<30){giveKey(p);}
            }
        }else if (e instanceof Endermite){
            if(tools.randInt(1, 100)<5){giveKey(p);}
        }else if (e instanceof Ghast){
            if(tools.randInt(1, 100)<5){giveKey(p);}
        }else if (e instanceof MagmaCube){
            if(tools.randInt(1, 100)<8){giveKey(p);}
        }else if (e instanceof Silverfish){
            if(tools.randInt(1, 100)<6){giveKey(p);}
        }else if (e instanceof Skeleton){
            if(((Skeleton)e).getSkeletonType()==Skeleton.SkeletonType.WITHER){
                if(tools.randInt(1, 100)<20){giveKey(p);}
            }else{
                if(tools.randInt(1, 100)<1){giveKey(p);}
            }
        }else if (e instanceof Wither){
            if(tools.randInt(1, 100)<1){giveKey(p);}
        }else if (e instanceof Slime){
            if(tools.randInt(1, 100)<2){giveKey(p);}
        }else if (e instanceof Witch){
            if(tools.randInt(1, 100)<35){giveKey(p);}
        }else if (e instanceof Zombie){
            if(((Zombie)e).isVillager()){
                if(tools.randInt(1, 100)<10){giveKey(p);}
            }else if(((Zombie)e).isBaby()){
                if(tools.randInt(1, 100)<30){giveKey(p);}
            }else {
                if(tools.randInt(1, 100)<1){giveKey(p);}
            }
        }
    }

    private void giveKey(Player p){
        p.getInventory().addItem(crateKey);
        msg.send(p, "*", ChatColor.RED.toString() + ChatColor.BOLD + "Congratulations! " + ChatColor.YELLOW.toString() + "You just found a lucky crate key! Now you can go to spawn and see what can be unlocked by this key!");
    }

}