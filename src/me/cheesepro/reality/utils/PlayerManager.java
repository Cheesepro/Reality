package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mark on 2015-05-01.
 */
public class PlayerManager {

    Reality plugin;
    Map<UUID, Map<String, String>> playersINFO;
    Map<String, Integer> levels;
    Config storageConfig;
    Messenger msg;

    public PlayerManager(Reality plugin){
        this.plugin = plugin;
        playersINFO = plugin.getPlayersINFO();
        storageConfig = plugin.getStorageConfig();
        levels = plugin.getLevels();
        msg = new Messenger(plugin);
    }

    public String getRank(UUID id){
        if(playersINFO.get(id)!=null){
            if (playersINFO.get(id).get("rank")!=null){
                return String.valueOf(playersINFO.get(id).get("rank"));
            }
        }
        return null;
    }

    public Integer getXP(UUID id){
        if(playersINFO.get(id)!=null){
            if (playersINFO.get(id).get("xp")!=null){
                try{
                    return Integer.parseInt(playersINFO.get(id).get("xp"));
                }catch (NumberFormatException e){
                    return -1;
                }
            }
        }
        return null;
    }

    public Integer getLevel(UUID id){
        if(playersINFO.get(id)!=null){
            if (playersINFO.get(id).get("level")!=null){
                try{
                    return Integer.parseInt(playersINFO.get(id).get("level"));
                }catch (NumberFormatException e){
                    return -1;
                }
            }
        }
        return null;
    }

    public void setRank(UUID id, String rank){
        if(playersINFO.get(id)!=null){
            storageConfig.set("players."+id.toString()+".rank", rank);
            storageConfig.saveConfig();
            Map<String, String> cache = playersINFO.get(id);
            cache.put("rank", rank);
            playersINFO.put(id, cache);
        }
    }

    public void addXP(UUID id, int xp){
        if(playersINFO.get(id)!=null){
            if(getLevel(id)==99){
                //TODO add money (vault) support
                msg.send(Bukkit.getPlayer(id), "e", "You earned $" + xp);
            }
            storageConfig.set("players."+id.toString()+".xp", Integer.parseInt(playersINFO.get(id).get("xp"))+xp);
            storageConfig.saveConfig();
            Map<String, String> cache = playersINFO.get(id);
            cache.put("xp", String.valueOf(Integer.parseInt(playersINFO.get(id).get("xp"))+xp));
            playersINFO.put(id, cache);
        }
    }

    public void setLevel(UUID id, int level){
        if(playersINFO.get(id)!=null){
            storageConfig.set("players."+id.toString()+".level", level);
            storageConfig.saveConfig();
            Map<String, String> cache = playersINFO.get(id);
            cache.put("level", String.valueOf(level));
            playersINFO.put(id, cache);
        }
    }

    public String levelUp(UUID id){
        if(playersINFO.get(id)!=null){
            int nextH = Integer.parseInt(playersINFO.get(id).get("level"))+1;
            String next = String.valueOf(nextH);
            if(Integer.parseInt(playersINFO.get(id).get("xp"))>=levels.get(next)){
                setLevel(id, nextH);
                Firework f = Bukkit.getPlayer(id).getWorld().spawn(Bukkit.getPlayer(id).getLocation(), Firework.class);
                FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(FireworkEffect.builder()
                        .flicker(true)
                        .trail(true)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.NAVY)
                        .withFade(Color.AQUA)
                        .build());
                fm.setPower(0);
                f.setFireworkMeta(fm);
                GraphicalAPI.sendTitleToPlayer(Bukkit.getPlayer(id), 1, 8, 1, "&a&lLeveled up!", "&f&lYou are now level " + next);
                msg.send(Bukkit.getPlayer(id), "d", "Leveled UP! You are now level " + next);
                return "yes";
            } else{
                return "no"+String.valueOf(levels.get(next) - Integer.parseInt(playersINFO.get(id).get("xp")))+"#"+next;
            }
        }
        return null;
    }

    public boolean hasDefaultPermission(Player p){
        if(p.hasPermission("reality.default") || p.hasPermission("reality.admin")){
            return true;
        }else{
            return false;
        }
    }

    public boolean hasAdminPermission(Player p){
        if(p.hasPermission("reality.admin")){
            return true;
        }else{
            return false;
        }
    }
}
