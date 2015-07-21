package me.cheesepro.reality.bosses;

import me.cheesepro.reality.Reality;
import org.bukkit.entity.Player;

/**
 * Created by Mark on 2015-07-21.
 */
public class BossesAPI {

    Reality plugin;

    public BossesAPI(Reality plugin) {
        this.plugin = plugin;

    }

    public boolean isPlayerPlaying(Player p) {
        return false;
    }

    public String getBossPlayerFighting(Player p) {
        return "";
    }

    public void test(){

    }

}
