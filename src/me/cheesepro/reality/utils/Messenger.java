package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Mark on 2015-04-03.
 */
public class Messenger {
    Reality plugin;
    ChatColor DARKRED = ChatColor.DARK_RED;
    ChatColor RED = ChatColor.RED;
    ChatColor YELLOW = ChatColor.YELLOW;
    ChatColor DARKGREEN = ChatColor.DARK_GREEN;
    ChatColor GREEN = ChatColor.GREEN;
    ChatColor AQUA = ChatColor.AQUA;
    ChatColor DARKAQUA = ChatColor.DARK_AQUA;
    ChatColor BLUE = ChatColor.BLUE;
    ChatColor DARKBLUE = ChatColor.DARK_BLUE;
    ChatColor PURPLE = ChatColor.LIGHT_PURPLE;
    ChatColor DARKPURPLE = ChatColor.DARK_PURPLE;
    ChatColor WHITE = ChatColor.WHITE;
    ChatColor GREY = ChatColor.GRAY;
    ChatColor BLACK = ChatColor.BLACK;
    ChatColor color;
    String prefix = "";

    public Messenger(Reality plugin)
    {
        this.plugin = plugin;
    }

    public void send(Player p, String c, String msg)
    {
        if (c.equalsIgnoreCase("*"))
        {
            p.sendMessage(prefix + " " + ChatColor.translateAlternateColorCodes('&', msg));
        }
        else
        {
            color(c);
            p.sendMessage(prefix + " " + this.color.toString() + msg);
        }
    }

    public void broadcast(String msg){
        Bukkit.broadcastMessage(prefix + " " + ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void noPermission(Player p){
        send(p, "4", "Sorry, but you have insufficient permissions. ");
    }

    private void color(String c)
    {
        if (c.equalsIgnoreCase("4")) {
            this.color = this.DARKRED;
        }
        if (c.equalsIgnoreCase("c")) {
            this.color = this.RED;
        }
        if (c.equalsIgnoreCase("e")) {
            this.color = this.YELLOW;
        }
        if (c.equalsIgnoreCase("2")) {
            this.color = this.DARKGREEN;
        }
        if (c.equalsIgnoreCase("a")) {
            this.color = this.GREEN;
        }
        if (c.equalsIgnoreCase("b")) {
            this.color = this.AQUA;
        }
        if (c.equalsIgnoreCase("9")) {
            this.color = this.BLUE;
        }
        if (c.equalsIgnoreCase("1")) {
            this.color = this.DARKBLUE;
        }
        if (c.equalsIgnoreCase("d")) {
            this.color = this.PURPLE;
        }
        if (c.equalsIgnoreCase("5")) {
            this.color = this.DARKPURPLE;
        }
        if (c.equalsIgnoreCase("f")) {
            this.color = this.WHITE;
        }
        if (c.equalsIgnoreCase("7")) {
            this.color = this.GREY;
        }
        if (c.equalsIgnoreCase("0")) {
            this.color = this.BLACK;
        }
        if (c.equalsIgnoreCase("3")) {
            this.color = this.DARKAQUA;
        }
    }
}
